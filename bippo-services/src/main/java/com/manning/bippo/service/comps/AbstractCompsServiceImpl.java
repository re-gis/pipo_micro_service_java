package com.manning.bippo.service.comps;

import com.google.common.collect.Lists;
import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.commons.dto.CompsFilterCalculated;
import com.manning.bippo.dao.itf.AbstractProperty;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.service.redis.RedisPropertyService;
import org.apache.commons.lang.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AbstractCompsServiceImpl
{
    @Autowired
    RedisPropertyService redisPropertyService;

    public List<? extends AbstractProperty> getCompsWithFilter(Float longitude, Float latitude, CompsFilterCalculated compsFilter)
    {
        StopWatch timer = new StopWatch();
        timer.start();

        if (compsFilter.region == null) {
            compsFilter.region = "NTREIS";
        }

        compsFilter.primed = true;
        final List<? extends AbstractProperty> comps;

        switch (compsFilter.region) {
            case "HAR":
                comps = redisPropertyService.findByFilterHAR(longitude, latitude, compsFilter);
                break;
            case "NTREIS":
            default:
                comps = redisPropertyService.findByFilter(longitude, latitude, compsFilter);
                break;
        }

//        Date statusChangeFrom = DateUtils.addDays(new Date(), -compsFilter.statusChangeInDays);
//        List<NtreisProperty> comps = ntreisPropertyRepository.findByCompsFilter(compsFilter.getProximityInMiles(),
//                compsFilter.getSqftTotalFrom(), compsFilter.getSqftTotalTo(),
//                compsFilter.getYearBuiltFrom(), compsFilter.getYearBuiltTo(),
//                compsFilter.getStatus(), compsFilter.getPropertySubType(),
//                statusChangeFrom,
//                ntreisProperty.getLongitude(), ntreisProperty.getLatitude());

        timer.stop();
        LogUtil.info("ntreisPropertyRepository.findByCompsFilter took {} msec.",  timer.getTime());
        return comps;
    }
}
