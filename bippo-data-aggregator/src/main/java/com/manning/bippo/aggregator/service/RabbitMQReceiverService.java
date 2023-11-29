package com.manning.bippo.aggregator.service;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.commons.dto.*;
import com.manning.bippo.dao.NtreisPropertyRepository;
import com.manning.bippo.dao.PropertyCompsFilterRepository;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.dao.pojo.PropertyComp;
import com.manning.bippo.dao.pojo.PropertyCompsFilter;
import com.manning.bippo.service.ZipCodeService;
import com.manning.bippo.service.comps.CompsService;
import com.manning.bippo.service.comps.CompsServiceImpl;
import com.manning.bippo.service.oi.OnBoardInformaticsPropertyService;
import com.manning.bippo.service.redis.RedisPropertyService;
import com.manning.bippo.service.rets.impl.CompsSearchFilterBuilderService;
import com.manning.bippo.service.trestle.TrestleDataService;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.StopWatch;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RabbitListener(queues = "bippo-dag")
public class RabbitMQReceiverService
{

    @Bean
    public Queue bippoDagQueue()
    {
        return new Queue("bippo-dag");
    }

    @Autowired
    OnBoardInformaticsPropertyService onBoardInformaticsPropertyService;

    @Autowired
    NtreisPropertyRepository ntreisPropertyRepository;

    @Autowired
    TrestleDataService trestleDataService;

    @RabbitHandler
    public void process(@Payload DownloadOnBoardInformaticsResponse downloadOnBoardInformaticsResponse)
    {
        try {
            onBoardInformaticsPropertyService.downloadAllEventsPropertyData(ntreisPropertyRepository.findOne(downloadOnBoardInformaticsResponse.ntreisId));
//            LogUtil.info("Sleeping for 10 seconds ....");
//            Thread.sleep(10 * 1000);
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    @Autowired
    RedisPropertyService redisPropertyService;


    @RabbitHandler
    public void process(@Payload UpdateRedisCache updateRedisCache)
    {
        try {
            LogUtil.info("Handling UpdateRedisCache for {} properties ...", updateRedisCache.propertyIds.size());
            final StopWatch timer = new StopWatch();
            timer.start();

            this.redisPropertyService.savePropertyToRedis(updateRedisCache.propertyIds.stream()
                    .map(id -> ntreisPropertyRepository.findOne(id)).collect(Collectors.toList()));

            timer.stop();
            LogUtil.info("... redisPropertyService.savePropertyToRedis (NTREIS) took {} msec for {} properties.",
                    timer.getTime(), updateRedisCache.propertyIds.size());
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    @RabbitHandler
    public void process(@Payload UpdateRedisCacheTrestle updateRedisCache)
    {
        try {
            LogUtil.info("Handling UpdateRedisCacheTrestle for {} {} properties ...",
                    updateRedisCache.propertyIds.size(), updateRedisCache.region);
            final StopWatch timer = new StopWatch();
            timer.start();

            this.redisPropertyService.saveTrestlePropertyToRedis(
                    this.trestleDataService.findByRegionAndIds(updateRedisCache.region, updateRedisCache.propertyIds));

            timer.stop();
            LogUtil.info("... redisPropertyService.savePropertyToRedis (Trestle) took {} msec for {} {} properties.",
                    timer.getTime(), updateRedisCache.propertyIds.size(), updateRedisCache.region);
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    @RabbitHandler
    public void process(@Payload DebugRedisCaches debugRedisCaches)
    {
        this.redisPropertyService.debugRedisCaches();
    }

    @RabbitHandler
    public void process(@Payload TruncateRedisCache truncateRedisCache)
    {
        try {
            final StopWatch timer = new StopWatch();
            timer.start();

            this.redisPropertyService.truncateByRegion(truncateRedisCache.region);

            timer.stop();
            LogUtil.info("redisPropertyService.truncateByRegion ({}) took {} msec.",  truncateRedisCache.region, timer.getTime());
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }

    @RabbitHandler
    public void process(@Payload TruncateRedisCacheByDate truncateRedisCacheByDate) {
        try {
            final StopWatch timer = new StopWatch();
            timer.start();

            this.redisPropertyService.truncateByRegionAndTime(
                    truncateRedisCacheByDate.region,
                    truncateRedisCacheByDate.dateBefore);

            timer.stop();
            LogUtil.info(
                    "redisPropertyService.truncateByRegionAndDate ({}, {}) took {} msec.",
                    truncateRedisCacheByDate.region,
                    truncateRedisCacheByDate.dateBefore,
                    timer.getTime());
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }
    }
}
