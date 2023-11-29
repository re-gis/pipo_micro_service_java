package com.manning.bippo.aggregator.controller;

import com.google.gson.GsonBuilder;
import com.manning.bippo.aggregator.service.SearchMetadataBuilderService;
import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.commons.dto.PropertySearchFilter;
import com.manning.bippo.dao.*;
import com.manning.bippo.dao.pojo.AddressSemantics;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.dao.pojo.TrestleHAR;
import com.manning.bippo.dao.rets.pojo.RetsMetadata;
import com.manning.bippo.dao.rets.pojo.RetsResource;
import com.manning.bippo.service.mapping.RetsBippoMappingService;
import com.manning.bippo.service.queuing.QueuingService;
import com.manning.bippo.service.queuing.impl.RabbitMQSenderService;
import com.manning.bippo.service.rets.RetsService;
import com.manning.bippo.service.trigger.AddressSemanticizationTriggerService;
import com.manning.bippo.service.trigger.RetsTriggerService;
import java.util.Arrays;
import org.apache.commons.lang.time.DateUtils;
import org.joda.time.DateTime;
import org.realtors.rets.client.RetsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/trigger")
public class TriggerController
{
    @Autowired
    RetsTriggerService retsTriggerService;

    @Autowired
    RetsBippoMappingService retsBippoMappingService;

    @Autowired
    QueuingService queuingService;

    @Autowired
    RabbitMQSenderService rabbitMQSenderService;

    @Autowired
    NtreisPropertyRepository ntreisPropertyRepository;

    @Autowired
    TrestleHARRepository trestleHARRepository;

    @Autowired
    AddressSemanticsRepository addressSemanticsRepository;

//    @RequestMapping(value = "/mls/updateActive", method = RequestMethod.POST)
//    public void triggerMLSProcessingUpdateActives(@RequestParam(defaultValue = "Active", required = false) String status,
//                                                  @RequestParam(defaultValue = "1000", required = false) int pageSize)
//    {
//        Page<Integer> activeProperties = ntreisPropertyRepository.getMatrixUniqueIdsByStatus(status, new PageRequest(0, pageSize));
//        rabbitMQSenderService.updateMLSRecords(activeProperties.getContent());
//
//        for (int i = 1; i < activeProperties.getTotalPages(); i++) {
//            rabbitMQSenderService.updateMLSRecords(ntreisPropertyRepository.getMatrixUniqueIdsByStatus(status, new PageRequest(0, pageSize)).getContent());
//        }
//    }


    @RequestMapping(value = "/mls/byFilterAndDays", method = RequestMethod.POST)
    public void triggerMLSProcessing(@RequestBody PropertySearchFilter propertySearchFilter,
                                     @RequestParam Date importForDay,
                                     @RequestParam(defaultValue = "0", required = false) int days)
    {
        for (int day = days; day >= 0; day--) {
            Date startTime = DateUtils.addHours(importForDay, -day * 24);
            Date previousStartTime = startTime;

            for (int hour = 1; hour <= 24; hour++) {
                Date endTime = DateUtils.addHours(previousStartTime, 1);
                propertySearchFilter.setStatusChangeTimestampFrom(previousStartTime);
                propertySearchFilter.setStatusChangeTimestampTo(endTime);
                LogUtil.debug("Queuing for RETS Search By Filter between dates {} - {}", previousStartTime, endTime);
                rabbitMQSenderService.queuePropertySearchFilter(propertySearchFilter);
                previousStartTime = endTime;
            }
        }
    }

    @RequestMapping(value = "/mls/byFilterExact", method = RequestMethod.POST)
    public void triggerMLSProcessing(@RequestBody PropertySearchFilter propertySearchFilter)
    {
        this.rabbitMQSenderService.queuePropertySearchFilter(propertySearchFilter);
    }

    // from external system
    @RequestMapping(value = "/mls/byDate", method = RequestMethod.POST)
    public void triggerMLSProcessing(@RequestParam Date importForDay, @RequestParam(defaultValue = "0", required = false) int days)
    {
        Date startTime = DateUtils.addDays(importForDay, -days);
        for (int day = days; day >= 0; day--) {
            Date endTime = DateUtils.addDays(importForDay, -day + 1);
            LogUtil.debug("Queueing ntreis update from ext system startTime: {} endTime: {}", startTime, endTime);
            rabbitMQSenderService.queueDownloadMLS(startTime, endTime);
            startTime = endTime;
        }
    }

