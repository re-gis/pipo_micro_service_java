package com.manning.bippo.dao;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.dao.config.SampleDataJpaApplication;
import com.manning.bippo.dao.pojo.AddressSemantics;
import com.manning.bippo.dao.pojo.NtreisProperty;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SampleDataJpaApplication.class)
public class AddressSemanticsTest
{
    @Autowired
    AddressSemanticsRepository addressSemanticsRepository;

    @Test
    public void test()
    {
        addressSemanticsRepository.findTopByNtreisProperty(new NtreisProperty());
    }

}
