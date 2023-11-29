package com.manning.bippo.aggregator.rets.pojo;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.service.mapping.RetsBippoMappingService;
import java.util.Collections;
import org.apache.commons.lang.time.StopWatch;

public class TaskFrontPhotos implements RetsFeedTask {

    public final long propertyId;

    public TaskFrontPhotos(long propertyId) {
        this.propertyId = propertyId;
    }

    @Override
    public void accept(RetsBippoMappingService rets) {
        rets.getFrontPhotos(Collections.singletonList(Long.valueOf(this.propertyId)));
    }

    @Override
    public void log(StopWatch timer) {
        LogUtil.info("FrontPhotos for {} finished in {} ms", this.propertyId, timer.getTime());
    }
}
