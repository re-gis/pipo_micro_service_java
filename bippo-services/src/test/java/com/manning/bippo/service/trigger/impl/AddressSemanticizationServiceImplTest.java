package com.manning.bippo.service.trigger.impl;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.dao.AddressSemanticsRepository;
import com.manning.bippo.dao.pojo.AddressSemantics;
import com.manning.bippo.service.config.SampleServicesApplication;
import com.manning.bippo.service.oi.OnBoardInformaticsPropertyService;
import com.manning.bippo.service.oi.pojo.AllEventsPropertyResponse;
import com.manning.bippo.service.trigger.AddressSemanticizationTriggerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SampleServicesApplication.class)
public class AddressSemanticizationServiceImplTest
{
    @Autowired
    AddressSemanticizationTriggerService addressSemanticizationTriggerService;

    @Autowired
    AddressSemanticsRepository addressSemanticsRepository;

    @Test
    public void verifyTrigger()
    {
        addressSemanticizationTriggerService.trigger();
    }

    @Autowired
    OnBoardInformaticsPropertyService onBoardInformaticsPropertyService;

    @Test
    public void verifyTaxAddressMatch()
    {
        Page<AddressSemantics> allByTaxIdIsNotNull = addressSemanticsRepository.findAllByTaxIdIsNotNull(new PageRequest(1, 100));
        fix(allByTaxIdIsNotNull.getContent());

        for (int pageCount = 2; pageCount < allByTaxIdIsNotNull.getTotalPages(); pageCount++)
        {
            LogUtil.debug("On Page {}", pageCount);
            fix(addressSemanticsRepository.findAllByTaxIdIsNotNull(new PageRequest(pageCount, 100)).getContent());
        }
    }

    private void fix(List<AddressSemantics> allByTaxIdIsNotNull)
    {
        for (AddressSemantics addressSemantics : allByTaxIdIsNotNull)
        {
            String response = addressSemantics.getTaxId().getResponse();
            AllEventsPropertyResponse allEventsPropertyResponse = onBoardInformaticsPropertyService.convertJSONToAllEventsResponse(response);
            if (allEventsPropertyResponse.property != null && allEventsPropertyResponse.property.length > 0)
            {
                AllEventsPropertyResponse.Property property = allEventsPropertyResponse.property[0];
                LogUtil.debug("Comparing {}, {} to {}, {} ", property.address.line1, property.address.line2,
                        addressSemantics.getFirstLine(), addressSemantics.getLastLine());
                if (!property.address.line1.trim().toLowerCase().equals(addressSemantics.getFirstLine().trim().toLowerCase()))
                {
                     LogUtil.debug("********************* NOT THE SAME ADDRESS *********************");
                     addressSemantics.setTaxId(null);
                     addressSemanticsRepository.save(addressSemantics);
                }
            }
        }
    }

}