package com.manning.bippo.aggregator;

import com.manning.bippo.aggregator.config.AsyncConfig;
import com.manning.bippo.aggregator.config.RedisConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.aws.autoconfigure.cache.ElastiCacheAutoConfiguration;
import org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration;
import org.springframework.cloud.aws.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.manning.bippo.dao")
@EntityScan(basePackages = {"com.manning.bippo.dao.pojo", "com.manning.bippo.dao.rets.pojo"})
@ComponentScan(basePackages = {
        "com.manning.bippo.service.smartystreets",
        "com.manning.bippo.aggregator.controller",
        "com.manning.bippo.service",
        "com.manning.bippo.service.mapping",
        "com.manning.bippo.aggregator.service",
        "com.manning.bippo.aggregator.config"
})
@EnableSwagger2
@EnableAutoConfiguration(exclude = {
        ElastiCacheAutoConfiguration.class,
        MailSenderAutoConfiguration.class,
        ContextStackAutoConfiguration.class
})
public class BippoDataAggregatorApplication extends SpringBootServletInitializer
{
    public static void main(String[] args)
    {
        SpringApplication.run(BippoDataAggregatorApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        return application.sources(BippoDataAggregatorApplication.class);
    }
}
