package com.manning.bippo.dao.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "onboard_informatics_analytics_response")
@Data
public class OnBoardInformaticsAnalyticsResponse
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "zip")
    int zip;

    @Column(name = "year")
    int year;

    @Column(name = "quarter")
    int quarter;

    @Column(name = "price")
    int price;

    @Column(name = "count")
    int count;

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
