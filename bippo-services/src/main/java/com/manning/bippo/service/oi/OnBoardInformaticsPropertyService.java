package com.manning.bippo.service.oi;

import com.manning.bippo.dao.itf.AbstractProperty;
import com.manning.bippo.dao.pojo.AddressSemantics;
import com.manning.bippo.dao.pojo.NtreisProperty;
import com.manning.bippo.dao.pojo.OnBoardInformaticsResponse;
import com.manning.bippo.service.oi.pojo.AddressPoiPropertyResponse;
import com.manning.bippo.service.oi.pojo.AllEventsPropertyResponse;
import com.manning.bippo.service.oi.pojo.AnalyticsMultiResponse;
import com.manning.bippo.service.oi.pojo.AnalyticsResponse;
import com.manning.bippo.service.oi.pojo.AreaFullPropertyResponse;
import com.manning.bippo.service.oi.pojo.DetailWithSchoolsResponse;
import com.manning.bippo.service.oi.pojo.MortgageOwnerPropertyResponse;
import com.manning.bippo.service.oi.pojo.OIPropertySearchFilter;
import com.manning.bippo.service.oi.pojo.SnapshotResponse;

public interface OnBoardInformaticsPropertyService
{
    SnapshotResponse getAllEvents(OIPropertySearchFilter filter, int page, int size);

    AllEventsPropertyResponse getAllEvents(String firstLine, String secondLine);
    String getAllEventsRaw(String apn, String fips, String primaryNumber, String streetName, String cityName);
    String getAllEventsRaw(String firstLine, String secondLine);

    AreaFullPropertyResponse getAreaFull(String areacode);
    AreaFullPropertyResponse getAreaFull(int zipcode);
    String getAreaFullRaw(String areacode);

    AnalyticsResponse getAnalytics(String areacode, int year);
    AnalyticsResponse getAnalytics(int zipcode, int year);
    String getAnalyticsRaw(String areacode, int year);

    String getAddressPoiRaw(String address);

    String getMortgageOwnerRaw(long attomId);

    String getDetailWithSchoolsRaw(long attomId);

    OnBoardInformaticsResponse getOnBoardInformaticsResponse(String firstLine, String secondLine);
    OnBoardInformaticsResponse getOnBoardInformaticsResponse(String apn, String fips, String primaryNumber, String streetName, String cityName);
    OnBoardInformaticsResponse findById(String obPropId);

    AllEventsPropertyResponse findByNtreisProperty(NtreisProperty ntreisProperty, boolean queueIfNotPresent);
    AllEventsPropertyResponse findByAbstractProperty(AbstractProperty abstractProperty, boolean queueIfNotPresent);
    AllEventsPropertyResponse findByAddressSemantics(AddressSemantics addressSemantics, boolean allowOldData);

    OnBoardInformaticsResponse downloadAllEventsPropertyData(AddressSemantics property);

    String downloadAllEventsPropertyData(NtreisProperty ntreisProperty);

    String redownloadAllEventsPropertyData(NtreisProperty ntreisProperty);
    OnBoardInformaticsResponse redownloadAllEventsPropertyData(AddressSemantics property);

    String downloadAreaFullPropertyData(int zipcode);
    AreaFullPropertyResponse downloadAreaFullPropertyResponse(int zipcode);

    String downloadAddressPoiPropertyData(String address);
    AddressPoiPropertyResponse downloadAddressPoiPropertyResponse(String address);

    String downloadMortgageOwnerPropertyData(long attomId);
    MortgageOwnerPropertyResponse downloadMortgageOwnerPropertyResponse(long attomId);

    AnalyticsMultiResponse downloadAnalyticsResponse(int zipcode);

    AllEventsPropertyResponse convertJSONToAllEventsResponse(String response);

    DetailWithSchoolsResponse downloadDetailWithSchoolsResponse(AllEventsPropertyResponse resp);
    DetailWithSchoolsResponse downloadDetailWithSchoolsResponse(long attomId);
}
