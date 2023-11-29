package com.manning.bippo.service.profiling;

import com.manning.bippo.dao.ProfilingMetricsRepository;
import com.manning.bippo.dao.pojo.ProfilingMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ProfilingMetricsService {

    @Autowired
    ProfilingMetricsRepository profilingMetricsRepository;

    @Async
    public void incrementCounter(String label) {
        this.deltaCounterSync(label, 1);
    }

    @Async
    public void incrementCounter(String label, int i) {
        this.deltaCounterSync(label, i);
    }

    public void deltaCounterSync(String label, int d) {
        if (d == 0) {
            return;
        }

        ProfilingMetrics pm = this.profilingMetricsRepository.findFirstByLabel(label);

        if (pm == null) {
            pm = new ProfilingMetrics();
            pm.setLabel(label);
        }

        final Integer ct = pm.getCounter();
        pm.setCounter(ct == null ? d : ct.intValue() + d);
        this.profilingMetricsRepository.save(pm);
    }
}
