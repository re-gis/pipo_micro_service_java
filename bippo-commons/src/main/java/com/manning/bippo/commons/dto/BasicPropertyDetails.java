package com.manning.bippo.commons.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class BasicPropertyDetails implements Serializable
{
    Float longitude;
    Float latitude;

    Float livingSize;

    Integer yearBuiltEffective;

    String addressLine1;
    String addressLine2;
    String postalCode;
    String apn;

    String propertyType;
    String propertySubType;

    String mlsRegion;
    String mlsStatus;
    String mlsPropertyType;
    String mlsPropertySubType;

    Long addressSemanticsId;
    private Integer bathsFull;
    private Integer bathsHalf;
    private Integer bedsTotal;
    private Integer parkingSpacesCarport;
    private Integer parkingSpacesGarage;
    private Boolean pool;
    private Float listPrice;
    private Boolean conflictingTaxData;
    private Long id;

    public String getAddress()
    {
        return addressLine1 + ", " + addressLine2;
    }
}