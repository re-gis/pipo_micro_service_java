package com.manning.bippo.service.oi.impl;

import com.manning.bippo.dao.NtreisPropertyRepository;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.service.config.SampleServicesApplication;
import com.manning.bippo.service.oi.OnBoardInformaticsPropertyService;
import com.manning.bippo.service.oi.pojo.AllEventsPropertyResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SampleServicesApplication.class)
public class OnBoardInformaticsPropertyServiceImplTest
{

    @Autowired
    OnBoardInformaticsPropertyService propertyService;

    @Test
    public void verifyGetAllEvents()
    {
//        OnBoardInformaticsPropertyServiceImpl propertyService = new OnBoardInformaticsPropertyServiceImpl();

        AllEventsPropertyResponse allEvents = propertyService.getAllEvents("59 ABBEY WOODS LN", "DALLAS, TX 75248");

        assertNotNull(allEvents);
    }

    @Autowired
    NtreisPropertyRepository ntreisPropertyRepository;

    @Test
    public void verifyForMLSNumber()
    {
        NtreisProperty byMLSNumber = ntreisPropertyRepository.findByMLSNumber(13581702);

        AllEventsPropertyResponse byNtreisProperty = propertyService.findByNtreisProperty(byMLSNumber, false);

        assertNotNull(byNtreisProperty);
    }

}