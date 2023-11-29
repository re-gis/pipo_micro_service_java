package com.manning.bippo.service.trigger.impl;

import com.manning.bippo.service.config.SampleServicesApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SampleServicesApplication.class)
public class RetsTriggerServiceImplTest
{

    @Autowired
    RetsTriggerServiceImpl retsTriggerService;

    @Test
    public void verifyTrigger()
    {
        retsTriggerService.trigger();
    }

    @Test
    public void verifySkipPageNumber()
    {
        int skipPageNumber = retsTriggerService.getSkipPageNumber();

        assertEquals(0, skipPageNumber);
    }
}