package com.manning.bippo.dao;

import com.manning.bippo.dao.config.SampleDataJpaApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SampleDataJpaApplication.class)
public class NtreisPropertyRepositoryTest
{
    @Autowired
    NtreisPropertyRepository ntreisPropertyRepository;

    @Test
    public void simpelTest()
    {
        Page<Long> propertyIdsWithNoLatLong = ntreisPropertyRepository.getPropertyIdsWithNoLatLong(new PageRequest(1, 10));

        Assert.assertNotNull(propertyIdsWithNoLatLong);
    }
}