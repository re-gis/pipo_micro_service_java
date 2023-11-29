package com.manning.bippo.commons.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class PropertySearchFilter implements Serializable
{
    List<String> propertyType;
    List<String> propertySubType;
    List<String> mlsStatus;
    List<String> andWords;
    List<String> notWords;
    String mlsRegion;
    Integer numberOfStories;
    Float priceRangeFrom;
    Float priceRangeTo;
    List<Integer> zipCodes;
    String city;
    String county;
    Float sqftFrom;
    Float sqftTo;
    Float lotFrom;
    Float lotTo;
    String address;
    List<String> schoolDistrict;
    Float listPriceFrom;
    Float listPriceTo;
    Integer hitArvFrom;
    Integer hitArvTo;
    Integer hitAsIsFrom;
    Integer hitAsIsTo;
    Integer hitWholesaleFrom;
    Integer hitWholesaleTo;
    Integer domFrom;
    Integer domTo;
    Integer cdomFrom;
    Integer cdomTo;
    Boolean pool;
    Integer bedsFrom;
    Integer bedsTo;
    Integer bathsFrom;
    Integer bathsTo;
    Integer halfBathsFrom;
    Integer halfBathsTo;
    Integer garageBaysFrom;
    Integer garageBaysTo;
    Integer carportBaysFrom;
    Integer carportBaysTo;
    Boolean hud;
    String condition;
    String conditionConfidenceScoreFrom;
    String conditionConfidenceScoreTo;
    Integer taxValueFrom;
    Integer taxValueTo;
    Integer hoaFrom;
    Integer hoaTo;
    Date statusChangeTimestampFrom;
    Date statusChangeTimestampTo;
    Integer livingAreasFrom;
    Integer livingAreasTo;
    Integer diningAreasFrom;
    Integer diningAreasTo;
    List<Long> propertyIds;
    private List<String> notInMLSStatus;
}
