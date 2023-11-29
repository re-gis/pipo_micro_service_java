package com.manning.bippo.dao.pojo;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "profiling_metrics")
@Data
public class ProfilingMetrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "label")
    String label;

    @Column(name = "counter")
    Integer counter;

    @Column(name = "created")
    Date creationDate;
}
