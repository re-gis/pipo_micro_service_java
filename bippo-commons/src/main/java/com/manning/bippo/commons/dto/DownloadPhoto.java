package com.manning.bippo.commons.dto;

import java.io.Serializable;

public class DownloadPhoto implements Serializable
{
    public Long ntreisPropertyId;

    public DownloadPhoto(Long ntreisPropertyId)
    {
        this.ntreisPropertyId = ntreisPropertyId;
    }
}
