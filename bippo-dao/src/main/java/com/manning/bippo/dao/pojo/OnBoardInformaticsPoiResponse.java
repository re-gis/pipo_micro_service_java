package com.manning.bippo.dao.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "onboard_informatics_poi_response")
@Data
public class OnBoardInformaticsPoiResponse
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "address")
    String address;

    @Column(name = "response")
    String response;

    @Column(name = "created")
    Date created;

    @Column(name = "updated")
    Date updated;

    @PrePersist
    protected void onCreate()
    {
        created = new Date();
    }

    @PreUpdate
    protected void onUpdate()
    {
        updated = new Date();
    }

}
