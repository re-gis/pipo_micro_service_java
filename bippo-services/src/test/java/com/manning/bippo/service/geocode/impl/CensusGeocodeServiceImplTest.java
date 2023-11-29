package com.manning.bippo.service.geocode.impl;

import com.manning.bippo.commons.geocode.pojo.LatLongCoordinates;
import com.manning.bippo.service.geocode.pojo.GeocodeResponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class CensusGeocodeServiceImplTest
{

    @Test
    public void simpleTest()
    {
        CensusGeocodeServiceImpl geocodeService = new CensusGeocodeServiceImpl();
        GeocodeResponse geocodeResponse = geocodeService.geocodeOneLineAddress("3010 UPLAND DR,MANSFIELD,TX 76063");
        assertNotNull(geocodeResponse);
    }

    @Test
    public void batchDownloadTest()
    {
        CensusGeocodeServiceImpl geocodeService = new CensusGeocodeServiceImpl();
        Map<Long, String> addresses= new HashMap<>();
        addresses.put(1l, "1005 Boyd Creek Road, McKinney, TX, 75071");

        Map<Long, LatLongCoordinates> geocodeResponses = geocodeService.batchGeocoding(addresses);
        Assert.assertNotNull(geocodeResponses);

    }

    @Test
    public void simpleTestWithGoogle()
    {
        GoogleGeocodeServiceImpl geocodeService = new GoogleGeocodeServiceImpl();
        GeocodeResponse geocodeResponse = geocodeService.geocodeOneLineAddress("2007 Carroll Avenue, Dallas, Texas, 75204");
        assertNotNull(geocodeResponse);
    }
}