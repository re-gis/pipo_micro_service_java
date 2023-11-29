package com.manning.bippo.commons.dto;

import java.io.Serializable;
import java.util.List;

public class DownloadMultiplePropertyPhotos implements Serializable
{
    public List<Long> propertyIds;

    public DownloadMultiplePropertyPhotos(List<Long> propertyIds)
    {
        this.propertyIds = propertyIds;
    }


}
