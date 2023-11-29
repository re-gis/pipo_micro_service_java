package com.manning.bippo.aggregator.rets.pojo;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.commons.dto.PropertySearchFilter;
import com.manning.bippo.service.mapping.RetsBippoMappingService;
import org.apache.commons.lang.time.StopWatch;

public class TaskDownloadForFilter implements RetsFeedTask {

    public final PropertySearchFilter filter;

    public TaskDownloadForFilter(PropertySearchFilter filter) {
        this.filter = filter;
    }

    @Override
    public void accept(RetsBippoMappingService rets) {
        rets.importByFilter(this.filter);
    }

    @Override
    public void log(StopWatch timer) {
        LogUtil.info("Download for a search filter finished in {} ms", timer.getTime());
    }
}
