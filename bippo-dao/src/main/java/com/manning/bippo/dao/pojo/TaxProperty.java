package com.manning.bippo.dao.pojo;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "new_property")
public class TaxProperty
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MLSid")
    private Long mlsId;

    @Column(name = "Matrix_Unique_ID")
    private String matrixUniqueId;

    @Column(name = "Source")
    private String source;

    @Column(name = "SaleType")
    private String saleType;

    @Column(name = "County")
    private String county;

    @Column(name = "State")
    private String state;

    @Column(name = "Zip")
    private Integer zip;

    @Column(name = "FullAddress")
    private String fullAddress;

    @Column(name = "SqFt")
    private Long sqFt;

    @Column(name = "Beds")
    private Integer beds;

    @Column(name = "Baths")
    private Integer baths;

    @Column(name = "Lotsize")
    private Integer lotSize;

    @Column(name = "YearBuilt")
    private Integer yearBuilt;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getCounty()
    {
        return county;
    }

    public void setCounty(String county)
    {
        this.county = county;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public Integer getZip()
    {
        return zip;
    }

    public void setZip(Integer zip)
    {
        this.zip = zip;
    }

    public String getFullAddress()
    {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress)
    {
        this.fullAddress = fullAddress;
    }

    public Long getSqFt()
    {
        return sqFt;
    }

    public void setSqFt(Long sqFt)
    {
        this.sqFt = sqFt;
    }

    public Integer getBeds()
    {
        return beds;
    }

    public void setBeds(Integer beds)
    {
        this.beds = beds;
    }

    public Integer getBaths()
    {
        return baths;
    }

    public void setBaths(Integer baths)
    {
        this.baths = baths;
    }

    public Integer getLotSize()
    {
        return lotSize;
    }

    public void setLotSize(Integer lotSize)
    {
        this.lotSize = lotSize;
    }

    public Integer getYearBuilt()
    {
        return yearBuilt;
    }

    public void setYearBuilt(Integer yearBuilt)
    {
        this.yearBuilt = yearBuilt;
    }
}
