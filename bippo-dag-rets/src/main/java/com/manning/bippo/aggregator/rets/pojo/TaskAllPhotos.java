package com.manning.bippo.aggregator.rets.pojo;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.service.mapping.RetsBippoMappingService;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.time.StopWatch;

public class TaskAllPhotos implements RetsFeedTask {

    public final long propertyId;

    public TaskAllPhotos(long propertyId) {
        this.propertyId = propertyId;
    }

    @Override
    public void accept(RetsBippoMappingService rets) {
        final List<Long> list = Collections.singletonList(Long.valueOf(this.propertyId));
        rets.getAllPhotos(list);
        rets.markPhotosDoNotCheck(list);
    }

    @Override
    public void log(StopWatch timer) {
        LogUtil.info("AllPhotos for {} finished in {} ms", this.propertyId, timer.getTime());
    }
}
