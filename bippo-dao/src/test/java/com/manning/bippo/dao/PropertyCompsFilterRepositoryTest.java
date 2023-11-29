package com.manning.bippo.dao;

import com.manning.bippo.dao.config.SampleDataJpaApplication;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.dao.pojo.PropertyComp;
import com.manning.bippo.dao.pojo.PropertyCompsFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SampleDataJpaApplication.class)
public class PropertyCompsFilterRepositoryTest
{

    @Autowired
    NtreisPropertyRepository ntreisPropertyRepository;

    @Autowired
    PropertyCompsFilterRepository propertyCompsFilterRepository;

    @Autowired

    @Test
    public void test()
    {
        Page<NtreisProperty> all = ntreisPropertyRepository.findAll(new PageRequest(0, 10));
        assertEquals(all.getContent().size(), 10);

        NtreisProperty subject = all.getContent().get(0);
        PropertyCompsFilter filter = propertyCompsFilterRepository.findTopBySubjectOrderByUpdated(subject);
        if(filter == null)
        {
            filter = new PropertyCompsFilter();
            filter.setSubject(subject);
        }

        Set<PropertyComp> comps = new HashSet<>();
        for (int i = 4; i < 10; i++)
        {
            comps.add(new PropertyComp(filter, all.getContent().get(i), "ARV"));
        }
        filter.setPropertyComps(comps);
        propertyCompsFilterRepository.save(filter);

    }

}