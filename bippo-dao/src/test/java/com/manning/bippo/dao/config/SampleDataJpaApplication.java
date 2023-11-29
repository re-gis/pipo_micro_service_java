package com.manning.bippo.dao.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.manning.bippo.dao")
@EntityScan(basePackages = {"com.manning.bippo.dao.pojo", "com.manning.bippo.dao.rets.pojo"})
public class SampleDataJpaApplication
{
    public static void main(String[] args) throws Exception
    {
        SpringApplication.run(SampleDataJpaApplication.class, args);
    }
}
