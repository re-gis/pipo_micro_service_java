package com.manning.bippo.dao.rets.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RetsResource implements Serializable
{
    private String name;
    private List<RetsLookup> lookups;

    public RetsResource()
    {
    }

    public RetsResource(String name)
    {
        this.name = name;
        this.lookups = new ArrayList<>();
    }

    public List<RetsLookup> getLookups()
    {
        return lookups;
    }

    public void setLookups(List<RetsLookup> lookups)
    {
        this.lookups = lookups;
    }
}
