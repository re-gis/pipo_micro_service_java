package com.manning.bippo.redis.pojo;

import java.util.Date;
import org.springframework.data.geo.Point;

public interface AbstractRedisProperty {

    public Long getId();
    public String getMlsStatus();
    public String getPropertySubType();
    public Point getLocation();
    public Float getSqftTotal();
    public Integer getYearBuilt();
    public Integer getStoryCount();
    public Boolean getPoolState();
    public Date getStatusChangeTimestamp();
}