    @RequestMapping(value = "/mls/byNumber", method = RequestMethod.POST)
    public void triggerMLSProcessing(@RequestParam int mlsNumber)
    {
        this.rabbitMQSenderService.updateMlsRecordForNumber(mlsNumber);
    }

    @RequestMapping(value = "/mls/byMatrix", method = RequestMethod.POST)
    public void triggerMLSProcessing(@RequestParam String[] matrixIds)
    {
        this.rabbitMQSenderService.updateMLSRecords(Arrays.asList(matrixIds));
    }

    @Autowired
    AddressSemanticizationTriggerService addressSemanticizationService;

    @RequestMapping(value = "/address_sematicize", method = RequestMethod.POST)
    public void triggerAddressSemanitization()
    {
        addressSemanticizationService.trigger();
    }

    @Autowired
    SearchMetadataBuilderService searchMetadataBuilderService;

    @RequestMapping(value = "/search/metadata", method = RequestMethod.POST)
    public void triggerBuildSearchMetadata() throws RetsException
    {
        searchMetadataBuilderService.buildMetadataService();
    }

    @Autowired
    RetsService retsService;

    @Autowired
    RetsMetadataRepository retsMetadataRepository;

    @RequestMapping(value = "/rets/metadata", method = RequestMethod.POST)
    public void triggerBuildRetsMetadata(@RequestParam String resourceName) throws RetsException
    {
        RetsResource resourceMetadata = retsService.getResourceMetadata(resourceName);
        RetsMetadata byName = retsMetadataRepository.findByName(resourceName);

        if (byName == null) {
            byName = new RetsMetadata();
            byName.setName(resourceName);
        }

        byName.setValue(resourceMetadata);
        retsMetadataRepository.save(byName);
    }

    @RequestMapping(value = "/photos/check", method = RequestMethod.POST)
    public void checkPhotosStatus(@ModelAttribute PropertySearchFilter propertySearchFilter, @RequestParam(required = false, defaultValue = "0") int page)
    {
        if (propertySearchFilter.getStatusChangeTimestampFrom() == null) {
            propertySearchFilter.setStatusChangeTimestampFrom(DateUtils.addMonths(new Date(), -14));
        }

        queuingService.queuePhotoDownloadSinglePage(propertySearchFilter, page);
    }


