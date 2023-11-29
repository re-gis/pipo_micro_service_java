package com.manning.bippo.aggregator.rets.pojo;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.commons.dto.CompsFilterCalculated;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.service.mapping.RetsBippoMappingService;
import java.util.List;
import org.apache.commons.lang.time.StopWatch;

public class TaskDownloadComps implements RetsFeedTask {

    public final NtreisProperty ntreisProperty;
    public final List<CompsFilterCalculated> compsFilters;
    public final String type;

    public TaskDownloadComps(NtreisProperty ntreisProperty, List<CompsFilterCalculated> compsFilters, String type) {
        this.ntreisProperty = ntreisProperty;
        this.compsFilters = compsFilters;
        this.type = type;
    }

    @Override
    public void accept(RetsBippoMappingService rets) {
        rets.importComps(this.ntreisProperty, this.compsFilters, this.type);
    }

    @Override
    public void log(StopWatch timer) {
        LogUtil.info("{} comps for MLSNumber {} finished in {} ms", this.type, this.ntreisProperty.getMLSNumber(), timer.getTime());
    }
}
