package com.manning.bippo.commons.dto;

import java.io.Serializable;
import java.util.Date;

public class TruncateRedisCacheByDate implements Serializable
{

    public String region;
    public Date dateBefore;

    public TruncateRedisCacheByDate(String region, Date dateBefore) {
        this.region = region;
        this.dateBefore = dateBefore;
    }
}
