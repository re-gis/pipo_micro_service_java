package com.manning.bippo.dao.converter;

import com.manning.bippo.dao.pojo.AuctionProperty;

public class LiensDataConverter extends BaseJsonConverter<AuctionProperty.LiensData> {

    public LiensDataConverter() {
        super(AuctionProperty.LiensData.class);
    }
}
