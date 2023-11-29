package com.manning.bippo.commons.dto;

import java.io.Serializable;
import java.util.List;

public class UpdateRedisCacheTrestle implements Serializable
{
    public String region;
    public List<Long> propertyIds;

    public UpdateRedisCacheTrestle(String region, List<Long> propertyIds)
    {
        this.region = region;
        this.propertyIds = propertyIds;
    }
}
