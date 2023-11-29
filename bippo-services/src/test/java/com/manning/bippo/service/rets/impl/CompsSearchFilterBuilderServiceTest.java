package com.manning.bippo.service.rets.impl;

import com.manning.bippo.commons.dto.CompsFilter;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CompsSearchFilterBuilderServiceTest
{

    @Test
    public void buildUrbanCalculators()
    {
        CompsSearchFilterBuilderService compsSearchFilterBuilderService = new CompsSearchFilterBuilderService();

        List<CompsFilter> urban = compsSearchFilterBuilderService.buildCompsFilters("URBAN");

        Assert.assertNotNull(urban);
    }

}