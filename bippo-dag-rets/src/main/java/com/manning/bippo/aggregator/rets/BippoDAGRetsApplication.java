package com.manning.bippo.aggregator.rets;

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
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories(basePackages = "com.manning.bippo.redis.dao")
@EnableJpaRepositories(basePackages = "com.manning.bippo.dao")
@EntityScan(basePackages = { "com.manning.bippo.dao.pojo", "com.manning.bippo.dao.rets.pojo" })
@ComponentScan(basePackages = {
        "com.manning.bippo.service",
        "com.manning.bippo.aggregator.rets.service",
})
@EnableAutoConfiguration(exclude = {
        ElastiCacheAutoConfiguration.class,
        MailSenderAutoConfiguration.class,
        ContextStackAutoConfiguration.class
})
public class BippoDAGRetsApplication extends SpringBootServletInitializer {
    public static void main(String[] cheese) {
        SpringApplication.run(BippoDAGRetsApplication.class, cheese);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BippoDAGRetsApplication.class);
    }

}