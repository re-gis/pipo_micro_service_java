package com.manning.bippo.service.mapping;

import com.manning.bippo.commons.dto.PropertySearchFilter;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.service.config.SampleServicesApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SampleServicesApplication.class)
public class RetsBippoMappingServiceImplTest
{

    @Autowired
    RetsBippoMappingServiceImpl retsBippoMappingService;

    @Test
    public void importByFilterTest()
    {
        PropertySearchFilter retsSearch = new PropertySearchFilter();
        retsSearch.setCity("Dallas");
        retsSearch.setCounty("Dallas");
        retsSearch.setBedsFrom(3);

        retsBippoMappingService.importByFilter(retsSearch);
    }

    @Test
    public void putImage()
    {
        NtreisProperty ntreisProperty = new NtreisProperty();
        ntreisProperty.setMatrix_Unique_ID(60179462);
        ntreisProperty.setCountyOrParish("Denton");

        NtreisProperty ntreisProperty2 = new NtreisProperty();
        ntreisProperty2.setMatrix_Unique_ID(60262596);
        ntreisProperty2.setCountyOrParish("Anderson");

        retsBippoMappingService.getPhotosByType(Arrays.asList(ntreisProperty, ntreisProperty2), "Photo");
    }

}