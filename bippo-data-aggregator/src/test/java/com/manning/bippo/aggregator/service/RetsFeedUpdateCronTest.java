package com.manning.bippo.aggregator.service;

import org.junit.Test;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.util.Assert;

import java.util.Date;

public class RetsFeedUpdateCronTest
{

    @Test
    public void simpleTest()
    {
        CronSequenceGenerator generator = new CronSequenceGenerator("0 0 */4 * * *");
        Date next = generator.next(new Date());

        Assert.notNull(next);
    }

}