package com.manning.bippo.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class DownloadRetsComps implements Serializable
{
    private final List<Long> propertyIds;

    private final Boolean forceRun;

}
