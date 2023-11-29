package com.manning.bippo.service.address_semanticize;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.dao.AddressSemanticsRepository;
import com.manning.bippo.dao.AuctionPropertyRepository;
import com.manning.bippo.dao.NtreisPropertyRepository;
import com.manning.bippo.dao.OnBoardInformaticsResponseRepository;
import com.manning.bippo.dao.itf.Addressable;
import com.manning.bippo.dao.itf.TrestleProperty;
import com.manning.bippo.dao.pojo.AddressSemantics;
import com.manning.bippo.dao.pojo.AuctionProperty;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.dao.pojo.OnBoardInformaticsResponse;
import com.manning.bippo.service.address.verifiy.AddressVerificationResponse;
import com.manning.bippo.service.address_semanticize.pojo.SemanticsIds;
import com.manning.bippo.service.oi.OnBoardInformaticsPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import com.manning.bippo.service.address.verifiy.AddressVerificationService;
import com.manning.bippo.service.address.verifiy.pojo.GoogleAddressVerificationResponse;
import com.manning.bippo.service.geocode.RooftopLatLongService;
import com.manning.bippo.service.oi.pojo.AllEventsPropertyResponse;
import com.manning.bippo.service.profiling.ProfilingMetricsService;
import java.util.NoSuchElementException;

@Service
public class AddressSemanticizationServiceImpl implements AddressSemanticizationService
{
    @Autowired
    AddressVerificationService addressVerificationService;

    @Autowired
    AddressSemanticsRepository addressSemanticsRepository;

    @Autowired
    ProfilingMetricsService profilingMetricsService;

    @Autowired
    OnBoardInformaticsPropertyService onBoardInformaticsPropertyService;

    @Override
    public AddressSemantics semanticize(String fullAddress, SemanticsIds ids) throws IOException
    {
        return this.saveAddressSemantics(ids, this.addressVerificationService.query(fullAddress));
    }

    @Override
    public AddressSemantics semanticize(Addressable addressable, SemanticsIds ids) throws IOException
    {
        return this.saveAddressSemantics(ids, this.addressVerificationService.query(addressable));
    }

    @Override
    public AddressSemantics forceSemanticize(AddressVerificationResponse avr, SemanticsIds ids)
    {
        return this.saveAddressSemantics(ids, avr);
    }

    @Autowired
    OnBoardInformaticsResponseRepository onBoardInformaticsResponseRepository;

    @Autowired
    NtreisPropertyRepository ntreisPropertyRepository;

    @Autowired
    AuctionPropertyRepository auctionPropertyRepository;

    @Autowired
    RooftopLatLongService rooftopLatLongService;

    @Override
    public AddressSemantics saveAddressSemantics(SemanticsIds ids, AddressVerificationResponse av)
    {
//        SmartyStreetsComponents components = smartStreetsStreetAddressResponse.getComponents();
        AddressSemantics addressSemantics = addressSemanticsRepository.findTopByFirstLineAndLastLine(
                av.getFirstLine(), av.getLastLine(), new Sort(Sort.Direction.DESC, "id"));

        if (addressSemantics == null)
        {
            addressSemantics = new AddressSemantics();

            addressSemantics.setFirstLine(av.getFirstLine());
            addressSemantics.setLastLine(av.getLastLine());

            if (av.hasAddressParts())
            {
                addressSemantics.setPrimaryNumber(av.getStreetNumber());
                addressSemantics.setStreetName(av.getStreetName());
                addressSemantics.setStreetSuffix(av.getStreetSuffix());
                addressSemantics.setCityName(av.getCity());
                addressSemantics.setState(av.getState());
                addressSemantics.setZipcode(av.getZipCode());
            }
        }

        if (ids != null)
        {
            if (ids.taxId != null)
                addressSemantics.setTaxId(onBoardInformaticsResponseRepository.findOne(ids.taxId));
            if (ids.ntreisId != null)
                addressSemantics.setNtreisProperty(ntreisPropertyRepository.findOne(ids.ntreisId));
            if (ids.auctionId != null)
                addressSemantics.setAuctionProperty(auctionPropertyRepository.findOne(ids.auctionId));
        }

        final AddressSemantics saved = addressSemanticsRepository.save(addressSemantics);

        if (av instanceof GoogleAddressVerificationResponse) {
            // If we have a Google AVR, take the opportunity to cache the Lat/Long
            this.rooftopLatLongService.updateFromAddressVerification(saved, av);
        }

        return saved;
    }

