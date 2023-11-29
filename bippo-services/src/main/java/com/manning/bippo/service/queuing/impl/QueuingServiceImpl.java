package com.manning.bippo.service.queuing.impl;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.commons.dto.PropertySearchFilter;
import com.manning.bippo.dao.AddressSemanticsRepository;
import com.manning.bippo.dao.NtreisPropertyRepository;
import com.manning.bippo.dao.TaxPropertyRepository;
import com.manning.bippo.dao.filter.PropertySearchSpecification;
import com.manning.bippo.dao.itf.TrestleProperty;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.service.geocode.RooftopLatLongService;
import com.manning.bippo.service.queuing.QueuingService;
import com.manning.bippo.service.trestle.TrestleDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

@Service
public class QueuingServiceImpl implements QueuingService
{

    @Autowired
    TaxPropertyRepository taxPropertyRepository;

    @Autowired
    AddressSemanticsRepository addressSemanticsRepository;

    @Autowired
    NtreisPropertyRepository ntreisPropertyRepository;

    @Autowired
    TrestleDataService trestleDataService;

    @Autowired
    RooftopLatLongService latLongService;

    @Autowired
    RabbitMQSenderService rabbitMQSenderService;

    @Override
    public void queuePhotoDownloadSinglePage(PropertySearchFilter propertySearchFilter, int page)
    {
        Page<NtreisProperty> all = ntreisPropertyRepository.findAll(new PropertySearchSpecification(propertySearchFilter), new PageRequest(page, 100));
        LogUtil.debug("Adding {} ntreis properties to all-photo queue, on page {}", all.getTotalElements(), all.getNumber());
        List<Long> propertyIds = all.getContent().stream().map(NtreisProperty::getId).collect(Collectors.toList());
        rabbitMQSenderService.queueDownloadPhotos(propertyIds);
    }

    @Override
    public void queuePhotoDownload(PropertySearchFilter propertySearchFilter, int page)
    {
        Page<NtreisProperty> all = ntreisPropertyRepository.findAll(new PropertySearchSpecification(propertySearchFilter), new PageRequest(page, 100));
        LogUtil.debug("Adding {} ntreis properties to all-photo queue, on page {}", all.getTotalElements(), all.getNumber());
        List<Long> propertyIds = all.getContent().stream().map(NtreisProperty::getId).collect(Collectors.toList());
        rabbitMQSenderService.queueDownloadPhotos(propertyIds);
        if (page <= all.getTotalPages())
        {
            queuePhotoDownload(propertySearchFilter, all.getNumber() + 1);
        }
    }

    @Override
    public void queuePhotoDownload(List<NtreisProperty> all, boolean alsoQueueFront)
    {
        LogUtil.debug("Adding {} ntreis properties to {} queue", all.size(), alsoQueueFront ? "front-photo/all-photo" : "all-photo");
        List<Long> propertyIds = all.stream().map(NtreisProperty::getId).collect(Collectors.toList());
        rabbitMQSenderService.queueDownloadPhotos(propertyIds);

        if (alsoQueueFront) {
            rabbitMQSenderService.queueDownloadFrontPhotos(propertyIds);
        }
    }

    @Override
    public void queueDownloadLatLong(List<Long> all)
    {
        LogUtil.debug("Adding ntreis properties to queue, on page {}", all.size());
        rabbitMQSenderService.queueDownloadLatLong(all);
    }

    @Override
    public void queueDownloadLatLong(int page, int size)
    {
        Page<Long> all = ntreisPropertyRepository.getPropertyIdsWithNoLatLong(new PageRequest(page, size));
        LogUtil.debug("Queuing {} properties of page {}", all.getSize(), page);
        queueDownloadLatLong(all.getContent());
        if (all.getTotalPages() != page)
        {
            queueDownloadLatLong(page + 1, size);
        }
    }

    @Override
    public void queueDownloadLeaseProperties(List<NtreisProperty> all)
    {
        List<Long> propertyIds = all.stream().map(NtreisProperty::getId).collect(Collectors.toList());
        rabbitMQSenderService.queueDownloadLeaseProperties(propertyIds);
    }

    @Override
    public void queueSearchPropertiesProcesses(List<NtreisProperty> content, boolean forceRun)
    {
        queueDownloadRetsComps(content, forceRun);
        queueDownloadLeaseProperties(content);
//        queuePhotoDownload(content); // These should already be queued when the NtreisProperty was newly added to the database
    }

    private void queueDownloadRetsComps(List<NtreisProperty> content, boolean forceRun)
    {
        content.forEach(subject -> {
            rabbitMQSenderService.queueDownloadRETSAreaComps(subject, forceRun);
        });
    }

    @Override
    public void queueARVComps(List<NtreisProperty> ntreisProperties)
    {
        ntreisProperties.forEach(p -> rabbitMQSenderService.queueARVComps(Collections.singletonList(p.getId())));
    }

    @Override
    public void queueUpdateRedisCache(Date startTime, Date endTime, boolean repairLatLong)
    {
        final List<NtreisProperty> ntreisProperties = ntreisPropertyRepository.findByStatusChangeTimestampBetween(startTime, endTime);
        int repaired = 0, lost = 0;

        for (final ListIterator<NtreisProperty> it = ntreisProperties.listIterator(); it.hasNext(); ) {
            NtreisProperty p = it.next();

            if (p.getLatitude() == null || p.getLongitude() == null) {
                if (!repairLatLong) {
                    // Only try to get the lat/long; if it is not already cached, then abort
                    p = this.latLongService.tryPropagateLatLong(p);

                    if (p != null) {
                        it.set(p);
                        repaired++;
                        continue;
                    }

                    it.remove();
                    lost++;
                    continue;
                }

                try {
                    /*
                     * Attempt to grab the lat/long from RooftopLatLongService
                     * Note that this may cause as many as 1 call each to the
                     * underlying address verification (if the property is not
                     * in address_semantics) and geocoding APIs (if the lat
                     * long is not cached and fresh).
                     */
                    p = this.latLongService.propagateLatLong(p);
                } catch (Exception e) {
                    it.remove();
                    lost++;
                    continue;
                }

                it.set(p);
                repaired++;
            }
        }

        if ((repaired | lost) != 0) {
            LogUtil.debug("Repaired Lat/Long for {} NtreisProperties, {} were lost", repaired, lost);
        }

        rabbitMQSenderService.queueUpdateRedisCache(ntreisProperties.stream()
                .map(NtreisProperty::getId).collect(Collectors.toList()));
    }

    @Override
    public void queueUpdateRedisCacheTrestle(String region, Date startTime, Date endTime, boolean repairLatLong)
    {
        final List<? extends TrestleProperty> trestleProperties = this.trestleDataService.findByRegionAndStatusChangeTimestampBetween(region, startTime, endTime);
        int lost = 0;

        for (final ListIterator<? extends TrestleProperty> it = trestleProperties.listIterator(); it.hasNext(); ) {
            TrestleProperty p = it.next();

            if (p.getLatitude() == null || p.getLongitude() == null) {
                it.remove();
                lost++;
                continue;
            }

            // Repair lat long? Trestle seems to be 100% reliable on having lat/long thus far
        }

        if (lost != 0) {
            LogUtil.debug("Repaired Lat/Long for {} TrestleProperties, {} were lost", 0, lost);
        }

        this.rabbitMQSenderService.queueUpdateRedisCacheTrestle(region, trestleProperties.stream()
                .map(TrestleProperty::getId).collect(Collectors.toList()));
    }
}
