package com.manning.bippo.aggregator.cron;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableRedisRepositories(basePackages = "com.manning.bippo.redis.dao")
@EnableJpaRepositories(basePackages = "com.manning.bippo.dao")
@EntityScan(basePackages = {"com.manning.bippo.dao.pojo", "com.manning.bippo.dao.rets.pojo"})
@ComponentScan(basePackages = {
        "com.manning.bippo.service",
        "com.manning.bippo.aggregator.cron.service",
})
@EnableScheduling
public class BippoDAGCronApplication extends SpringBootServletInitializer
{
    public static void main(String[] cheese)
    {
        SpringApplication.run(BippoDAGCronApplication.class, cheese);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        return application.sources(BippoDAGCronApplication.class);
    }

}
