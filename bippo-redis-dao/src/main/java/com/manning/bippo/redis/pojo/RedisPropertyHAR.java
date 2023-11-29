package com.manning.bippo.redis.pojo;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.GeoIndexed;

import java.util.Date;

@Builder
@Data
@RedisHash("property_har")
public class RedisPropertyHAR implements AbstractRedisProperty
{
    @Id
    private Long id;

    private String mlsStatus;

    private String propertySubType;

    @GeoIndexed
    private Point location;

    private Float sqftTotal;

    private Integer yearBuilt;

    private Integer storyCount;

    private Boolean poolState;

    private Date statusChangeTimestamp;
}
