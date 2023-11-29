package com.manning.bippo.commons.geocode.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LatLongCoordinates
{
    @JsonProperty("y")
    Double latitude;

    @JsonProperty("x")
    Double longitude;

    public LatLongCoordinates() {}

    public LatLongCoordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String toString()
    {
        return String.format("(%s, %s)", this.latitude, this.longitude);
    }

    public Double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(Double latitude)
    {
        this.latitude = latitude;
    }

    public Double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(Double longitude)
    {
        this.longitude = longitude;
    }
}
