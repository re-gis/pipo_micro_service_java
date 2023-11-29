package com.manning.bippo.dao.itf;

import java.util.Date;

public interface AbstractProperty extends Addressable {

    @Override
    public String getAddress();
    public Long getId();
    public boolean hasValidAddress();
    public boolean hasStandardizableAddress();
    public String getStreetNumber();
    public String getStreetName();
    public String getStreetDirPrefix();
    public String getStreetDirSuffix();
    public String getCity();
    public String getPostalCode();
    public String getPostalCodePlus4();
    public String getStreetSuffix();
    public String getStateOrProvince();
    public String getParcelNumber();

    // Multiple NtreisProperty and TrestleProperty methods differ in which type of numeric value they return, so we use Number to address this
    public Number getLongitude();
    public Number getLatitude();
    public Number getListPrice();
    public Number getClosePrice();
    public Number getSqFtTotal();
    public Integer getYearBuilt();
    public Date getStatusChangeTimestamp();
    public String getSubdivisionName();
    public Number getNumberOfStories();
    public Integer getBathsHalf();
    public Integer getBathsFull();
    public Integer getBedsTotal();
    public Integer getParkingSpacesCarport();
    public Integer getParkingSpacesGarage();
    public String getPoolYN();
    public String getStatus();
    public String getPropertyType();
    public String getPropertySubType();


    public static int compareByListPriceAsc(AbstractProperty l, AbstractProperty r)
    {
        return Float.compare(l.getListPrice().intValue(), r.getListPrice().intValue());
    }

    public static int compareByListPriceDesc(AbstractProperty l, AbstractProperty r)
    {
        return Float.compare(r.getListPrice().intValue(), l.getListPrice().intValue());
    }

    public static int compareByClosePriceAsc(AbstractProperty l, AbstractProperty r)
    {
        return Float.compare(l.getClosePrice().intValue(), r.getClosePrice().intValue());
    }

    public static int compareByClosePriceDesc(AbstractProperty l, AbstractProperty r)
    {
        return Float.compare(r.getClosePrice().intValue(), l.getClosePrice().intValue());
    }

    public static int compareBySqFtTotalAsc(AbstractProperty l, AbstractProperty r)
    {
        return Integer.compare(l.getSqFtTotal().intValue(), r.getSqFtTotal().intValue());
    }
}
