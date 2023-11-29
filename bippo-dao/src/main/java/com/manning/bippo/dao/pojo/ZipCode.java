package com.manning.bippo.dao.pojo;

import javax.persistence.*;

@Entity
@Table(name = "zip_code")
public class ZipCode
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "county_id", referencedColumnName = "id")
    County county;

    @OneToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    City city;

    Integer value;

    @Column(name = "location_type")
    String locationType;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public County getCounty()
    {
        return county;
    }

    public void setCounty(County county)
    {
        this.county = county;
    }

    public City getCity()
    {
        return city;
    }

    public void setCity(City city)
    {
        this.city = city;
    }

    public Integer getValue()
    {
        return value;
    }

    public void setValue(Integer value)
    {
        this.value = value;
    }

    public String getLocationType()
    {
        return this.locationType;
    }

    public void setLocationType(String type)
    {
        this.locationType = type;
    }
}
