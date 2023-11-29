package com.manning.bippo.dao.pojo;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "auction_documents")
@Data
public class AuctionDocument
{
    public static final String TYPE_FORECLOSURE_POSTING = "posting";
    public static final String TYPE_NOTICE_TO_TRUSTEE = "notice";
    public static final String TYPE_O_AND_E_REPORT = "oe";

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "auction_id", referencedColumnName = "id")
    private AuctionProperty auctionProperty;

    @Column(name = "s3_url")
    private String s3Url;

    @Column(name = "doc_type")
    private String documentType;

    @Column(name = "expiry")
    private Date expiry;
}
