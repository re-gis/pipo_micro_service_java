package com.manning.bippo.aggregator.rets.pojo;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.service.mapping.RetsBippoMappingService;
import org.apache.commons.lang.time.StopWatch;

public class TaskUpdateMatrixId implements RetsFeedTask {

    public final String matrixId;
    public final boolean semanticize;

    public TaskUpdateMatrixId(String matrixId, boolean semanticize) {
        this.matrixId = matrixId;
        this.semanticize = semanticize;
    }

    @Override
    public void accept(RetsBippoMappingService rets) {
        rets.importUpdatedMLSRecords(this.matrixId, this.semanticize);
    }

    @Override
    public void log(StopWatch timer) {
        LogUtil.info("UpdateMatrix for {} finished in {} ms", this.matrixId, timer.getTime());
    }
}
