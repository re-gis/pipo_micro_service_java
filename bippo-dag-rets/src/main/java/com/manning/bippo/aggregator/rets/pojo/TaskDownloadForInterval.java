package com.manning.bippo.aggregator.rets.pojo;

import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.commons.dto.DownloadRetsUpdated;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.service.mapping.RetsBippoMappingService;
import com.manning.bippo.service.queuing.QueuingService;
import com.manning.bippo.service.queuing.impl.RabbitMQSenderService;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import org.apache.commons.lang.time.StopWatch;

public class TaskDownloadForInterval implements RetsFeedTask {

    private static final SimpleDateFormat datefmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

    static {
        TaskDownloadForInterval.datefmt.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
    }

    public final DownloadRetsUpdated interval;
    private final QueuingService queuingService;
    private final RabbitMQSenderService rabbitMQSenderService;

    public TaskDownloadForInterval(DownloadRetsUpdated interval, QueuingService qs, RabbitMQSenderService rs) {
        this.interval = interval;
        this.queuingService = qs;
        this.rabbitMQSenderService = rs;
    }

    @Override
    public void accept(RetsBippoMappingService rets) {
        List<NtreisProperty> ntreisProperties = rets.importUpdatedMLSRecords(this.interval.getStartTime(), this.interval.getEndTime());
        this.queuingService.queueSearchPropertiesProcesses(ntreisProperties, false);
        ntreisProperties.forEach(p -> this.rabbitMQSenderService.queueARVComps(Collections.singletonList(p.getId())));
    }

    @Override
    public void log(StopWatch timer) {
        LogUtil.info("Download between [{}, {}] finished in {} ms",
                datefmt.format(this.interval.getStartTime()), datefmt.format(this.interval.getEndTime()), timer.getTime());
    }
}
