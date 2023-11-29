package com.manning.bippo.aggregator.rets.service;

import com.manning.bippo.aggregator.rets.pojo.TaskDownloadComps;
import com.manning.bippo.aggregator.rets.pojo.TaskDownloadForFilter;
import com.manning.bippo.aggregator.rets.pojo.TaskDownloadForInterval;
import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.commons.dto.*;
import com.manning.bippo.dao.MLSStatusRepository;
import com.manning.bippo.dao.NtreisPropertyRepository;
import com.manning.bippo.dao.PropertyCompsFilterRepository;
import com.manning.bippo.dao.pojo.MLSStatus;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.dao.pojo.PropertyCompsFilter;
import com.manning.bippo.service.queuing.QueuingService;
import com.manning.bippo.service.queuing.impl.RabbitMQSenderService;
import com.manning.bippo.service.rets.impl.CompsSearchFilterBuilderService;
import java.text.SimpleDateFormat;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RabbitListener(queues = "bippo-dag-rets")
public class RetsDownloadRmqReceiver
{
//    @Autowired
//    RetsBippoMappingService retsBippoMappingService;

    @Autowired
    RetsFeedSynchronizationService retsFeedSynchronizationService;

    @Autowired
    MLSStatusRepository mlsStatusRepository;

    @Autowired
    RabbitMQSenderService rabbitMQSenderService;

    @Autowired
    NtreisPropertyRepository ntreisPropertyRepository;

    @Autowired
    QueuingService queuingService;

    @Bean
    public Queue bippoDagRetsQueue()
    {
        return new Queue("bippo-dag-rets");
    }

    /*
     * Be aware that all RabbitHandler methods within this class block on the
     * same queue. If one call to RetsFeedSynchronizationService blocks, none
     * of the other methods within this class can proceed until that is done
     * blocking. We use unbounded LinkedBlockingQueues to accommodate this.
     */

    @RabbitHandler
    public void process(@Payload PropertySearchFilter retsSearch)
    {
        try {
            if (retsSearch.getStatusChangeTimestampFrom() == null) {
                retsSearch.setStatusChangeTimestampFrom(DateUtils.addMonths(new Date(), -12));
            }

            List<String> mlsStatus = mlsStatusRepository.findByInvalidOrderByValueAsc(true).stream().map(MLSStatus::getValue).collect(Collectors.toList());
            retsSearch.setNotInMLSStatus(mlsStatus);

            LogUtil.debug("<< Queueing RetsFeed cron update for a PropertySearchFilter");
            this.retsFeedSynchronizationService.queueDownloadFilter(new TaskDownloadForFilter(retsSearch));
            LogUtil.debug(">> Filter update queued.");
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    @RabbitHandler
    public void process(@Payload DownloadRetsUpdated downloadRetsUpdated)
    {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
            dateFormat.setTimeZone(TimeZone.getTimeZone("America/Chicago"));

            LogUtil.debug("<< Queueing RetsFeed cron update between start: {} and end: {}",
                    dateFormat.format(downloadRetsUpdated.getStartTime()), dateFormat.format(downloadRetsUpdated.getEndTime()));
            this.retsFeedSynchronizationService.queueDownloadInterval(new TaskDownloadForInterval(downloadRetsUpdated, queuingService, rabbitMQSenderService));
            LogUtil.debug(">> Interval update queued.");
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    @Autowired
    CompsSearchFilterBuilderService compsSearchFilterBuilderService;

    @Autowired
    PropertyCompsFilterRepository propertyCompsFilterRepository;

    @RabbitHandler
    public void process(@Payload DownloadLeaseProperties downloadLeaseProperties)
    {
        try {
            LogUtil.debug("Downloading Lease comps for {} subjects.", downloadLeaseProperties.getPropertyIds().size());
            List<NtreisProperty> ntreisProperties = ntreisPropertyRepository.findAll(downloadLeaseProperties.getPropertyIds());

            for (NtreisProperty ntreisProperty : ntreisProperties) {
                PropertyCompsFilter propertyCompsFilter = propertyCompsFilterRepository.findTopBySubjectOrderByUpdated(ntreisProperty);
                /**
                 * TODO: Add logic to be updated after certain date.
                 */
                if (propertyCompsFilter == null || propertyCompsFilter.getLeaseFilterUsed() == null) {
                    if (ntreisProperty.getYearBuilt() != null && ntreisProperty.getSqFtTotal() != null &&
                            ntreisProperty.getLatitude() != null && ntreisProperty.getLongitude() != null) {
                        List<CompsFilterCalculated> compsFilterCalculatedList = compsSearchFilterBuilderService
                                .buildLeaseCompFilters("", ntreisProperty.getSqFtTotal(), ntreisProperty.getYearBuilt(), "L", "LSE", Collections.emptyList());

                        LogUtil.debug("<< Queueing RetsFeed download for Lease comps for ntreis id {}", ntreisProperty.getId());
                        this.retsFeedSynchronizationService.queueDownloadComps(new TaskDownloadComps(ntreisProperty, compsFilterCalculatedList, "LEASE"));
                        LogUtil.debug(">> Lease comps queued.");
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    @RabbitHandler
    public void process(@Payload DownloadRetsComps downloadRetsComps)
    {
        try {
            LogUtil.debug("Downloading ARV comps for {} subjects.", downloadRetsComps.getPropertyIds().size());
            List<NtreisProperty> ntreisProperties = ntreisPropertyRepository.findAll(downloadRetsComps.getPropertyIds());

            String[] propertySubType = {"RESCON", "RESRAN", "RESDUP", "RESFAM", "RESTOW"};

            for (NtreisProperty ntreisProperty : ntreisProperties) {
                PropertyCompsFilter propertyCompsFilter = propertyCompsFilterRepository.findTopBySubjectOrderByUpdated(ntreisProperty);
                /**
                 * TODO: Add logic to be updated after certain date.
                 */
                if (downloadRetsComps.getForceRun() || propertyCompsFilter == null || propertyCompsFilter.getArvFilterUsed() == null) {
                    if (ntreisProperty.getYearBuilt() != null && ntreisProperty.getSqFtTotal() != null &&
                            ntreisProperty.getLatitude() != null && ntreisProperty.getLongitude() != null) {
                        List<CompsFilterCalculated> compsFilterCalculatedList = compsSearchFilterBuilderService
                                .buildCompsFilters("", ntreisProperty.getSqFtTotal(), ntreisProperty.getYearBuilt(), "S", Arrays.asList(propertySubType));

                        LogUtil.debug("<< Queueing RetsFeed download for ARV comps for ntreis id {}", ntreisProperty.getId());
                        this.retsFeedSynchronizationService.queueDownloadComps(new TaskDownloadComps(ntreisProperty, compsFilterCalculatedList, "ARV"));
                        LogUtil.debug(">> ARV comps queued.");
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }

    }

}
