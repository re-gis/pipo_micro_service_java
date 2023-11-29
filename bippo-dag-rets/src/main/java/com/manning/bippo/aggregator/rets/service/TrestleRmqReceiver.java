package com.manning.bippo.aggregator.rets.service;

import com.google.common.collect.Sets;
import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.commons.dto.DownloadTrestleRange;
import com.manning.bippo.commons.dto.DownloadTrestleSingle;
import com.manning.bippo.dao.itf.TrestleProperty;
import com.manning.bippo.dao.pojo.TrestleHAR;
import com.manning.bippo.service.mapping.TrestleBippoMappingService;
import com.manning.bippo.service.mapping.pojo.TrestleApiData;
import com.manning.bippo.service.queuing.impl.RabbitMQSenderService;
import com.manning.bippo.service.trestle.TrestleApiService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = "bippo-dag-trestle")
public class TrestleRmqReceiver {

    private static final Set<String> autoRedisStatuses = Sets.newHashSet("Active", "ActiveUnderContract", "Contigent", "Closed", "Pending");

    @Autowired
    TrestleApiService trestleApiService;

    @Autowired
    TrestleBippoMappingService trestleBippoMappingService;

    @Autowired
    RabbitMQSenderService rabbitMQSenderService;

    @Bean
    public Queue bippoDagTrestle()
    {
        return new Queue("bippo-dag-trestle");
    }

    @RabbitHandler
    public void process(@Payload DownloadTrestleRange downloadTrestleRange) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
            dateFormat.setTimeZone(TimeZone.getTimeZone("America/Chicago"));

            LogUtil.debug("Performing Trestle API sync between start: {} and end: {}",
                    dateFormat.format(downloadTrestleRange.getStartTime()), dateFormat.format(downloadTrestleRange.getEndTime()));
            this.downloadInterval(downloadTrestleRange.getOriginatingSystem(), downloadTrestleRange.getStartTime(), downloadTrestleRange.getEndTime());
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    @RabbitHandler
    public void process(@Payload DownloadTrestleSingle downloadTrestleSingle) {
        try {
            LogUtil.debug("Querying single ID from trestle (ListingId " + downloadTrestleSingle.getListingId() + ")");
            this.downloadSingle(downloadTrestleSingle.getOriginatingSystem(), downloadTrestleSingle.getListingId());
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    private boolean waitUntil(long time) {
        if (time < 1L) {
            return true;
        } else for (long rem; ; ) {
            rem = time - System.currentTimeMillis();

            if (rem > 0L) {
                try {
                    Thread.sleep(time - System.currentTimeMillis());
                } catch (InterruptedException e) {
                    continue;
                }
            }

            return true;
        }
    }

    private void downloadAll(TrestleApiData page, String originatingSystem) {
        if (page == null) {
            throw new IllegalArgumentException("No page to process");
        }

        final List<TrestleProperty> touched = new ArrayList<>();
        final BiConsumer<TrestleApiData, List<TrestleProperty>> update;
        long delayUntil = 0L;
        int recorded = 0, ord = 0;

        switch (originatingSystem) {
            case "HAR":
                update = this.trestleBippoMappingService::updateAllADataHAR;
                break;
            default:
                LogUtil.warn("Cannot download interval for Trestle: unknown originating system " + originatingSystem);
                return;
        }

        do {
            if (++ord > 10) {
                // One third sec delay is 10800 requests per hour, which is our rate limit for the Trestle API
                // But only kick in with this throttle after 10 consecutive pages
                delayUntil = System.currentTimeMillis() + 332L;
            }

            touched.clear();
            update.accept(page, touched);
            touched.removeIf(p -> !autoRedisStatuses.contains(p.getStandardStatus()));

            if (!touched.isEmpty()) {
                this.rabbitMQSenderService.queueUpdateRedisCacheTrestle(originatingSystem, touched.stream().map(TrestleProperty::getId).collect(Collectors.toList()));
            }

            recorded += page.value.length;
        } while (page.hasNextPage() && this.waitUntil(delayUntil) && (page = this.trestleApiService.requestNextPage(page)) != null);

        LogUtil.info("Recorded {} pages, {} new records into {} from Trestle API", ord, recorded, originatingSystem);
    }

    public void downloadInterval(String originatingSystem, Date start, Date end) {
        TrestleApiData page = this.trestleApiService.requestModifiedBetween(originatingSystem, start, end);

        if (page == null) {
            LogUtil.info("Initial request returned no data for Trestle API interval sync");
            return;
        }

        this.downloadAll(page, originatingSystem);
    }

    public void downloadSingle(String originatingSystem, int listingId) {
        TrestleApiData page = this.trestleApiService.requestListingId(originatingSystem, listingId);

        if (page == null) {
            LogUtil.info("No result for Trestle API single sync");
            return;
        }

        this.downloadAll(page, originatingSystem);
    }
}
