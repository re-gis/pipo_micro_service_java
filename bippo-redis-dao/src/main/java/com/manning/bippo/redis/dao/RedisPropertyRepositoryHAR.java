package com.manning.bippo.redis.dao;

import com.manning.bippo.redis.pojo.RedisPropertyHAR;
import java.util.Date;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RedisPropertyRepositoryHAR extends CrudRepository<RedisPropertyHAR, Long>, AbstractRedisRepository
{
    @Override
    List<RedisPropertyHAR> findByMlsStatusAndLocationNear(String status, Point location, Distance distance);

    @Override
    List<RedisPropertyHAR> findByMlsStatusAndPropertySubTypeAndLocationNear(String status, String propertySubType,
                                                                         Point location, Distance radius);

    @Override
    List<RedisPropertyHAR> findByLocationNear(Point location, Distance radius);

    @Override
    void deleteByStatusChangeTimestampBefore(Date beforeDate);
}
