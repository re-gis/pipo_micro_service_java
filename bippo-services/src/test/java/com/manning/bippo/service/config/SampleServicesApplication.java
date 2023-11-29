package com.manning.bippo.service.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.aws.autoconfigure.cache.ElastiCacheAutoConfiguration;
import org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration;
import org.springframework.cloud.aws.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.manning.bippo.dao")
@EnableRedisRepositories(basePackages = "com.manning.bippo.redis.dao")
@EntityScan({"com.manning.bippo.dao.pojo", "com.manning.bippo.dao.rets.pojo"})
@ComponentScan(basePackages = {"com.manning.bippo.service.smartystreets", "com.manning.bippo.service"})
@EnableAutoConfiguration(exclude = {
        ElastiCacheAutoConfiguration.class,
        MailSenderAutoConfiguration.class,
        ContextStackAutoConfiguration.class
})
public class SampleServicesApplication
{
}
