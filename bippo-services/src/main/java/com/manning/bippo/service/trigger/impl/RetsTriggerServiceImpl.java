package com.manning.bippo.service.trigger.impl;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.dao.pojo.TaxProperty;
import com.manning.bippo.service.address.verifiy.AddressVerificationResponse;
import com.manning.bippo.service.mapping.RetsBippoMappingService;
import com.manning.bippo.service.trigger.RetsTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import com.manning.bippo.service.address.verifiy.AddressVerificationService;

@Service
public class RetsTriggerServiceImpl extends AbstractTriggerService implements RetsTriggerService
{
    public static final int RETURN_COUNT_SIZE = 100;

    @Autowired
    RetsBippoMappingService retsBippoMappingService;

    @Autowired
    AddressVerificationService addressVerificationService;

    @Override
    public String getProcessName()
    {
        return "RETS_FEED";
    }

    @Override
    @Async
    public void trigger()
    {
        long taxPropertyCounty = taxPropertyRepository.count();
        int pageCount = Double.valueOf(Math.floor(taxPropertyCounty / RETURN_COUNT_SIZE)).intValue();

        int skipPageNumber = getSkipPageNumber();
        LogUtil.info("Skipping to page: {} of page size: {}.", skipPageNumber, RETURN_COUNT_SIZE);

        /*
         * FIXME The AbstractAddressVerificationService backed by onboard data
         * cannot supply the address components required for this code.
         */
//        for (int pageNumber = skipPageNumber; pageNumber <= pageCount; pageNumber++)
//        {
//            Page<TaxProperty> all = taxPropertyRepository.findAll(new PageRequest(pageNumber, RETURN_COUNT_SIZE));
//
//            List<String> addresses = all.getContent().parallelStream().map(TaxProperty::getFullAddress).collect(Collectors.toList());
//            boolean missingComponents = false;
//
//            try
//            {
//                List<? extends AddressVerificationResponse> responses = addressVerificationService.getSmartStreetsStreetAddressResponse(addresses);
//
//                if (!responses.stream().allMatch(AddressVerificationResponse::hasAddressParts)) {
//                    missingComponents = true;
//                }
//
//                for (AddressVerificationResponse resp : responses) {
//                    List<NtreisProperty> mlsRecords = retsBippoMappingService.importMLSRecord(resp.getFirstLine(), resp.getLastLine(),
//                            resp.getStreetNumber(), resp.getStreetName(), resp.getStreetSuffix(), resp.getCity(), resp.getZipCode());
//                    LogUtil.info("Added {} records for address {}", mlsRecords.size(), resp.getFirstLine() + ", " + resp.getLastLine());
//                }
//            } catch (Exception e)
//            {
//                LogUtil.error(e.getMessage(), e);
//            } finally
//            {
//                if (!missingComponents) {
//                    updateProcessStatusIdSkip(all);
//                }
//            }
//        }
    }
}


