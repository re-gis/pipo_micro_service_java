package com.manning.bippo.dao.rets.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RetsLookup implements Serializable
{
    private String name;
    private List<RetsLookupType> lookupTypes;

    public RetsLookup()
    {
    }

    public RetsLookup(String name)
    {
        this.name = name;
        this.lookupTypes = new ArrayList<>();
    }

    public String getName()
    {
        return name;
    }

    public List<RetsLookupType> getLookupTypes()
    {
        return lookupTypes;
    }

    public void setLookupTypes(List<RetsLookupType> lookupTypes)
    {
        this.lookupTypes = lookupTypes;
    }
}
