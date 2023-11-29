package com.manning.bippo.aggregator.service;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.commons.dto.BuildARVComps;
import com.manning.bippo.commons.dto.CompsFilterCalculated;
import com.manning.bippo.dao.NtreisPropertyRepository;
import com.manning.bippo.dao.PropertyCompsFilterRepository;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.dao.pojo.PropertyComp;
import com.manning.bippo.dao.pojo.PropertyCompsFilter;
import com.manning.bippo.service.comps.CompsService;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.StopWatch;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RabbitListener(queues = "bippo-dag-arv")
public class RabbitMQARVCompsReceiverService
{

    @Bean
    public Queue bippoDagARVQueue()
    {
        return new Queue("bippo-dag-arv");
    }

    @Autowired
    CompsService compsService;

    @Autowired
    PropertyCompsFilterRepository propertyCompsFilterRepository;

    @Autowired
    NtreisPropertyRepository ntreisPropertyRepository;

    @RabbitHandler
    @Transactional
    public void process(@Payload BuildARVComps buildARVComps)
    {
        LogUtil.debug("Disabled ARV Comps calculation ...");
    }
}
