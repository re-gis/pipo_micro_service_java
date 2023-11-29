package com.manning.bippo.service.oi.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MortgageOwnerPropertyResponse
{

    public Status status;
    public Property[] property;

    @JsonCreator
    public MortgageOwnerPropertyResponse(@JsonProperty("status") Status status, @JsonProperty("property") Property[] property)
    {
        this.status = status;
        this.property = property;
    }

    public MortgageOwnerPropertyResponse()
    {}

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

    public static class Property
    {
        public Identifier identifier;
        public Mortgage mortgage;
        public Owner owner;

        @JsonCreator
        public Property(@JsonProperty("identifier") Identifier identifier, @JsonProperty("mortgage") Mortgage mortgage, @JsonProperty("owner") Owner owner)
        {
            this.identifier = identifier;
            this.mortgage = mortgage;
            this.owner = owner;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Identifier
        {
            public long obPropId;
            public String fips;
            public String apn;
            public String apnOrig;
            public long attomId;

            @JsonCreator
            public Identifier(@JsonProperty("obPropId") long obPropId, @JsonProperty("fips") String fips,
                              @JsonProperty("apn") String apn, @JsonProperty("apnOrig") String apnOrig,
                              @JsonProperty("attomId") long attomId)
            {
                this.obPropId = obPropId;
                this.fips = fips;
                this.apn = apn;
                this.apnOrig = apnOrig;
                this.attomId = attomId;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Mortgage
        {
            public LenderDetails lender;
            public LenderDetails title;
            public float amount;
            public String date;
            public String deedtype;
            public String duedate;
            public String interestratetype;

            @JsonCreator
            public Mortgage(@JsonProperty("lender") LenderDetails lender, @JsonProperty("title") LenderDetails title,
                    @JsonProperty("amount") float amount, @JsonProperty("date") String date,
                    @JsonProperty("deedtype") String deedtype, @JsonProperty("duedate") String duedate,
                    @JsonProperty("interestratetype") String interestratetype)
            {
                this.lender = lender;
                this.title = title;
                this.amount = amount;
                this.date = date;
                this.deedtype = deedtype;
                this.duedate = duedate;
                this.interestratetype = interestratetype;
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class LenderDetails
            {
                public String companyname;
                public String companycode;
                public String lastname;
                public String city;
                public String state;
                public String zip;

                @JsonCreator
                public LenderDetails(@JsonProperty(value = "companyname", required = false) String companyname, @JsonProperty(value = "companycode", required = false) String companycode,
                        @JsonProperty(value = "lastname", required = false) String lastname, @JsonProperty(value = "city", required = false) String city,
                        @JsonProperty(value = "state", required = false) String state, @JsonProperty(value = "zip", required = false) String zip)
                {
                    this.companyname = companyname;
                    this.companycode = companycode;
                    this.lastname = lastname;
                    this.city = city;
                    this.state = state;
                    this.zip = zip;
                }
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Owner
        {
            public OwnerDetails owner1;
            public OwnerDetails owner2;
            public OwnerDetails owner3;
            public OwnerDetails owner4;
            public String corporateindicator;
            public String absenteeownerstatus;
            public String mailingaddressoneline;

            @JsonCreator
            public Owner(@JsonProperty("owner1") OwnerDetails owner1, @JsonProperty("owner2") OwnerDetails owner2,
                    @JsonProperty("owner3") OwnerDetails owner3, @JsonProperty("owner4") OwnerDetails owner4,
                    @JsonProperty("corporateindicator") String corporateindicator, @JsonProperty("absenteeownerstatus") String absenteeownerstatus,
                    @JsonProperty("mailingaddressoneline") String mailingaddressoneline)
            {
                this.owner1 = owner1;
                this.owner2 = owner2;
                this.owner3 = owner3;
                this.owner4 = owner4;
                this.corporateindicator = corporateindicator;
                this.absenteeownerstatus = absenteeownerstatus;
                this.mailingaddressoneline = mailingaddressoneline;
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class OwnerDetails
            {
                public String lastname;
                public String firstnameandmi;

                @JsonCreator
                public OwnerDetails(@JsonProperty("lastname") String lastname, @JsonProperty("firstnameandmi") String firstnameandmi)
                {
                    this.lastname = lastname;
                    this.firstnameandmi = firstnameandmi;
                }
            }
        }
    }
}
