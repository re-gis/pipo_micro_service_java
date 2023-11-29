package com.manning.bippo.service.area;

import com.manning.bippo.commons.dto.BasicPropertyDetails;
import com.manning.bippo.service.rets.pojo.CompsResponse;

import java.util.List;

public interface AreaListingsService
{
//    CompsResponse getAreaListings(NtreisProperty ntreisProperty, Float distance);

    CompsResponse getAreaListings(BasicPropertyDetails propertyDetails, Float radius);
}
