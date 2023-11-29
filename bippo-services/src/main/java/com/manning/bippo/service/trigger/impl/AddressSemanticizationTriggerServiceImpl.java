package com.manning.bippo.service.trigger.impl;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.dao.pojo.TaxProperty;
import com.manning.bippo.service.address_semanticize.AddressSemanticizationService;
import com.manning.bippo.service.address_semanticize.pojo.SemanticsIds;
import com.manning.bippo.service.profiling.ProfilingMetricsService;
import com.manning.bippo.service.trigger.AddressSemanticizationTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AddressSemanticizationTriggerServiceImpl extends AbstractTriggerService implements AddressSemanticizationTriggerService
{

    @Autowired
    AddressSemanticizationService addressSemanticizationService;
    @Autowired
    ProfilingMetricsService profilingMetricsService;

    @Override
    public String getProcessName()
    {
        return "ADDRESS_SEMANTICIZE";
    }

    @Override
    @Async
    public void trigger()
    {
        long taxPropertyCounty = taxPropertyRepository.count();
        int pageCount = Double.valueOf(Math.floor(taxPropertyCounty / RETURN_COUNT_SIZE)).intValue();

        int skipPageNumber = getSkipPageNumber();

        for (int pageNumber = skipPageNumber; pageNumber <= pageCount; pageNumber++)
        {
            Page<TaxProperty> all = taxPropertyRepository.findAll(new PageRequest(pageNumber, RETURN_COUNT_SIZE));

            this.profilingMetricsService.incrementCounter("ss_AddressSemanticizationTriggerServiceImpl_trigger", all.getContent().size());
            all.getContent().parallelStream().forEach(property -> {
                try
                {
                    addressSemanticizationService.semanticize(property.getFullAddress(), SemanticsIds.builder().taxId(property.getId()).build());
                } catch (Exception e)
                {
                    LogUtil.error(e.getMessage(), e);
                }
            });

            updateProcessStatusIdSkip(all);

        }

    }


}
