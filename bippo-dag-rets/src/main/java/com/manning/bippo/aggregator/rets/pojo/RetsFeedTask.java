package com.manning.bippo.aggregator.rets.pojo;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.service.mapping.RetsBippoMappingService;
import java.util.function.Consumer;
import org.apache.commons.lang.time.StopWatch;

public interface RetsFeedTask extends Consumer<RetsBippoMappingService> {

    @Override
    public void accept(RetsBippoMappingService rets);

    public default void log(StopWatch timer) {
        LogUtil.info("{} finished in {} ms", this.getClass().getSimpleName(), timer.getTime());
    }
}
