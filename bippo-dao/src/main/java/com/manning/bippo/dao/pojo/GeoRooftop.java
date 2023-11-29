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
@Table(name = "geo_rooftop")
@Data
public class GeoRooftop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    
    @OneToOne
    @JoinColumn(name = "semantics_id")
    AddressSemantics semantics;

    @Column(name = "latitude")
    Double latitude;

    @Column(name = "longitude")
    Double longitude;

    @Column(name = "created")
    Date creationDate;

}
