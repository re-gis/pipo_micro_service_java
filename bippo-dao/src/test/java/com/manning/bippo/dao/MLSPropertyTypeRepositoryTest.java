package com.manning.bippo.dao;

import com.manning.bippo.dao.config.SampleDataJpaApplication;
import com.manning.bippo.dao.pojo.MLSPropertyType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SampleDataJpaApplication.class)
public class MLSPropertyTypeRepositoryTest
{
    @Autowired
    MLSPropertyTypeRepository mlsPropertyTypeRepository;

    @Test
    public void simpleTest()
    {
        MLSPropertyType propertyType = new MLSPropertyType();
        propertyType.setValue("test");
        MLSPropertyType save = mlsPropertyTypeRepository.save(propertyType);

        Assert.assertNotNull(save.getId());

    }
}