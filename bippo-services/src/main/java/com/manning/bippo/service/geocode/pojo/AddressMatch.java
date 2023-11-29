package com.manning.bippo.service.geocode.pojo;

import com.manning.bippo.commons.geocode.pojo.LatLongCoordinates;

public class AddressMatch
{
    LatLongCoordinates coordinates;

    public String toString()
    {
        return String.valueOf(this.coordinates);
    }

    public LatLongCoordinates getCoordinates()
    {
        return coordinates;
    }

    public void setCoordinates(LatLongCoordinates coordinates)
    {
        this.coordinates = coordinates;
    }
}