    @RequestMapping(value = "/latlong/check", method = RequestMethod.POST)
    public void checkPhotosStatus(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "100") int size)
    {
        queuingService.queueDownloadLatLong(page, size);
    }

    @RequestMapping(value = "/rabbitmq/queueCheck", method = RequestMethod.GET)
    public String checkRabbitMqStatus()
    {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this.rabbitMQSenderService.debugLogAllQueues());
    }

    @RequestMapping(value = "/debugMlsUpdateHook", method = RequestMethod.GET)
    public String queueDebugMlsUpdateHook(@RequestParam Long id)
    {
        final NtreisProperty prop = this.ntreisPropertyRepository.findOne(id);

        if (prop == null) {
            return "No such property";
        }

        final AddressSemantics as = this.addressSemanticsRepository.findTopByNtreisProperty(prop);
        this.rabbitMQSenderService.queueMlsUpdateHook(prop, true, as == null ? -1L : as.getId(), "", 0, true);
        return "Queued MlsUpdateHook:" + prop.getMLSNumber() + "/" + (as == null ? "null" : "as:" + as.getId());
    }

    @RequestMapping(value = "/debugTrestleUpdateHook", method = RequestMethod.GET)
    public String queueDebugTrestleUpdateHook(@RequestParam Integer listingKeyNumeric) {
        final TrestleHAR prop = this.trestleHARRepository.findByListingKeyNumeric(listingKeyNumeric);

        if (prop == null) {
            return "No such property";
        }

        final AddressSemantics as = this.addressSemanticsRepository.findTopByTrestleSystemAndTrestleId(
                prop.getOriginatingSystemName(),
                prop.getId());
        this.rabbitMQSenderService.queueTrestleUpdateHook(prop, true, as == null ? -1L : as.getId(), "", 0, true);
        return "Queued TrestleUpdateHook:" + prop.getListingKeyNumeric() + "/" + (as == null ? "null" : "as:" + as.getId());
    }

    // from external system
    @RequestMapping(value = "/trestle/byDate", method = RequestMethod.POST)
    public void queueDebugTrestleUpdateByDate(
            @RequestParam(defaultValue = "HAR") String originatingSystem,
            @RequestParam Date importForDay,
            @RequestParam(defaultValue = "0", required = false) int days) {

        Date startTime = DateUtils.addDays(importForDay, -days);
        for (int day = days; day >= 0; day--) {
            Date endTime = DateUtils.addDays(importForDay, -day + 1);
            LogUtil.debug(
                    "Queueing trestle update from ext system originatingSystem: {} startTime: {} endTime: {}",
                    originatingSystem,
                    startTime,
                    endTime);
            rabbitMQSenderService.queueDownloadTrestle(originatingSystem, startTime, endTime);
            startTime = endTime;
        }
    }

    // from external system
    @RequestMapping(value = "/debugTrestleUpdate", method = RequestMethod.GET)
    public void queueDebugTrestleUpdate(@RequestParam String originatingSystem, @RequestParam Date startTime, @RequestParam Date endTime)
    {
        LogUtil.debug("Queuing Trestle update between {} - {} in {}", startTime, endTime, originatingSystem);
        this.rabbitMQSenderService.queueDownloadTrestle(originatingSystem, startTime, endTime);
    }

    @RequestMapping(value = "/debugTrestleMlsNumber", method = RequestMethod.GET)
    public void queueDebugTrestleMlsNumber(@RequestParam String originatingSystem, @RequestParam Integer mlsNumber)
    {
        LogUtil.debug("Queuing Trestle update for ListingId {} in {}", mlsNumber, originatingSystem);
        this.rabbitMQSenderService.updateMlsRecordTrestleForNumber(originatingSystem, mlsNumber);
    }

    @RequestMapping(value = "/redis/debugRedis", method = RequestMethod.POST)
    public void debugRedisCaches()
    {
        this.rabbitMQSenderService.queueDebugRedisCaches();
    }

    @RequestMapping(value = "/redis/trcRedis", method = RequestMethod.POST)
    public void truncateByRedisRegion(@RequestParam String region)
    {
        LogUtil.debug("Queueing truncation of redis in region '" + region + "'");
        this.rabbitMQSenderService.queueTruncateRedisCache(region);
    }

    @RequestMapping(value = "/redis/trcRedisByDateBefore", method = RequestMethod.POST)
    public void queueTrestleDataToRedis(@RequestParam String region, @RequestParam Date dateBefore) {

        LogUtil.debug("Queueing truncation of redis in region {} before date {}", region, dateBefore);
        this.rabbitMQSenderService.queueTruncateRedisCacheByDate(region, dateBefore);
    }

    @RequestMapping(value = "/redis/queueByStartAndEndTime", method = RequestMethod.POST)
    public void queueSoldDataToRedis(@RequestParam Date startTime, @RequestParam Date endTime,
            @RequestParam(required = false, defaultValue = "true") boolean repairLatLong)
    {
        LogUtil.debug("Queuing for redis between dates {} - {}", startTime, endTime);
        queuingService.queueUpdateRedisCache(startTime, endTime, repairLatLong);
    }

    @RequestMapping(value = "/redis/queuePastWeeks", method = RequestMethod.POST)
    public void queueSoldDataToRedisInWeeks(@RequestParam Integer weeks,
            @RequestParam(required = false, defaultValue = "true") boolean repairLatLong)
    {
        if (weeks == null || weeks.intValue() < 0) {
            return;
        }

        LogUtil.debug("Queuing for redis in the past {} weeks.", weeks);
        final Date now = new Date(System.currentTimeMillis());
        final int wk = weeks.intValue();

        for (int i = 0; i < wk; i++) {
            queuingService.queueUpdateRedisCache(DateUtils.addDays(now, (i + 1) * -7), DateUtils.addDays(now, i * -7), repairLatLong);
        }
    }

    @RequestMapping(value = "/redis/queueTrestleByStartAndEndTime", method = RequestMethod.POST)
    public void queueTrestleDataToRedis(@RequestParam String region, @RequestParam Date startTime, @RequestParam Date endTime)
    {
        LogUtil.debug("Queuing for {} redis between dates {} - {}", region, startTime, endTime);
        queuingService.queueUpdateRedisCacheTrestle(region, startTime, endTime, false);
    }

    @RequestMapping(value = "/redis/queueTrestlePastWeeks", method = RequestMethod.POST)
    public void queueTrestleDataToRedisInWeeks(@RequestParam String region, @RequestParam Integer weeks)
    {
        if (weeks == null || weeks.intValue() < 0) {
            return;
        }

        LogUtil.debug("Queuing for {} redis in the past {} weeks.", region, weeks);
        final Date now = new Date(System.currentTimeMillis());
        final int wk = weeks.intValue();

        for (int i = 0; i < wk; i++) {
            queuingService.queueUpdateRedisCacheTrestle(region, DateUtils.addDays(now, (i + 1) * -7), DateUtils.addDays(now, i * -7), false);
        }
    }

    @RequestMapping(value = "/process/bippoId", method = RequestMethod.POST)
    public void queueProcessNtreisProperty(@RequestParam Integer mlsNumber)
    {
        queuingService.queueSearchPropertiesProcesses(Collections.singletonList(ntreisPropertyRepository.findByMLSNumber(mlsNumber)), true);
    }

    @RequestMapping(value = "/process/queue", method = RequestMethod.POST)
    public void queueProcess(@RequestParam Date importForDay, @RequestParam(defaultValue = "0", required = false) int days)
    {
        for (int day = 0; day <= days; day++) {
            DateTime importFromDate = new DateTime(importForDay);
            DateTime theDay = importFromDate.minusDays(day);
            DateTime theDayStartTime = theDay.withTimeAtStartOfDay();

            for (int hour = 0; hour < 24; hour++) {
                Date startTime = theDayStartTime.plusHours(hour).toDate();
                Date endTime = theDayStartTime.plusHours(hour + 1).toDate();
                LogUtil.debug("Queuing for process properties between dates {} - {}", startTime, endTime);
                List<NtreisProperty> byStatusChangeTimestampBetween = ntreisPropertyRepository.findByStatusChangeTimestampBetween(startTime, endTime);
                queuingService.queueSearchPropertiesProcesses(byStatusChangeTimestampBetween, false);
            }
        }
    }

    @RequestMapping(value = "/redis/queue", method = RequestMethod.POST)
    public void queueSoldDataToRedis(@RequestParam Date importForDay, @RequestParam(defaultValue = "0", required = false) int days,
            @RequestParam(required = false, defaultValue = "true") boolean repairLatLong)
    {
        for (int day = 0; day <= days; day++) {
            DateTime importFromDate = new DateTime(importForDay);
            DateTime theDay = importFromDate.minusDays(day);
            DateTime theDayStartTime = theDay.withTimeAtStartOfDay();

            for (int hour = 0; hour < 24; hour++) {
                Date startTime = theDayStartTime.plusHours(hour).toDate();
                Date endTime = theDayStartTime.plusHours(hour + 1).toDate();
                LogUtil.debug("Queuing for redis between dates {} - {}", startTime, endTime);
                queuingService.queueUpdateRedisCache(startTime, endTime, repairLatLong);
            }
        }
    }

    @RequestMapping(value = "/redis/rents/queue", method = RequestMethod.POST)
    public void queueRentsDataToRedis(@RequestParam Date importForDay, @RequestParam(defaultValue = "0", required = false) int days)
    {

        for (int day = 0; day <= days; day++) {
            DateTime importFromDate = new DateTime(importForDay);
            DateTime theDay = importFromDate.minusDays(day);
            DateTime theDayStartTime = theDay.withTimeAtStartOfDay();

//            for (int hour = 0; hour < 24; hour++)
            {
                Date startTime = theDayStartTime.toDate();
                Date endTime = theDayStartTime.plusHours(24).toDate();

                LogUtil.debug("Queuing for redis between dates {} - {}", startTime, endTime);
                List<NtreisProperty> ntreisProperties = ntreisPropertyRepository.findByPropertyTypeAndStatusChangeTimestampBetween("Residential Lease", startTime, endTime);

                List<Long> propertyIds = ntreisProperties.stream()
                        .filter(p -> p.getLatitude() != null || p.getLongitude() != null)
                        .map(NtreisProperty::getId).collect(Collectors.toList());
                rabbitMQSenderService.queueUpdateRedisCache(propertyIds);
            }
        }
    }

}
