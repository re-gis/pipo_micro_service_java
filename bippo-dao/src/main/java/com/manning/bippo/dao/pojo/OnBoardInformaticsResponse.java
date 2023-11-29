package com.manning.bippo.dao.pojo;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "onboard_informatics_response")
@Data
public class OnBoardInformaticsResponse
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "ntreis_id")
    NtreisProperty ntreisProperty;

    @Column(name = "response")
//    @Convert(converter = AreaFullPropertyResponseConverter.class)
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
