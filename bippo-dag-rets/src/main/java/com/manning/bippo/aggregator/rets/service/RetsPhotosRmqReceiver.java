package com.manning.bippo.aggregator.rets.service;

import com.manning.bippo.aggregator.rets.pojo.TaskAllPhotos;
import com.manning.bippo.commons.dto.DownloadMultiplePropertyPhotos;
import com.manning.bippo.commons.dto.DownloadPhoto;
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
@RabbitListener(queues = "bippo-dag-rets-photos")
public class RetsPhotosRmqReceiver
{
    @Autowired
    RetsFeedSynchronizationService retsFeedSynchronizationService;

    @Autowired
    NtreisPropertyRepository ntreisPropertyRepository;

    @Bean
    public Queue bippoDagRetsPhotosQueue()
    {
        return new Queue("bippo-dag-rets-photos");
    }

    @RabbitHandler
    @Transactional
    public void process(@Payload DownloadPhoto downloadPhoto)
    {
        final NtreisProperty prop = this.ntreisPropertyRepository.findOne(downloadPhoto.ntreisPropertyId);

        if (prop.getNtreisPhotos().stream().anyMatch(p -> p.getType().equalsIgnoreCase("HighRes") || p.getType().equalsIgnoreCase("LargePhoto") || p.getType().equalsIgnoreCase("DoNotCheck"))) {
            return;
        }

        this.retsFeedSynchronizationService.queueAllPhotos(new TaskAllPhotos(downloadPhoto.ntreisPropertyId.longValue()));
    }

    @RabbitHandler
    @Transactional
    public void process(@Payload DownloadMultiplePropertyPhotos downloadPhotos)
    {
        final List<NtreisProperty> properties = this.ntreisPropertyRepository.findAll(downloadPhotos.propertyIds);

        for (final Iterator<NtreisProperty> it = properties.iterator(); it.hasNext(); ) {
            final NtreisProperty prop = it.next();

            if (prop.getNtreisPhotos().stream().anyMatch(p -> p.getType().equalsIgnoreCase("HighRes") || p.getType().equalsIgnoreCase("LargePhoto") || p.getType().equalsIgnoreCase("DoNotCheck"))) {
                // We already have one or more HighRes/LargePhoto, or we have a DoNotCheck
                it.remove();
            }
        }

        for (NtreisProperty prop : properties) {
            this.retsFeedSynchronizationService.queueAllPhotos(new TaskAllPhotos(prop.getId().longValue()));
        }
    }
}
