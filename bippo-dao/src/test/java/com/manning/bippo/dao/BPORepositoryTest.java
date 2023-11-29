package com.manning.bippo.dao;

import com.manning.bippo.dao.config.SampleDataJpaApplication;
import com.manning.bippo.dao.pojo.BPO;
import com.manning.bippo.dao.pojo.NtreisProperty;
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
public class BPORepositoryTest
{
    @Autowired
    BPORepository bpoRepository;

    @Autowired
    NtreisPropertyRepository ntreisPropertyRepository;

    @Test
    public void simpleTest()
    {
        BPO bpo = new BPO();
        NtreisProperty ntreisProperty = ntreisPropertyRepository.findAll(new PageRequest(0, 1)).getContent().get(0);
        bpo.setSubject(ntreisProperty);
        BPO save = bpoRepository.save(bpo);

        Page<BPO> bySubject = bpoRepository.findBySubject(ntreisProperty, new PageRequest(0, 10));

        Assert.assertNotNull(bySubject);
        Assert.assertNotNull(bySubject.getContent().get(0).getSoldComps());
    }


}