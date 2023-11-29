package com.manning.bippo.commons.dto;

import java.io.Serializable;

public class DownloadOnBoardInformaticsResponse implements Serializable
{
    public Long ntreisId;

    public DownloadOnBoardInformaticsResponse(Long ntreisId)
    {
        this.ntreisId = ntreisId;
    }
}
