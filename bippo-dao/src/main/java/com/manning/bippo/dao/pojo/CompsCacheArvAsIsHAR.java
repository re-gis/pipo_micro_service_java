package com.manning.bippo.dao.pojo;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.manning.bippo.commons.dto.CompsFilterCalculated;
import com.manning.bippo.dao.converter.CompIdsJsonConverter;
import com.manning.bippo.dao.converter.CompsFilterCalculatedJsonConverter;
import com.manning.bippo.dao.converter.LongListConverter;
import lombok.Data;

@Entity
@Table(name = "comps_cache_arv_as_is_har")
@Data
public class CompsCacheArvAsIsHAR {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "arv_value")
    Double arvValue;

    @Column(name = "as_is_value")
    Double asIsValue;

    @Column(name = "recorded")
    Date recordedDate;

    @Column(name = "trestle_id")
    Long trestleId;

    @Convert(converter = CompsFilterCalculatedJsonConverter.class)
    @Column(name = "filter")
    CompsFilterCalculated filter;

    @Convert(converter = LongListConverter.class)
    @Column(name = "comp_ids")
    List<Long> compIds;

    @Convert(converter = LongListConverter.class)
    @Column(name = "selected_comp_ids")
    List<Long> selectedCompIds;

    @Convert(converter = LongListConverter.class)
    @Column(name = "excluded_comp_ids")
    List<Long> excludedCompIds;
}
