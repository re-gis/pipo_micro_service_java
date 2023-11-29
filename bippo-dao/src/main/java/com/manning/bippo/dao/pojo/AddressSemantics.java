package com.manning.bippo.dao.pojo;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "address_semantics")
@Data
public class AddressSemantics
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "first_line")
    String firstLine;

    @Column(name = "last_line")
    String lastLine;

    @Column(name = "primary_number")
    String primaryNumber;

    @Column(name = "street_name")
    String streetName;

    @Column(name = "street_suffix")
    String streetSuffix;

    @Column(name = "city_name")
    String cityName;

    @Column(name = "state")
    String state;

    @Column(name = "zipcode")
    Integer zipcode;

    @Column(name = "plus4_code")
    Integer plus4Code;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ntreis_id")
    NtreisProperty ntreisProperty;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_id")
    OnBoardInformaticsResponse taxId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id")
    AuctionProperty auctionProperty;

    @Column(name = "trestle_system")
    String trestleSystem;

    @Column(name = "trestle_id")
    Long trestleId;
}
