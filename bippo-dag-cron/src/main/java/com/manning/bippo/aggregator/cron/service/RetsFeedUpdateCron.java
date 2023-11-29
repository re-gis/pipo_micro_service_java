package com.manning.bippo.aggregator.cron.service;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.dao.NtreisPropertyRepository;
import com.manning.bippo.dao.PropertyCompsFilterRepository;
import com.manning.bippo.dao.pojo.PropertyCompsFilter;
import com.manning.bippo.service.mapping.RetsBippoMappingService;
import com.manning.bippo.service.queuing.QueuingService;
import com.manning.bippo.service.queuing.impl.RabbitMQSenderService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

@Service
public class RetsFeedUpdateCron
{

    @Autowired
    RetsBippoMappingService retsBippoMappingService;

    @Autowired
    QueuingService queuingService;

    @Autowired
    NtreisPropertyRepository ntreisPropertyRepository;

    @Autowired
    RabbitMQSenderService rabbitMQSenderService;

    @Scheduled(cron = "0 */15 * * * *")
    public void triggerRetsFeedUpdate()
    {
        final Date endTime = new Date(), startTime = DateUtils.addMinutes(endTime, -17);
        LogUtil.debug("Queuing RetsFeed for HAR & NTREIS Cron Update between start: {} and end: {}", startTime, endTime);

        rabbitMQSenderService.queueDownloadMLS(startTime, endTime);
        rabbitMQSenderService.queueDownloadTrestle("HAR", startTime, endTime);
    }

//    @Autowired
//    PropertyCompsFilterRepository propertyCompsFilterRepository;

//    /**
//     * Every Midnight trigger the ARV Filter
//     */
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void triggerARVFilters()
//    {
//        queuePropertyCompsFilter(0, 100);
//
//    }

//    private void queuePropertyCompsFilter(int page, int size)
//    {
//        Page<PropertyCompsFilter> all = propertyCompsFilterRepository.findAll(new PageRequest(page, size));
//
//        LogUtil.debug("Queuing ARV PropertyCompsFilter's on page : {}", page);
//
//        Date sixtyDays = DateUtils.addDays(new Date(), -60);
//        queuingService.queueARVComps(all.getContent().stream()
//                .filter(p -> p.getUpdated().before(sixtyDays))
//                .map(PropertyCompsFilter::getSubject)
//                .collect(Collectors.toList())
//        );
//
//        if (all.getTotalPages() != all.getNumber())
//        {
//            queuePropertyCompsFilter(page++, size);
//        }
//    }


}
