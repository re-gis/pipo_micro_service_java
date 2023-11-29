package com.manning.bippo.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class DownloadAreaComps implements Serializable
{
    private final BasicPropertyDetails subjectPropertyDetails;

    private Boolean forceRun = Boolean.FALSE;
}
