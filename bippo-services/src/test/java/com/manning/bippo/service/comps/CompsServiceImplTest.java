package com.manning.bippo.service.comps;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.commons.dto.CompsFilter;
import com.manning.bippo.commons.dto.CompsFilterCalculated;
import com.manning.bippo.commons.dto.PropertySearchFilter;
import com.manning.bippo.dao.NtreisPropertyRepository;
import com.manning.bippo.dao.filter.PropertySearchSpecification;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.redis.dao.RedisPropertyRepository;
import com.manning.bippo.redis.pojo.RedisProperty;
import com.manning.bippo.service.config.SampleServicesApplication;
import com.manning.bippo.service.rets.RetsService;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SampleServicesApplication.class)
public class CompsServiceImplTest
{

    @Autowired
    NtreisPropertyRepository ntreisPropertyRepository;

    @Autowired
    RetsService retsService;

    @Autowired
    RedisPropertyRepository redisPropertyRepository;


    @Test
    public void verySoldStatusValid()
    {
        PropertySearchFilter propertySearchFilter = new PropertySearchFilter();
        propertySearchFilter.setMlsStatus(Arrays.asList("Sold"));
        PropertySearchSpecification specification = new PropertySearchSpecification(propertySearchFilter);
        Page<NtreisProperty> all = ntreisPropertyRepository.findAll(specification, new PageRequest(0, 1000));

        all.forEach(ntreisProperty ->
        {
            LogUtil.debug(ntreisProperty.getAddress());
//            List<RetsResponse> updatedMLSRecords = retsService.getUpdatedMLSRecords(ntreisProperty.getMatrix_Unique_ID());
//            updatedMLSRecords.forEach(retsResponse ->
//            {
//                LogUtil.debug("{} -> {}", ntreisProperty.getStatus(), retsResponse.Status);
//            });
            if (ntreisProperty.getLongitude() != null && ntreisProperty.getLongitude() != null)
            {

                RedisProperty redisProperty = RedisProperty.builder()
                        .id(ntreisProperty.getId())
//                        .propertyType(ntreisProperty.getPropertyType())
                        .propertySubType(ntreisProperty.getPropertySubType())
                        .location(new Point(ntreisProperty.getLongitude(), ntreisProperty.getLatitude()))
                        .mlsStatus(ntreisProperty.getStatus())
//                        .streetNumber(ntreisProperty.getStreetNumber())
//                        .streetName(ntreisProperty.getStreetName())
//                        .streetSuffix(ntreisProperty.getStreetSuffix())
//                        .city(ntreisProperty.getCity())
//                        .stateOrProvince(ntreisProperty.getStateOrProvince())
//                        .postalCode(ntreisProperty.getPostalCode())
                        .yearBuilt(ntreisProperty.getYearBuilt())
                        .sqftTotal(ntreisProperty.getSqFtTotal())
                        .statusChangeTimestamp(ntreisProperty.getStatusChangeTimestamp())
                        .build();
                LogUtil.debug("Saving property to redis: {}, {}, {}", ntreisProperty.getAddress(), ntreisProperty.getLongitude(), ntreisProperty.getLatitude());
                redisPropertyRepository.save(redisProperty);
            }
        });
    }

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void testQuery()
    {
        String status = "Sold";
        Point location = new Point(-96.73551, 32.91823);
        Distance radius = new Distance(0.5, Metrics.MILES);
        CompsFilterCalculated compsFilterCalculated = new CompsFilterCalculated(new CompsFilter());
        compsFilterCalculated.setSqftTotal(2000F);
        compsFilterCalculated.setPropertySubType(Collections.singletonList("RES-Single Family"));

        List<RedisProperty> byLocationNear = redisPropertyRepository.findByMlsStatusAndLocationNear(status, location, radius);

        Date afterStatusChangeTimestamp = DateUtils.addDays(new Date(), -compsFilterCalculated.getStatusChangeInDays());
        List<RedisProperty> collect = byLocationNear.stream()
                .filter(redisProperty -> redisProperty.getSqftTotal() > compsFilterCalculated.getSqftTotalFrom())
                .filter(redisProperty -> redisProperty.getSqftTotal() < compsFilterCalculated.getSqftTotalTo())
                .filter(redisProperty -> compsFilterCalculated.getPropertySubType().contains(redisProperty.getPropertySubType()))
                .filter(redisProperty -> redisProperty.getYearBuilt() > compsFilterCalculated.getYearBuiltFrom())
                .filter(redisProperty -> redisProperty.getYearBuilt() < compsFilterCalculated.getYearBuiltTo())
                .filter(redisProperty -> redisProperty.getStatusChangeTimestamp().after(afterStatusChangeTimestamp))
                .collect(Collectors.toList());

        Assert.assertNotNull(collect);
    }

}
