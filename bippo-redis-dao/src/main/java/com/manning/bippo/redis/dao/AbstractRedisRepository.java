package com.manning.bippo.redis.dao;

import com.manning.bippo.redis.pojo.AbstractRedisProperty;
import java.util.Date;
import java.util.List;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;

public interface AbstractRedisRepository {

    List<? extends AbstractRedisProperty> findByMlsStatusAndLocationNear(String status, Point location, Distance distance);

    List<? extends AbstractRedisProperty> findByMlsStatusAndPropertySubTypeAndLocationNear(String status, String propertySubType,
                                                                         Point location, Distance radius);

    List<? extends AbstractRedisProperty> findByLocationNear(Point location, Distance radius);

    void deleteByStatusChangeTimestampBefore(Date beforeDate);
}
