package com.manning.bippo.dao;

import com.manning.bippo.dao.config.SampleDataJpaApplication;
import com.manning.bippo.dao.pojo.TaxProperty;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SampleDataJpaApplication.class)
public class TaxTaxPropertyRepositoryTest
{

    @Autowired
    TaxPropertyRepository taxPropertyRepository;

    @Test
    public void simpleTest()
    {
        List<TaxProperty> properties = taxPropertyRepository.findByFullAddressLikeAndLatitudeAndLongitude("516", BigDecimal.valueOf(33.03458), BigDecimal.valueOf(-96.59956));

        assertNotNull(properties);

    }

}