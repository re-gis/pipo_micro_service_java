package com.manning.bippo.commons.dto;

import java.io.Serializable;

public class DownloadMultiplePropertyMLS implements Serializable
{
    public String matrixUniqueId;
    public int mlsNumber;
    public boolean indexSemantics;

    public DownloadMultiplePropertyMLS(String matrixUniqueId, boolean indexSemantics)
    {
        this.matrixUniqueId = matrixUniqueId;
        this.indexSemantics = indexSemantics;
        this.mlsNumber = -1;
    }

    public DownloadMultiplePropertyMLS(int mlsNumber)
    {
        this.mlsNumber = mlsNumber;
        this.matrixUniqueId = null;
        this.indexSemantics = true;
    }
}
