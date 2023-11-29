package com.manning.bippo.dao.converter;

import com.manning.bippo.dao.pojo.AuctionProperty;

public class TaxDataConverter extends BaseJsonConverter<AuctionProperty.TaxData> {

    public TaxDataConverter() {
        super(AuctionProperty.TaxData.class);
    }
}
