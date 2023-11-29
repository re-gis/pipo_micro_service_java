package com.manning.bippo.commons.dto;

import java.io.Serializable;
import java.util.List;

public class DownloadMultiplePropertyLatLong implements Serializable
{
    public List<Long> propertyIds;

    public DownloadMultiplePropertyLatLong(List<Long> propertyIds)
    {
        this.propertyIds = propertyIds;
    }
}
