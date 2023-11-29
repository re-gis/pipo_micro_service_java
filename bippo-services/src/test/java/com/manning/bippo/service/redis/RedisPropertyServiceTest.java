package com.manning.bippo.service.redis;

import com.manning.bippo.commons.dto.CompsFilterCalculated;
import com.manning.bippo.dao.NtreisPropertyRepository;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.service.config.SampleServicesApplication;
import com.manning.bippo.service.rets.impl.CompsSearchFilterBuilderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SampleServicesApplication.class)
public class RedisPropertyServiceTest
{
    @Autowired
    RedisPropertyService redisPropertyService;

    @Autowired
    CompsSearchFilterBuilderService compsSearchFilterBuilderService;

    @Autowired
    NtreisPropertyRepository ntreisPropertyRepository;

    @Test
    public void searchRedis()
    {
        NtreisProperty byMLSNumber = ntreisPropertyRepository.findByMLSNumber(13532600);
        List<CompsFilterCalculated> compsFilters = compsSearchFilterBuilderService.buildCompsFilters(
                "suburban", byMLSNumber.getSqFtTotal(), byMLSNumber.getYearBuilt(),
                "Sold", Collections.singletonList(byMLSNumber.getPropertySubType()));

        Optional<CompsFilterCalculated> arvCompsFilterUsed = redisPropertyService.getARVCompsFilterUsed(null, byMLSNumber);

        assertTrue(arvCompsFilterUsed.isPresent());

    }
}