package com.manning.bippo.dao.pojo;

import com.manning.bippo.commons.dto.CompsFilterCalculated;
import com.manning.bippo.dao.converter.CompIdsJsonConverter;
import com.manning.bippo.dao.converter.CompsFilterCalculatedJsonConverter;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "comps_cache_rents")
@Data
public class CompsCacheRents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "rent_high_value")
    Double rentHighValue;

    @Column(name = "rent_low_value")
    Double rentLowValue;

    @Column(name = "rent_average_value")
    Double rentAverageValue;

    @Column(name = "recorded")
    Date recordedDate;

    @OneToOne
    @JoinColumn(name = "subject_semantics_id")
    AddressSemantics subjectSemanticsId;

    @OneToOne
    @JoinColumn(name = "subject_ntreis_id")
    NtreisProperty subjectNtreisId;

    @OneToOne
    @JoinColumn(name = "subject_tax_id")
    OnBoardInformaticsResponse subjectTaxId;

    @OneToOne
    @JoinColumn(name = "subject_auction_id")
    AuctionProperty subjectAuctionId;

    @Convert(converter = CompsFilterCalculatedJsonConverter.class)
    @Column(name = "filter")
    CompsFilterCalculated filter;

    @Convert(converter = CompIdsJsonConverter.class)
    @Column(name = "comp_ids")
    int[] compIds;

    @Column(name = "high_comp_id")
    Integer highCompId;

    @Column(name = "low_comp_id")
    Integer lowCompId;
}
