package com.manning.bippo.service.oi.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/*
 * TODO: This class and AreaFullPropertyResponse can be abstracted to a common base, as can their
 * component classes (Status doesn't change, InputParameter can be abstract, Result can be generic)
 */
public class AddressPoiPropertyResponse
{
    public Response response;

    @JsonCreator
    public AddressPoiPropertyResponse(@JsonProperty("response") Response response)
    {
        this.response = response;
    }

    public AddressPoiPropertyResponse()
    {}

    public static class Response
    {
        public InputParameter inputparameter;
        public Result result;
        public Status status;

        @JsonCreator
        public Response(@JsonProperty("inputparameter") InputParameter ipar,
                      @JsonProperty("result") Result result,
                      @JsonProperty("status") Status status,
                      @JsonProperty("xmlns") String xmlns)
        {
            this.inputparameter = ipar;
            this.result = result;
            this.status = status;
        }
    }

    public static class InputParameter
    {
        public String pkg, resource, service, limit, sort, distance, address;

        @JsonCreator
        public InputParameter(@JsonProperty("package") String pkg, @JsonProperty("resource") String resource,
                @JsonProperty("service") String service, @JsonProperty("RecordLimit") String limit,
                @JsonProperty("Sort") String sort, @JsonProperty("SearchDistance") String distance,
                @JsonProperty("StreetAddress") String address)
        {
            this.pkg = pkg;
            this.resource = resource;
            this.service = service;
            this.limit = limit;
            this.sort = sort;
            this.distance = distance;
            this.address = address;
        }
    }

    public static class Result
    {
        public ResultPackage pkg;

        @JsonCreator
        public Result(@JsonProperty("package") ResultPackage pkg, @JsonProperty("xml_record") String xmlrecord)
        {
            this.pkg = pkg;
        }
    }

    public static class ResultPackage
    {
        public ResultItem[] items;
        public String descr, name, notice, resource, service, version;

        @JsonCreator
        public ResultPackage(@JsonProperty("item") ResultItem[] items,
                @JsonProperty("descr") String descr, @JsonProperty("name") String name,
                @JsonProperty("notice") String notice, @JsonProperty("resource") String resource,
                @JsonProperty("service") String service, @JsonProperty("version") String version)
        {
            this.items = items;
            this.descr = descr;
            this.name = name;
            this.notice = notice;
            this.resource = resource;
            this.service = service;
            this.version = version;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResultItem
    {

        public String business, city, distance, franchise, latitude, longitude, geomatch, industry, lob, name, onboardId, phone, primary, state, street, type, unit, zipcode;

        public ResultItem(@JsonProperty("business_category") String business, @JsonProperty("city") String city,
                @JsonProperty("distance") String distance, @JsonProperty("franchise") String franchise,
                @JsonProperty("geo_latitude") String latitude, @JsonProperty("geo_longitude") String longitude,
                @JsonProperty("geo_match") String geomatch, @JsonProperty("industry") String industry,
                @JsonProperty("lob") String lob, @JsonProperty("name") String name,
                @JsonProperty("ob_id") String onboardId, @JsonProperty("phone") String phone,
                @JsonProperty("primary") String primary, @JsonProperty("state") String state,
                @JsonProperty("street") String street, @JsonProperty("type") String type,
                @JsonProperty("unit") String unit, @JsonProperty("zip_code") String zipcode) {
            this.business = business;
            this.city = city;
            this.distance = distance;
            this.franchise = franchise;
            this.latitude = latitude;
            this.longitude = longitude;
            this.geomatch = geomatch;
            this.industry = industry;
            this.lob = lob;
            this.name = name;
            this.onboardId = onboardId;
            this.phone = phone;
            this.primary = primary;
            this.state = state;
            this.street = street;
            this.type = type;
            this.unit = unit;
            this.zipcode = zipcode;
        }
    }

    public static class Status
    {
        public String code, longdescription, shortdescription;

        @JsonCreator
        public Status(@JsonProperty("code") String code,
                @JsonProperty("long_description") String longd, @JsonProperty("short_description") String shortd)
        {
            this.code = code;
            this.longdescription = longd;
            this.shortdescription = shortd;
        }
    }
}