    @Override
    public AddressSemantics indexIfAbsent(AddressVerificationResponse avr) {
        final AddressSemantics as = this.addressSemanticsRepository.findTopByFirstLineAndLastLine(avr.getFirstLine(), avr.getLastLine(), new Sort(Sort.Direction.DESC, "id"));

        if (as != null) {
            if (as.getTaxId() == null || as.getTaxId().getResponse() == null) {
                // Attempt to re-request the tax data if it was missing
                final OnBoardInformaticsResponse newTaxData = onBoardInformaticsPropertyService.getOnBoardInformaticsResponse(avr.getFirstLine(), avr.getLastLine());

                if (newTaxData != null) {
                    LogUtil.info("indexIfAbsent: updating missing tax in existing AddressSemantics w/ tax " + newTaxData.getId());
                    as.setTaxId(newTaxData);
                    return this.addressSemanticsRepository.save(as);
                }

                LogUtil.info("indexIfAbsent: existing AddressSemantics missing tax data and could not be repaired for {}, {}", avr.getFirstLine(), avr.getLastLine());
            }

            return as;
        }

        // We have [what we are being told is] a valid address, so we can request tax data to link this address with when we create its AddressSemantics index
        final OnBoardInformaticsResponse newTaxData = onBoardInformaticsPropertyService.getOnBoardInformaticsResponse(avr.getFirstLine(), avr.getLastLine());

        if (newTaxData != null) {
            LogUtil.info("indexIfAbsent: indexing lone AddressVerificationResponse w/ tax " + newTaxData.getId());
            return this.saveAddressSemantics(SemanticsIds.builder().taxId(newTaxData.getId()).build(), avr);
        } else {
            LogUtil.info("indexIfAbsent: indexing lone AddressVerificationResponse, tax data failed for {}, {}", avr.getFirstLine(), avr.getLastLine());
            return this.saveAddressSemantics(null, avr);
        }
    }

    @Override
    public AddressSemantics indexIfAbsent(NtreisProperty ntreisProperty) {
        final AddressSemantics as = this.addressSemanticsRepository.findTopByNtreisProperty(ntreisProperty);

        if (as != null) {
            return as;
        }

        LogUtil.info("indexIfAbsent: indexing NtreisProperty: " + ntreisProperty.getAddress());
        this.profilingMetricsService.incrementCounter("ss_AddressSemanticizationServiceImpl_indexIfAbsent");

        // Link with NtreisProperty data
        try {
            return this.semanticize(ntreisProperty, SemanticsIds.builder().ntreisId(ntreisProperty.getId()).build());
        } catch (Exception e) {
            LogUtil.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public AddressSemantics findOne(Long id)
    {
        AddressSemantics addressSemantics = addressSemanticsRepository.findOne(id);

        if (addressSemantics.getTaxId() == null)
        {
            String apn = null, fips = null;

            if (addressSemantics.getNtreisProperty() != null && addressSemantics.getNtreisProperty().getParcelNumber() != null)
            {
                // TODO: Get FIPS from MLS data (?)
                // APN given by MLS data
                apn = addressSemantics.getNtreisProperty().getParcelNumber().trim();
            } /*else if (addressSemantics.getAuctionProperty() != null && addressSemantics.getAuctionProperty().getApn() != null)
            {
                // APN given by an auction entry
                apn = addressSemantics.getAuctionProperty().getApn().trim();
            }*/

            OnBoardInformaticsResponse onBoardInformaticsResponse;

            if (apn != null && fips != null)
            {
                try {
                    // Use the resolved APN to avoid possible near misses with addresses
                    onBoardInformaticsResponse = onBoardInformaticsPropertyService.getOnBoardInformaticsResponse(apn, fips,
                            addressSemantics.getPrimaryNumber(), addressSemantics.getStreetName(), addressSemantics.getCityName());
                } catch (NoSuchElementException e) {
                    // The APN search can sometimes fail to find the intended property in its results
                    // In those cases, we can at least try to fall back to an address-based search
                    onBoardInformaticsResponse = onBoardInformaticsPropertyService.getOnBoardInformaticsResponse(addressSemantics.getFirstLine(), addressSemantics.getLastLine());
                }
            } else
            {
                // Note: This is prone to near misses, where the address is different enough that onboard cannot resolve it
                onBoardInformaticsResponse = onBoardInformaticsPropertyService.getOnBoardInformaticsResponse(addressSemantics.getFirstLine(), addressSemantics.getLastLine());
            }

            addressSemantics.setTaxId(onBoardInformaticsResponse);

            return addressSemanticsRepository.save(addressSemantics);
        }
        return addressSemantics;
    }

    @Override
    public AllEventsPropertyResponse getTaxData(AddressSemantics property) {
        OnBoardInformaticsResponse ob = property.getTaxId();

        if (ob == null) {
            ob = this.onBoardInformaticsPropertyService.downloadAllEventsPropertyData(property);
            property.setTaxId(ob);
            this.addressSemanticsRepository.save(property);
        }

        return this.onBoardInformaticsPropertyService.convertJSONToAllEventsResponse(ob.getResponse());
    }

    @Override
    public AddressSemantics findTopByNtreisProperty(NtreisProperty ntreisProperty)
    {
        return addressSemanticsRepository.findTopByNtreisProperty(ntreisProperty);
    }

    @Override
    public AddressSemantics findByAuctionProperty(AuctionProperty auctionProperty)
    {
        return addressSemanticsRepository.findByAuctionProperty(auctionProperty);
    }

    @Override
    public AddressSemantics findTopByTrestleProperty(TrestleProperty trestleProperty)
    {
        return addressSemanticsRepository.findTopByTrestleSystemAndTrestleId(
                trestleProperty.getOriginatingSystemName(), trestleProperty.getId());
    }
}
