package com.manning.bippo.dao.rets.pojo;

import java.io.Serializable;

public class RetsLookupType implements Serializable
{
    private String shortValue;
    private String longValue;

    public RetsLookupType()
    {
    }

    public RetsLookupType(String shortValue, String longValue)
    {
        this.shortValue = shortValue;
        this.longValue = longValue;
    }

    public String getShortValue()
    {
        return shortValue;
    }

    public String getLongValue()
    {
        return longValue;
    }
}
