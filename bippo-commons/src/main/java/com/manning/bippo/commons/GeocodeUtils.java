package com.manning.bippo.commons;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

public class GeocodeUtils
{

    public static GeocodeBox getBoxCorner(Double latitude, Double longitude, Double proximityInMiles)
    {
        GeocodeBox geocodeBox = new GeocodeBox();

        LatLng travelNorthWest = LatLngTool.travel(new LatLng(latitude, longitude), LatLngTool.Bearing.NORTH_WEST, proximityInMiles, LengthUnit.MILE);
        geocodeBox.northWestLongitude = travelNorthWest.getLongitude();
        geocodeBox.northWestLatitude = travelNorthWest.getLatitude();

        LatLng travelNorthEast = LatLngTool.travel(new LatLng(latitude, longitude), LatLngTool.Bearing.NORTH_EAST, proximityInMiles, LengthUnit.MILE);
        geocodeBox.northEastLatitude = travelNorthEast.getLatitude();
        geocodeBox.northEastLongitude = travelNorthEast.getLongitude();

        LatLng travelSouthEast = LatLngTool.travel(new LatLng(latitude, longitude), LatLngTool.Bearing.SOUTH_EAST, proximityInMiles, LengthUnit.MILE);
        geocodeBox.southEastLatitude = travelSouthEast.getLatitude();
        geocodeBox.southEastLongitude = travelSouthEast.getLongitude();

        LatLng travelSouthWest = LatLngTool.travel(new LatLng(latitude, longitude), LatLngTool.Bearing.SOUTH_WEST, proximityInMiles, LengthUnit.MILE);
        geocodeBox.southWestLatitude = travelSouthWest.getLatitude();
        geocodeBox.southWestLongitude = travelSouthWest.getLongitude();

        return geocodeBox;

    }
}
