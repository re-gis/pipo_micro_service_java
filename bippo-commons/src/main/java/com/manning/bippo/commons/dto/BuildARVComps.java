package com.manning.bippo.commons.dto;

import java.io.Serializable;
import java.util.List;

public class BuildARVComps  implements Serializable
{

    private final List<Long> propertyIds;

    public BuildARVComps(List<Long> propertyIds)
    {
        this.propertyIds = propertyIds;
    }

    public List<Long> getPropertyIds()
    {
        return propertyIds;
    }
}