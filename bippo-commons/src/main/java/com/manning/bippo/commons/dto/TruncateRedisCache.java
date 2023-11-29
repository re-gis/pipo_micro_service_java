package com.manning.bippo.commons.dto;

import java.io.Serializable;

public class TruncateRedisCache implements Serializable
{

    public String region;

    public TruncateRedisCache(String region) {
        this.region = region;
    }
}
