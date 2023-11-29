package com.manning.bippo.dao.pojo;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "zillow_property")
@Data
public class ZillowProperty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "semantics_id")
    AddressSemantics semantics;

    @Column(name = "zillow_id")
    int zillowId;
    @Column(name = "fips")
    String fipsCode;
    @Column(name = "type")
    String propertyType;
    @Column(name = "tax_year")
    int taxYear;
    @Column(name = "tax_assessment")
    double taxAssessment;
    @Column(name = "year_built")
    int yearBuilt;
    @Column(name = "lot_footage")
    int lotFootage;
    @Column(name = "finished_footage")
    int finishedFootage;
    @Column(name = "bedrooms")
    int bedrooms;
    @Column(name = "bathrooms")
    float bathrooms;
    @Column(name = "total_rooms")
    int totalRooms;
    @Column(name = "link_details")
    String linkToDetails;
    @Column(name = "link_graphs")
    String linkToGraphs;
    @Column(name = "link_map")
    String linkToMap;
    @Column(name = "link_comps")
    String linkToComps;
    @Column(name = "first_line")
    String firstLine;
    @Column(name = "zip_code")
    String zipCode;
    @Column(name = "city")
    String city;
    @Column(name = "state")
    String state;
    @Column(name = "latitude")
    double latitude;
    @Column(name = "longitude")
    double longitude;
    @Column(name = "zestimate")
    double zestimate;
    @Column(name = "zestimate_low")
    double zestimateLow;
    @Column(name = "zestimate_high")
    double zestimateHigh;
    @Column(name = "created")
    Date creationDate;
}
