package com.manning.bippo.aggregator.cron.service;

import com.manning.bippo.commons.LogUtil;
import org.junit.Test;
import org.springframework.scheduling.support.CronSequenceGenerator;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CronStringTester
{

    @Test
    public void nextSchedule()
    {
        CronSequenceGenerator generator = new CronSequenceGenerator("* */15 * * * *");

        LogUtil.debug("Next Schedule run: {}", generator.next(new Date()));
    }

    @Test
    public void showTimes()
    {
        TimeZone.setDefault(TimeZone.getTimeZone("CST"));

        LogUtil.debug("CST : {}", new Date());
    }
}
