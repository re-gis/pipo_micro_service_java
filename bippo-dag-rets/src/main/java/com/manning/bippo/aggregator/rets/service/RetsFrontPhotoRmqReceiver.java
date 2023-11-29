package com.manning.bippo.aggregator.rets.service;

import com.manning.bippo.aggregator.rets.pojo.TaskFrontPhotos;
import com.manning.bippo.commons.dto.DownloadMultiplePropertyFrontPhotos;
import com.manning.bippo.dao.NtreisPropertyRepository;
import com.manning.bippo.dao.pojo.NtreisProperty;
import java.util.Iterator;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = "bippo-dag-rets-front-photos")
public class RetsFrontPhotoRmqReceiver
{
    public static final int EXECUTOR_THREADS = 5;

    @Bean
    public Queue bippoRetsFrontPhotos()
    {
        return new Queue("bippo-dag-rets-front-photos");
    }

    @Autowired
    RetsFeedSynchronizationService retsFeedSynchronizationService;

    @Autowired
    NtreisPropertyRepository ntreisPropertyRepository;

    @RabbitHandler
    @Transactional
    public void process(@Payload DownloadMultiplePropertyFrontPhotos downloadPhoto)
    {
        final List<NtreisProperty> properties = this.ntreisPropertyRepository.findAll(downloadPhoto.propertyIds);

        for (final Iterator<NtreisProperty> it = properties.iterator(); it.hasNext(); ) {
            final NtreisProperty prop = it.next();

            if (prop.getNtreisPhotos().stream().anyMatch(p -> p.getType().equalsIgnoreCase("Photo") || p.getType().equalsIgnoreCase("DoNotCheck"))) {
                // We already have a [front] Photo, or we have a DoNotCheck
                it.remove();
            }
        }

        for (NtreisProperty prop : properties) {
            this.retsFeedSynchronizationService.queueFrontPhotos(new TaskFrontPhotos(prop.getId().longValue()));
        }
    }
}
