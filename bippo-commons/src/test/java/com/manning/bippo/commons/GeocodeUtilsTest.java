package com.manning.bippo.commons;

import org.junit.Test;

public class GeocodeUtilsTest
{
    @Test
    public void simpleTest()
    {
        GeocodeBox boxCorner = GeocodeUtils.getBoxCorner(32.802895, -96.694145, 1d);

//        Assert.assertEquals(boxCorner.northWestLatitude, Double.valueOf(32.4847));
        System.out.println(boxCorner.northEastLatitude + "," + boxCorner.northEastLongitude);
        System.out.println(boxCorner.northWestLatitude + "," + boxCorner.northWestLongitude);
        System.out.println(boxCorner.southWestLatitude + "," + boxCorner.southWestLongitude);
        System.out.println(boxCorner.southEastLatitude + "," + boxCorner.southEastLongitude);
    }
}