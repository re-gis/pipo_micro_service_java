package com.manning.bippo.redis.dao;

import com.manning.bippo.redis.pojo.RedisProperty;
import java.util.Date;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RedisPropertyRepository extends CrudRepository<RedisProperty, Long>, AbstractRedisRepository
{
    @Override
    List<RedisProperty> findByMlsStatusAndLocationNear(String status, Point location, Distance distance);

    @Override
    List<RedisProperty> findByMlsStatusAndPropertySubTypeAndLocationNear(String status, String propertySubType,
                                                                         Point location, Distance radius);

    @Override
    List<RedisProperty> findByLocationNear(Point location, Distance radius);

    @Override
    void deleteByStatusChangeTimestampBefore(Date beforeDate);
}
