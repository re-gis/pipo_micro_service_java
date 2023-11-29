package com.manning.bippo.service.address_semanticize;

import com.manning.bippo.dao.itf.Addressable;
import com.manning.bippo.dao.itf.TrestleProperty;
import com.manning.bippo.dao.pojo.AddressSemantics;
import com.manning.bippo.dao.pojo.AuctionProperty;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.service.address.verifiy.AddressVerificationResponse;
import com.manning.bippo.service.address_semanticize.pojo.SemanticsIds;
import com.manning.bippo.service.oi.pojo.AllEventsPropertyResponse;

import java.io.IOException;

public interface AddressSemanticizationService
{
    AddressSemantics semanticize(String fullAddress, SemanticsIds ids) throws IOException;

    AddressSemantics semanticize(Addressable addressable, SemanticsIds ids) throws IOException;

    AddressSemantics forceSemanticize(AddressVerificationResponse avr, SemanticsIds ids);

    AddressSemantics saveAddressSemantics(SemanticsIds ids, AddressVerificationResponse addressVerificationResponse);

    AddressSemantics indexIfAbsent(AddressVerificationResponse addressVerificationResponse);

    AddressSemantics indexIfAbsent(NtreisProperty ntreisProperty);

    AllEventsPropertyResponse getTaxData(AddressSemantics property);

    AddressSemantics findOne(Long id);

    AddressSemantics findTopByNtreisProperty(NtreisProperty ntreisProperty);

    AddressSemantics findByAuctionProperty(AuctionProperty auctionProperty);

    AddressSemantics findTopByTrestleProperty(TrestleProperty trestleProperty);
}
