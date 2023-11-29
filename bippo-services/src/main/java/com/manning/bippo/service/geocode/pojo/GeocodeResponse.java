package com.manning.bippo.service.geocode.pojo;

import com.manning.bippo.commons.geocode.pojo.LatLongCoordinates;
import java.util.NoSuchElementException;

public class GeocodeResponse
{
    Result result;

    public String toString()
    {
        return String.valueOf(this.result);
    }

    public Result getResult()
    {
        return result;
    }

    public void setResult(Result result)
    {
        this.result = result;
    }

    public LatLongCoordinates requireSingle() throws NoSuchElementException, IllegalStateException
    {
        if (this.result == null)
        {
            throw new NoSuchElementException("No Result present");
        }

        return this.result.requireSingle();
    }

    public LatLongCoordinates requireAny() throws NoSuchElementException
    {
        if (this.result == null)
        {
            throw new NoSuchElementException("No Result present");
        }

        return this.result.requireAny();
    }
}
