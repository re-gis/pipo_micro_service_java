package com.manning.bippo.dao.filter;

import com.manning.bippo.dao.pojo.NtreisProperty;

import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.SingularAttribute;

public class  NtreisProperty_ {
    public static String latitude;
    public static String longitude;
    public static String lotSizeAreaSQFT;
    public static SingularAttribute<NtreisProperty, ?> yearBuilt;
    public static SingularAttribute<NtreisProperty, ?> status;
    public static String statusChangeTimestamp;
    public static SingularAttribute<? super NtreisProperty,?> propertyType;
    public static SingularAttribute<NtreisProperty,?> propertySubType;
    public static SingularAttribute<? super NtreisProperty,?> numberOfStories;
    public static SingularAttribute<? super NtreisProperty,?> closePrice;
    public static SingularAttribute<NtreisProperty, ?> countyOrParish;
    public static SingularAttribute<NtreisProperty, ?> city;
    public static SingularAttribute<? super NtreisProperty,?> sqFtTotal;
    public static SingularAttribute<NtreisProperty, ?> listPrice;
    public static SingularAttribute<NtreisProperty, ?> schoolDistrict;
    public static SingularAttribute<NtreisProperty, ?> cDOM;
    public static SingularAttribute<NtreisProperty, ?> dOM;
    public static SingularAttribute<NtreisProperty,?> bathsTotal;
    public static SingularAttribute<NtreisProperty, ?> bedsTotal;
    public static SingularAttribute<? super NtreisProperty,? extends Object> publicRemarks;
    public static SingularAttribute<? super NtreisProperty,?> numberOfDiningAreas;
    public static SingularAttribute<? super NtreisProperty,?> numberOfLivingAreas;
    public static SingularAttribute<? super NtreisProperty,?> associationFee;
    public static SingularAttribute<? super NtreisProperty, ?> postalCode;
    public static char[] bathsHalf;
    public static char[] bathsFull;
    public static char[] parkingSpacesGarage;
    public static char[] parkingSpacesCarport;
    public static SingularAttribute getYearBuilt() {
        return yearBuilt;
    }
}
