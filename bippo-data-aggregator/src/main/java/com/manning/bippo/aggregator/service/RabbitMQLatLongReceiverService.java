package com.manning.bippo.aggregator.service;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.commons.dto.DownloadMultiplePropertyLatLong;
import com.manning.bippo.dao.NtreisPropertyRepository;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.service.address_semanticize.AddressSemanticizationService;
import com.manning.bippo.service.queuing.impl.RabbitMQSenderService;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.manning.bippo.service.address.verifiy.AddressVerificationService;
import com.manning.bippo.service.address.verifiy.pojo.GoogleAddressVerificationResponse;
import com.manning.bippo.service.address_semanticize.pojo.SemanticsIds;
import com.manning.bippo.service.geocode.impl.GoogleGeocodeServiceImpl;
import java.util.ArrayList;

@Service
@RabbitListener(queues = "bippo-dag-lat-lang-queue")
public class RabbitMQLatLongReceiverService
{
//    @Autowired
//    CensusGeocodeServiceImpl censusGeocodeService;

    @Autowired
    GoogleGeocodeServiceImpl googleGeocodeService;

    @Autowired
    NtreisPropertyRepository ntreisPropertyRepository;

    @Autowired
    AddressVerificationService addressVerificationService;

    @Autowired
    AddressSemanticizationService addressSemanticizationService;

    @Autowired
    RabbitMQSenderService rabbitMQSenderService;

    @Bean
    public Queue bippoDagLatLongQueue()
    {
        return new Queue("bippo-dag-lat-lang-queue");
    }

    PassiveExpiringMap messagesPreviouslyReceived = new PassiveExpiringMap(new PassiveExpiringMap.ConstantTimeToLiveExpirationPolicy(60 * 1000));

    @RabbitHandler
    public void process(@Payload DownloadMultiplePropertyLatLong multiplePropertyLatLong, @Headers Map<String, Object> headers)
    {
        try
        {
            List<NtreisProperty> ntreisProperties = ntreisPropertyRepository.findAll(multiplePropertyLatLong.propertyIds);
            long sum = multiplePropertyLatLong.propertyIds.stream().mapToLong(Long::longValue).sum();

            if (messagesPreviouslyReceived.get(sum) != null)
            {
                LogUtil.debug("Message already received. Ignoring...");
                return;
            } else
            {
                messagesPreviouslyReceived.put(sum, multiplePropertyLatLong);
            }

            Map<Long, String> propertiesThatNeedLatLong = ntreisProperties.stream()
                    .filter(ntreisProperty -> ntreisProperty.getLongitude() == null || ntreisProperty.getLongitude() == null)
                    .collect(Collectors.toMap(NtreisProperty::getId, NtreisProperty::getAddress));

            List<Long> propertiesWithLatLong = ntreisProperties.stream()
                    .filter(p -> p.getLatitude() != null)
                    .map(NtreisProperty::getId).collect(Collectors.toList());

            if (propertiesThatNeedLatLong.size() > 0)
            {
                // FIXME Analyze heavy usage
//                Map<Long, NtreisProperty> properties = ntreisProperties.stream().collect(Collectors.toMap(NtreisProperty::getId, ntreisProperty -> ntreisProperty));
//
//                LogUtil.debug("Attempting to geocode {} NtreisProperties with missing lat/long ...", propertiesThatNeedLatLong.size());
//                final Map<Long, GoogleAddressVerificationResponse> longCoordinatesMap = this.googleGeocodeService.queryMultipleMapped(propertiesThatNeedLatLong);
//                final List<NtreisProperty> updatedNtreisProperties = new ArrayList<>();
//
//                longCoordinatesMap.forEach((k, v) -> {
//                    if (v.hasLatLong()) {
//                        NtreisProperty p = properties.get(k);
//                        p.setLatitude((float) v.getLatitude());
//                        p.setLongitude((float) v.getLongitude());
//                        updatedNtreisProperties.add(p);
//                        addressSemanticizationService.saveAddressSemantics(SemanticsIds.builder().ntreisId(k).build(), v);
//                    }
//                });
//
//                LogUtil.debug("Saving {} NtreisProperties and updating redis ...", updatedNtreisProperties.size());
//                ntreisPropertyRepository.save(updatedNtreisProperties);
//                rabbitMQSenderService.queueUpdateRedisCache(updatedNtreisProperties.stream().map(NtreisProperty::getId).collect(Collectors.toList()));
            }

            LogUtil.debug("Updating redis with {} NtreisProperties that already had lat/long ...", propertiesWithLatLong.size());
            rabbitMQSenderService.queueUpdateRedisCache(propertiesWithLatLong);
        } catch (Exception e)
        {
            LogUtil.error(e.getMessage(), e);
        }
    }
}
