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
@Table(name = "comps_cache_wholesale")
@Data
public class CompsCacheWholesale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "wholesale_value")
    Double wholesaleValue;

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

    @Convert(converter = CompIdsJsonConverter.class)
    @Column(name = "selected_comp_ids")
    int[] selectedCompIds;
}
