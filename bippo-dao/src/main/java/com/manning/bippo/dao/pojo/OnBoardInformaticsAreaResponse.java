package com.manning.bippo.dao.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "onboard_informatics_area_response")
@Data
public class OnBoardInformaticsAreaResponse
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "area_code")
    String areaCode;

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
