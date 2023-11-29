package com.manning.bippo.service.rets.impl;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.commons.dto.CompsFilter;
import com.manning.bippo.commons.dto.CompsFilterCalculated;
import com.manning.bippo.commons.dto.PropertySearchFilter;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.service.config.SampleServicesApplication;
import com.manning.bippo.service.mapping.RetsBippoMappingService;
import com.manning.bippo.service.rets.pojo.RetsResponse;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.realtors.rets.client.RetsException;
import org.realtors.rets.common.metadata.Metadata;
import org.realtors.rets.common.metadata.types.MClass;
import org.realtors.rets.common.metadata.types.MLookup;
import org.realtors.rets.common.metadata.types.MTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SampleServicesApplication.class)
public class RetsServiceImplTest
{
    @Autowired
    RetsServiceImpl retsService;

    @Autowired
    RetsBippoMappingService retsBippoMappingService;

    @Test
    public void simpleTest() throws MalformedURLException
    {
        List<RetsResponse> mlsRecord = retsService.findMLSRecord("5167", "Navajo", "Drive", "Frisco", 75034);

        assertEquals(3, mlsRecord.size());

        List<NtreisProperty> ntreisProperties = retsBippoMappingService.mapRetsResponseToNtreisProperty(mlsRecord);
        assertEquals(3, ntreisProperties.size());

        ntreisProperties.forEach(ntreisProperty ->
        {
            LogUtil.info("MLS Number: {}", ntreisProperty.getMLSNumber());
            LogUtil.info("MLS {}", ntreisProperty.getMLS());
        });

//        List<NtreisProperty> persistantNtreisProperties = retsService.importNtreisProperties(ntreisProperties);
//        assertEquals(4, persistantNtreisProperties.size());

    }

    @Test
    public void getUpdatedRecords()
    {
        Date endTime = new Date();
        Date startTime = DateUtils.addMinutes(endTime, -6);
        List<NtreisProperty> ntreisResponses = retsBippoMappingService.importUpdatedMLSRecords(startTime, endTime);

        assertTrue(ntreisResponses.size() > 0);

        ntreisResponses.forEach(ntreisResponse ->
        {
            LogUtil.debug(ntreisResponse.getAddress());
        });

    }

    @Test
    public void getPhotos()
    {
        retsBippoMappingService.getAllPhotos(Collections.singletonList(896237L));
    }

    @Test
    public void importSold()
    {
        Date endTime = new Date();
        Date startTime = DateUtils.addDays(endTime, -1);
        retsBippoMappingService.importSoldMLSRecords(startTime, endTime);
    }

    @Test
    public void testRetsFields() throws RetsException
    {
        Metadata metadata = retsService.getMetadata();
        for (MClass mClass : metadata.getResource("Property").getMClasses())
        {
            LogUtil.info("class: {}", mClass.getClassName());
            MTable[] mTables = mClass.getMTables();
            for (MTable mTable : mTables)
            {
                LogUtil.debug("path: {}, DataType: {}, maxLength: {}", mTable.getPath(), mTable.getDataType(), mTable.getMaximumLength());
            }
        }
    }

    @Test
    public void searchByFilter()
    {
        PropertySearchFilter filter = new PropertySearchFilter();
//        filter.setBathsFrom(3);
//        filter.setBathsTo(5);
        filter.setPropertyType(Collections.singletonList("Residential Lease"));
//        filter.setCounty("Dallas");
//        filter.setSqftFrom(1000f);
//        filter.setSqftTo(1500f);
//        filter.setPool(Boolean.TRUE);
//        filter.setNotInMLSStatus(Arrays.asList("Cancelled", "Sold"));
//        filter.setHoaFrom(100);
//        filter.setHoaTo(400);
        filter.setStatusChangeTimestampTo(new Date());
        filter.setStatusChangeTimestampFrom(DateUtils.addDays(new Date(), -1));

        Page<RetsResponse> bySearchFilter = retsService.findBySearchFilter(filter, 0, 10);
        Assert.assertEquals(1000, bySearchFilter.getTotalElements());

//        NtreisProperty ntreisProperty = retsBippoMappingService.mapRetsResponseToNtreisProperty(bySearchFilter.getContent().get(0));

//        Assert.assertEquals(5, retsService.getTotalResultsCountBySearchFilter(filter));
//        int newResultsCountBySearchFilter = retsService.getNewResultsCountBySearchFilter(filter, DateUtils.addDays(new Date(), -1), new Date());
//        Assert.assertNotNull(ntreisProperty.getListDate());
    }

    @Test
    public void searchByGeoCode()
    {
        CompsFilterCalculated compsFilter = new CompsFilterCalculated(new CompsFilter());
        compsFilter.proximityInMiles = 2d;
        compsFilter.setYearBuilt(1973);
        compsFilter.setSqftTotal(2422f);
//        compsFilter.setStatus("S");
        compsFilter.setStatus(null);
        compsFilter.setPropertyType("LSE");
        Page<RetsResponse> comps = retsService.findComps(32.84048,
                -96.616, compsFilter, 1, 10);

        LogUtil.debug("Running query: " + compsFilter.toString());

        assertNotNull(comps);
    }

    @Test
    public void searchSoldComps()
    {
        CompsFilterCalculated compsFilter = new CompsFilterCalculated(new CompsFilter());
        compsFilter.proximityInMiles = 1.41d;
        compsFilter.setYearBuilt(1983);
        compsFilter.setSqftTotal(1634f);
        compsFilter.setStatus("S");
//        compsFilter.setStatus(null);
        compsFilter.setPropertyType("RES");
        Page<RetsResponse> comps = retsService.findComps(32.745903, -96.64491, compsFilter, 0, 25);

        assertNotNull(comps);
        comps.forEach(c -> LogUtil.debug("{} - {}",  c.address(), c.MatrixModifiedDT));
    }


    @Test
    public void getRetsFields()
    {
        RetsResponse sqftGross = retsService.getRetsResponseForFields(61445595, Collections.singletonList("SQFTGross"));

        assertNotNull(sqftGross);
    }
}