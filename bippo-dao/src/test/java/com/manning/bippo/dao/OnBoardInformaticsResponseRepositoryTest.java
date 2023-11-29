package com.manning.bippo.dao;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.dao.config.SampleDataJpaApplication;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.dao.pojo.OnBoardInformaticsResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SampleDataJpaApplication.class)
public class OnBoardInformaticsResponseRepositoryTest
{
    @Autowired
    NtreisPropertyRepository ntreisPropertyRepository;

    @Autowired
    OnBoardInformaticsResponseRepository onBoardInformaticsResponseRepository;

    @Test
    public void verifyOnBoardInformaticsResponse()
    {
        Page<NtreisProperty> all = ntreisPropertyRepository.findAll(new PageRequest(0, 1));
        Assert.assertEquals(all.getContent().size(), 1);

        NtreisProperty ntreisProperty = all.getContent().get(0);
        OnBoardInformaticsResponse response = new OnBoardInformaticsResponse();
        response.setNtreisProperty(ntreisProperty);

        OnBoardInformaticsResponse save = onBoardInformaticsResponseRepository.save(response);
        Assert.assertNotNull(save);
    }

    @Test
    public void simpleTest()
    {
        List<NtreisProperty> allWithNoOIResponse = ntreisPropertyRepository.findAllWithNoOIResponse();

        Assert.assertEquals(allWithNoOIResponse.size(), 100);

        allWithNoOIResponse.forEach(ntreisProperty ->
        {
            LogUtil.debug("MLSNumber: {}", ntreisProperty.getMLSNumber());
        });
    }

}