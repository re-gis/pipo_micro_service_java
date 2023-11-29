package com.manning.bippo.service.oi.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DetailWithSchoolsResponse
{

    public Status status;
    public Property[] property;

    @JsonCreator
    public DetailWithSchoolsResponse(@JsonProperty("status") Status status, @JsonProperty("property") Property[] property)
    {
        this.status = status;
        this.property = property;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Status
    {
        public String version;
        public Integer code;
        public String msg;
        public Integer total;
        public long page;

        @JsonCreator
        public Status(@JsonProperty("version") String version, @JsonProperty("code") Integer code,
                      @JsonProperty("msg") String msg, @JsonProperty("total") Integer total, @JsonProperty("page") long page)
        {
            this.version = version;
            this.code = code;
            this.msg = msg;
            this.total = total;
            this.page = page;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Property
    {
        public SchoolDistrict schoolDistrict;
        public School[] school;

        @JsonCreator
        public Property(@JsonProperty(value = "schoolDistrict") SchoolDistrict schoolDistrict, @JsonProperty(value = "school") School[] school)
        {
            this.schoolDistrict = schoolDistrict;
            this.school = school;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class SchoolDistrict
        {
            public String obDistrictNumber;
            public String districtType;
            public String districtName;
            public double districtLatitude;
            public double districtLongitude;

            @JsonCreator
            public SchoolDistrict(@JsonProperty("Obdistrictnumber") String obdistrictnumber, @JsonProperty("districttype") String districttype,
                                  @JsonProperty("districtname") String districtname, @JsonProperty("districtlatitude") double districtlatitude,
                                  @JsonProperty("districtlongitude") double districtlongitude)
            {
                this.obDistrictNumber = obdistrictnumber;
                this.districtType = districttype;
                this.districtName = districtname;
                this.districtLatitude = districtlatitude;
                this.districtLongitude = districtlongitude;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class School
        {
            public int obInstID;
            public String institutionName;
            public int gsTestRating;
            public String gradelevel1lotext;
            public String gradelevel1hitext;
            public String filetypetext;
            public double latitude;
            public double longitude;
            public float distance;

            public School(@JsonProperty("OBInstID") int obInstID, @JsonProperty("InstitutionName") String institutionName,
                          @JsonProperty("GSTestRating") int gsTestRating, @JsonProperty("gradelevel1lotext") String gradelevel1lotext,
                          @JsonProperty("gradelevel1hitext") String gradelevel1hitext, @JsonProperty("Filetypetext") String filetypetext,
                          @JsonProperty("geocodinglatitude") double geocodinglatitude, @JsonProperty("geocodinglongitude") double geocodinglongitude,
                          @JsonProperty("distance") float distance)
            {
                this.obInstID = obInstID;
                this.institutionName = institutionName;
                this.gsTestRating = gsTestRating;
                this.gradelevel1lotext = gradelevel1lotext;
                this.gradelevel1hitext = gradelevel1hitext;
                this.filetypetext = filetypetext;
                this.latitude = geocodinglatitude;
                this.longitude = geocodinglongitude;
                this.distance = distance;
            }
        }
    }
}
