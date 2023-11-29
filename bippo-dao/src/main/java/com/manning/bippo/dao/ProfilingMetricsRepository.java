package com.manning.bippo.dao;

import com.manning.bippo.dao.pojo.ProfilingMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfilingMetricsRepository extends JpaRepository<ProfilingMetrics, Long> {

    public ProfilingMetrics findFirstByLabel(String label);
}
