package com.manning.bippo.service.trestle;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.dao.AddressSemanticsRepository;
import com.manning.bippo.dao.TrestleHARRepository;
import com.manning.bippo.dao.itf.TrestleProperty;
import com.manning.bippo.dao.pojo.AddressSemantics;
import com.manning.bippo.service.address.standardize.AddressStandardizationService;
import com.manning.bippo.service.address.verifiy.AddressVerificationResponse;
import com.manning.bippo.service.address_semanticize.AddressSemanticizationService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrestleDataService {

    @Autowired
    TrestleHARRepository trestleHARRepository;

    @Autowired
    AddressStandardizationService addressStandardizationService;

    @Autowired
    AddressSemanticizationService addressSemanticizationService;

    @Autowired
    AddressSemanticsRepository addressSemanticsRepository;

    public AddressSemantics findAddressSemanticsForTrestleProperty(TrestleProperty trestleProperty) {
        AddressSemantics as = this.addressSemanticizationService.findTopByTrestleProperty(trestleProperty);

        if (as == null) {
            final AddressVerificationResponse standardizedAvr = this.addressStandardizationService.standardizeAbstractProperty(trestleProperty);

            if (standardizedAvr != null) {
                // We were able to manually semanticize this address
                as = this.addressSemanticizationService.forceSemanticize(standardizedAvr, null);
            } else try {
                // Fallback to attempting to use SmartyStreets instead
                as = this.addressSemanticizationService.semanticize(trestleProperty.getAddress(), null);
            } catch (Exception e) {
                LogUtil.error("Error semanticizing Trestle property", e);
                return null;
            }

            as.setTrestleSystem(trestleProperty.getOriginatingSystemName());
            as.setTrestleId(trestleProperty.getId());
            as = this.addressSemanticsRepository.save(as);
        }

        return as;
    }

    public TrestleProperty findByRegionAndListingKey(String region, int listingKey) {
        switch (region) {
            case "HAR":
                return this.trestleHARRepository.findByListingKeyNumeric(listingKey);
            default:
                LogUtil.info("Unrecognized Trestle region " + region);
                return null;
        }
    }

    public TrestleProperty findByRegionAndId(String region, Long id) {
        switch (region) {
            case "HAR":
                return this.trestleHARRepository.findOne(id);
            default:
                LogUtil.info("Unrecognized Trestle region " + region);
                return null;
        }
    }

    public List<? extends TrestleProperty> findByRegionAndIds(String region, List<Long> ids) {
        switch (region) {
            case "HAR":
                return this.trestleHARRepository.findAll(ids);
            default:
                LogUtil.info("Unrecognized Trestle region " + region);
                return null;
        }
    }

    public List<? extends TrestleProperty> findByRegionAndStatusChangeTimestampBetween(String region, Date beg, Date end) {
        switch (region) {
            case "HAR":
                return this.trestleHARRepository.findByStatusChangeTimestampBetween(beg, end);
            default:
                LogUtil.info("Unrecognized Trestle region " + region);
                return null;
        }
    }
}
