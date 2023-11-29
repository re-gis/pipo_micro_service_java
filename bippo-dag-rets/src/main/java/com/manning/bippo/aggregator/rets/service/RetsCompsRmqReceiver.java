package com.manning.bippo.aggregator.rets.service;

import com.manning.bippo.aggregator.rets.pojo.DownloadSubjectStatusComps;
import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.commons.dto.BasicPropertyDetails;
import com.manning.bippo.commons.dto.CompsFilterCalculated;
import com.manning.bippo.commons.dto.DownloadAreaComps;
import com.manning.bippo.dao.AddressSemanticsRepository;
import com.manning.bippo.dao.MLSStatusRepository;
import com.manning.bippo.dao.PropertyCompsFilterRepository;
import com.manning.bippo.dao.pojo.AddressSemantics;
import com.manning.bippo.dao.pojo.MLSStatus;
import com.manning.bippo.dao.pojo.PropertyCompsFilter;
import com.manning.bippo.service.mapping.RetsBippoMappingService;
import com.manning.bippo.service.rets.impl.CompsSearchFilterBuilderService;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = "bippo-dag-rets-comps")
public class RetsCompsRmqReceiver
{
    /**
     * The method name for bean that creates a queue should always be unique.
     */
    @Bean
    public Queue bippoDagRetsCompsQueue()
    {
        return new Queue("bippo-dag-rets-comps");
    }

    @Autowired
    AddressSemanticsRepository addressSemanticsRepository;

    @Autowired
    PropertyCompsFilterRepository propertyCompsFilterRepository;

    @Autowired
    CompsSearchFilterBuilderService compsSearchFilterBuilderService;

    @Autowired
    MLSStatusRepository mlsStatusRepository;

//    @Autowired
//    RetsBippoMappingService retsBippoMappingService;

    @Autowired
    RetsFeedSynchronizationService retsFeedSynchronizationService;

    @RabbitHandler
    public void process(@Payload DownloadAreaComps downloadAreaComps)
    {
        // FIXME Temporarily disabled
/*        try {
            BasicPropertyDetails subjectPropertyDetails = downloadAreaComps.getSubjectPropertyDetails();
            AddressSemantics addressSemantics = addressSemanticsRepository.findOne(subjectPropertyDetails.getAddressSemanticsId());
            PropertyCompsFilter propertyCompsFilter = propertyCompsFilterRepository.findTopBySubjectAddressSemanticsOrderByUpdated(addressSemantics);

            if (downloadAreaComps.getForceRun() || propertyCompsFilter == null || propertyCompsFilter.getAreaFilterUsed() != null) {
                final List<MLSStatus> validStatuses = mlsStatusRepository.findByInvalidOrderByValueAsc(false);
                validStatuses.add(mlsStatusRepository.findByValue("Sold"));
//                validStatuses.forEach(validStatus -> importAreaListingsForStatus(validStatus, subjectPropertyDetails));
                final DownloadSubjectStatusComps downloadReq = new DownloadSubjectStatusComps(subjectPropertyDetails,
                        validStatuses.stream().map(MLSStatus::getRetsShortValue).collect(Collectors.toList()));
            } else {
                LogUtil.debug("Skipping downloading AREA comps for semantics id {}", addressSemantics.getId());
            }
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
        }*/
    }

//    private void importAreaListingsForStatus(MLSStatus validStatus, BasicPropertyDetails subjectPropertyDetails) {
//        String[] propertySubTypes = {"RESCON", "RESRAN", "RESDUP", "RESFAM", "RESTOW"};
//        List<CompsFilterCalculated> compsFilterCalculatedList = compsSearchFilterBuilderService.buildCompsFilters("", subjectPropertyDetails.getLivingSize(),
//                subjectPropertyDetails.getYearBuiltEffective(), validStatus.getRetsShortValue(), Arrays.asList(propertySubTypes));
//
//        retsBippoMappingService.importComps(subjectPropertyDetails, compsFilterCalculatedList, "ALL");
//    }
}
