package com.manning.bippo.service.zillow.pojo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import com.manning.bippo.dao.pojo.ZillowProperty;
import java.util.Date;

public class ZillowResult {

    @JacksonXmlProperty(localName = "zpid")
    public int zpid;
    @JacksonXmlProperty(localName = "FIPScounty")
    public String fips;
    @JacksonXmlProperty(localName = "useCode")
    public String propertyType;
    @JacksonXmlProperty(localName = "taxAssessmentYear")
    public int taxYear;
    @JacksonXmlProperty(localName = "taxAssessment")
    public double taxAssessment;
    @JacksonXmlProperty(localName = "yearBuilt")
    public int yearBuilt;
    @JacksonXmlProperty(localName = "lotSizeSqFt")
    public int lotSqft;
    @JacksonXmlProperty(localName = "finishedSqFt")
    public int finishedSqft;
    @JacksonXmlProperty(localName = "bathrooms")
    public float bathrooms;
    @JacksonXmlProperty(localName = "bedrooms")
    public int bedrooms;
    @JacksonXmlProperty(localName = "totalRooms")
    public int totalRooms;
    @JacksonXmlProperty(localName = "links")
    public Links links;
    @JacksonXmlProperty(localName = "address")
    public Address address;
    @JacksonXmlProperty(localName = "zestimate")
    public Zestimate zestimate;
    
    public ZillowProperty toNewZillowProperty() {
        final ZillowProperty zp = new ZillowProperty();
        zp.setZillowId(this.zpid);
        zp.setFipsCode(this.fips);
        zp.setPropertyType(this.propertyType);
        zp.setTaxYear(this.taxYear);
        zp.setTaxAssessment(this.taxAssessment);
        zp.setYearBuilt(this.yearBuilt);
        zp.setLotFootage(this.lotSqft);
        zp.setFinishedFootage(this.finishedSqft);
        zp.setBathrooms(this.bathrooms);
        zp.setBedrooms(this.bedrooms);
        zp.setTotalRooms(this.totalRooms);
        
        if (this.links != null) {
            zp.setLinkToDetails(this.links.details);
            zp.setLinkToGraphs(this.links.graphs);
            zp.setLinkToMap(this.links.mapthis);
            zp.setLinkToComps(this.links.comps);
        }
        
        if (this.address != null) {
            zp.setFirstLine(this.address.firstLine);
            zp.setZipCode(this.address.zipCode);
            zp.setCity(this.address.city);
            zp.setState(this.address.state);
            zp.setLatitude(this.address.latitude);
            zp.setLongitude(this.address.longitude);
        }
        
        if (this.zestimate != null) {
            if (this.zestimate.amount != null) {
                zp.setZestimate(this.zestimate.amount.value);
            }
            
            if (this.zestimate.range != null) {
                if (this.zestimate.range.low != null) {
                    zp.setZestimateLow(this.zestimate.range.low.value);
                }
                
                if (this.zestimate.range.high != null) {
                    zp.setZestimateHigh(this.zestimate.range.high.value);
                }
            }
        }
        
        zp.setCreationDate(new Date());
        return zp;
    }
    
    public static class Links {
    
        @JacksonXmlProperty(localName = "homedetails")
        public String details;
        @JacksonXmlProperty(localName = "graphsanddata")
        public String graphs;
        @JacksonXmlProperty(localName = "mapthishome")
        public String mapthis;
        @JacksonXmlProperty(localName = "comparables")
        public String comps;
    }
    
    public static class Address {
    
        @JacksonXmlProperty(localName = "street")
        public String firstLine;
        @JacksonXmlProperty(localName = "zipcode")
        public String zipCode;
        @JacksonXmlProperty(localName = "city")
        public String city;
        @JacksonXmlProperty(localName = "state")
        public String state;
        @JacksonXmlProperty(localName = "latitude")
        public double latitude;
        @JacksonXmlProperty(localName = "longitude")
        public double longitude;
    }
    
    public static class Zestimate {
    
        @JacksonXmlProperty(localName = "amount")
        public Zvalue amount;
        @JacksonXmlProperty(localName = "valuationRange")
        public Zrange range;
    }
    
    public static class Zrange {
    
        @JacksonXmlProperty(localName = "high")
        public Zvalue high;
        @JacksonXmlProperty(localName = "low")
        public Zvalue low;
    }
    
    public static class Zvalue {
    
        @JacksonXmlProperty(isAttribute = true, localName = "currency")
        public String currency;
        @JacksonXmlText(true)
        public double value;
    }
}
