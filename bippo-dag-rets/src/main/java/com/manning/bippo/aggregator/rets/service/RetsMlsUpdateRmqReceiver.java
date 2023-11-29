package com.manning.bippo.aggregator.rets.service;

import com.manning.bippo.aggregator.rets.pojo.TaskUpdateMatrixId;
import com.manning.bippo.aggregator.rets.pojo.TaskUpdateMlsNumber;
import com.manning.bippo.commons.dto.DownloadMultiplePropertyMLS;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = "bippo-dag-update-mls-records")
public class RetsMlsUpdateRmqReceiver
{
    @Autowired
    RetsFeedSynchronizationService retsFeedSynchronizationService;

    @Bean
    public Queue bippoDagUpdateMLSRecordsQueue()
    {
        return new Queue("bippo-dag-update-mls-records");
    }

    @RabbitHandler
    public void process(@Payload DownloadMultiplePropertyMLS ntreis)
    {
        if (ntreis.matrixUniqueId != null) {
            this.retsFeedSynchronizationService.queueResyncData(new TaskUpdateMatrixId(ntreis.matrixUniqueId, ntreis.indexSemantics));
        } else {
            this.retsFeedSynchronizationService.queueResyncData(new TaskUpdateMlsNumber(ntreis.mlsNumber));
        }
    }
}
