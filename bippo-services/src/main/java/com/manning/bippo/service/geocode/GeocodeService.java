package com.manning.bippo.service.geocode;

import com.manning.bippo.service.geocode.pojo.GeocodeResponse;

public interface GeocodeService
{
    public GeocodeResponse geocodeOneLineAddress(String address);
}
