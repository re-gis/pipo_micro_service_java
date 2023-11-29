package com.manning.bippo.dao.pojo;

import javax.persistence.*;

@Entity
@Table(name = "trestle_photo")
public class TrestlePhoto
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "listing_key")
    Integer listingKey;

    @Column(name = "trestle_url")
    String trestleUrl;

    @Column(name = "s3_url")
    String s3Url;

    public TrestlePhoto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getListingKey() {
        return listingKey;
    }

    public void setListingKey(Integer listingKey) {
        this.listingKey = listingKey;
    }

    public String getTrestleUrl() {
        return trestleUrl;
    }

    public void setTrestleUrl(String trestleUrl) {
        this.trestleUrl = trestleUrl;
    }

    public String getS3Url() {
        return s3Url;
    }

    public void setS3Url(String s3Url) {
        this.s3Url = s3Url;
    }
}
