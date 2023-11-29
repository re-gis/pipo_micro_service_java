package com.manning.bippo.commons.dto;

import java.io.Serializable;
import java.util.List;

public class UpdateRedisCache implements Serializable
{
    public List<Long> propertyIds;

    public UpdateRedisCache(List<Long> propertyIds)
    {
        this.propertyIds = propertyIds;
    }
}
