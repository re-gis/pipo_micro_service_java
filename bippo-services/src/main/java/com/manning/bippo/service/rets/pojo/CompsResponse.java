package com.manning.bippo.service.rets.pojo;

import com.manning.bippo.commons.dto.CompsFilterCalculated;
import com.manning.bippo.dao.itf.AbstractProperty;
import com.manning.bippo.dao.pojo.NtreisProperty;
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

public class CompsResponse
{
    private CompsFilterCalculated compsFilterUsed;
    private List<? extends AbstractProperty> properties;
    private List<RetsResponse> retsResponses;
    private List<NtreisProperty> ntreisProperties;

    public CompsResponse(CompsFilterCalculated compsFilterUsed, List<? extends AbstractProperty> properties)
    {
        this.compsFilterUsed = compsFilterUsed;
        this.properties = properties;
        this.ntreisProperties = new ArrayList<>();

        for (AbstractProperty p : properties) {
            if (p instanceof NtreisProperty) {
                this.ntreisProperties.add((NtreisProperty) p);
            }
        }
    }

    public CompsFilterCalculated getCompsFilterUsed()
    {
        return compsFilterUsed;
    }

    public void setCompsFilterUsed(CompsFilterCalculated compsFilterUsed)
    {
        this.compsFilterUsed = compsFilterUsed;
    }

    public List<? extends AbstractProperty> getProperties()
    {
        return this.properties;
    }

    public void setProperties(List<? extends AbstractProperty> properties)
    {
        this.properties = properties;
    }

    @Deprecated
    public List<NtreisProperty> getNtreisProperties()
    {
        return this.ntreisProperties;
    }

    @Deprecated
    public void setNtreisProperties(List<NtreisProperty> ntreisProperties)
    {}

    public List<RetsResponse> getRetsResponses()
    {
        return retsResponses;
    }

    public void setRetsResponses(List<RetsResponse> retsResponses)
    {
        this.retsResponses = retsResponses;
    }
}
