package com.manning.bippo.dao.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "onboard_informatics_mortgage_owner_response")
@Data
public class OnBoardInformaticsMortgageOwnerResponse
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "attom_id")
    Long attomId;

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
