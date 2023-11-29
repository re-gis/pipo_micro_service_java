package com.manning.bippo.commons.dto;

import java.io.Serializable;
import java.util.List;

public class DownloadMultiplePropertyFrontPhotos implements Serializable
{
    public List<Long> propertyIds;

    public DownloadMultiplePropertyFrontPhotos(List<Long> propertyIds)
    {
        this.propertyIds = propertyIds;
    }
}
