package com.manning.bippo.redis.dao;

import com.manning.bippo.redis.config.SampleDataRedisApplication;
import com.manning.bippo.redis.pojo.RedisProperty;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SampleDataRedisApplication.class)
public class RedisPropertyRepositoryTest
{

    @Autowired
    RedisPropertyRepository redisPropertyRepository;


    @Test
    public void verifyPropertyRepo()
    {
        assertNotNull(redisPropertyRepository);

        RedisProperty property = RedisProperty.builder()
                .mlsStatus("Sold")
                .location(new Point(-96.785630, 33.158080))
                .build();

        RedisProperty save = redisPropertyRepository.save(property);

        assertNotNull(save);
    }

}