package com.manning.bippo.aggregator.config;

import com.manning.bippo.commons.LogUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import redis.clients.jedis.JedisShardInfo;

@Configuration
@EnableRedisRepositories(basePackages = "com.manning.bippo.redis.dao")
public class RedisConfig
{

    @Value("${bippo.redis.host:localhost}")
    String redisHost;

    @Value("${bippo.redis.password:}")
    String redisPassword;

    @Bean
    public RedisConnectionFactory connectionFactory()
    {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        LogUtil.debug("Redis will connect to host: {}", redisHost);
        jedisConnectionFactory.setHostName(redisHost);
        jedisConnectionFactory.setPort(6379);
        if(redisPassword != null && !redisPassword.isEmpty())
        {
            jedisConnectionFactory.setPassword(redisPassword);
        }

        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory)
    {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        return redisTemplate;
    }
}
