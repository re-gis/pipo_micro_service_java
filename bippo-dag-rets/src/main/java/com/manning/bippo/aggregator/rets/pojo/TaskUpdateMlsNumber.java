package com.manning.bippo.aggregator.rets.pojo;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.service.mapping.RetsBippoMappingService;
import org.apache.commons.lang.time.StopWatch;

public class TaskUpdateMlsNumber implements RetsFeedTask {

    public final int mlsNumber;

    public TaskUpdateMlsNumber(int mlsNumber) {
        this.mlsNumber = mlsNumber;
    }

    @Override
    public void accept(RetsBippoMappingService rets) {
        rets.importUpdatedMLSRecords(this.mlsNumber);
    }

    @Override
    public void log(StopWatch timer) {
        LogUtil.info("UpdateMLS for {} finished in {} ms", this.mlsNumber, timer.getTime());
    }
}
