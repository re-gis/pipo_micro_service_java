package com.manning.bippo.service.geocode.pojo;

import com.manning.bippo.commons.geocode.pojo.LatLongCoordinates;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Result
{
    List<AddressMatch> addressMatches = new ArrayList<>();

    public String toString()
    {
        return String.valueOf(this.addressMatches);
    }

    public List<AddressMatch> getAddressMatches()
    {
        return addressMatches;
    }

    public void setAddressMatches(List<AddressMatch> addressMatches)
    {
        this.addressMatches = addressMatches;
    }

    public LatLongCoordinates requireSingle() throws NoSuchElementException, IllegalStateException
    {
        if (this.addressMatches.isEmpty())
        {
            throw new NoSuchElementException("No AddressMatches present");
        } else if (this.addressMatches.size() > 1)
        {
            throw new IllegalStateException("Too many AddressMatches present");
        }

        final LatLongCoordinates c = this.addressMatches.get(0).coordinates;

        if (c == null || c.getLatitude() == null || c.getLongitude() == null)
        {
            throw new IllegalStateException("The present AddressMatch is incomplete");
        }

        return c;
    }

    public LatLongCoordinates requireAny() throws NoSuchElementException
    {
        for (AddressMatch match : this.addressMatches)
        {
            if (match.coordinates == null || match.coordinates.getLatitude() == null || match.coordinates.getLongitude() == null) {
                continue;
            }

            return match.coordinates;
        }

        throw new NoSuchElementException("No AddressMatches present");
    }
}
