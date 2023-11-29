package com.manning.bippo.dao.pojo;

import javax.persistence.*;

@Entity
@Table(name = "ntreis_photo")
public class NtreisPhoto
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "ntreis_id", referencedColumnName = "id")
    NtreisProperty ntreisProperty;

    @Column(name = "s3_url")
    String s3Url;

    String type;

    public NtreisPhoto()
    {
    }

    public NtreisPhoto(NtreisProperty ntreisProperty, String s3Url, String type)
    {
        this.ntreisProperty = ntreisProperty;
        this.s3Url = s3Url;
        this.type = type;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public NtreisProperty getNtreisProperty()
    {
        return ntreisProperty;
    }

    public void setNtreisProperty(NtreisProperty ntreisProperty)
    {
        this.ntreisProperty = ntreisProperty;
    }

    public String getS3Url()
    {
        return s3Url;
    }

    public void setS3Url(String s3Url)
    {
        this.s3Url = s3Url;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}
