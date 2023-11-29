package com.manning.bippo.service.oi.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SnapshotResponse
{
    public final Status status;
    public final Property property[];

    @JsonCreator
    public SnapshotResponse(@JsonProperty("status") Status status, @JsonProperty("property") Property[] property)
    {
        this.status = status;
        this.property = property;
    }

    public Status getStatus()
    {
        return status;
    }

    public Property[] getProperty()
    {
        return property;
    }

    public static final class Status
    {
        public final String version;
        public final Integer code;
        public final String msg;
        public final Integer total;
        public final Integer page;
        public final Integer pagesize;

        @JsonCreator
        public Status(@JsonProperty("version") String version, @JsonProperty("code") Integer code,
                      @JsonProperty("msg") String msg, @JsonProperty("total") Integer total,
                      @JsonProperty("page") Integer page, @JsonProperty("pagesize") Integer pagesize)
        {
            this.version = version;
            this.code = code;
            this.msg = msg;
            this.total = total;
            this.page = page;
            this.pagesize = pagesize;
        }

        public String getVersion()
        {
            return version;
        }

        public Integer getCode()
        {
            return code;
        }

        public String getMsg()
        {
            return msg;
        }

        public Integer getTotal()
        {
            return total;
        }

        public Integer getPage()
        {
            return page;
        }

        public Integer getPagesize()
        {
            return pagesize;
        }
    }

    public static final class Property
    {
        public final Identifier identifier;
        public final Lot lot;
        public final Address address;
        public final Location location;
        public final Summary summary;
        public final Building building;
        public final Vintage vintage;

        @JsonCreator
        public Property(@JsonProperty("identifier") Identifier identifier, @JsonProperty("lot") Lot lot,
                        @JsonProperty("address") Address address, @JsonProperty("location") Location location,
                        @JsonProperty("summary") Summary summary, @JsonProperty("building") Building building,
                        @JsonProperty("vintage") Vintage vintage)
        {
            this.identifier = identifier;
            this.lot = lot;
            this.address = address;
            this.location = location;
            this.summary = summary;
            this.building = building;
            this.vintage = vintage;
        }

        public static final class Identifier
        {
            public final long obPropId;
            public final String fips;
            public final String apn;
            public final String apnOrig;

            @JsonCreator
            public Identifier(@JsonProperty("obPropId") long obPropId, @JsonProperty("fips") String fips,
                              @JsonProperty("apn") String apn, @JsonProperty("apnOrig") String apnOrig)
            {
                this.obPropId = obPropId;
                this.fips = fips;
                this.apn = apn;
                this.apnOrig = apnOrig;
            }
        }

        public static final class Lot
        {
            public final long lotSize1;

            @JsonCreator
            public Lot(@JsonProperty("lotSize1") long lotSize1)
            {
                this.lotSize1 = lotSize1;
            }
        }

        public static final class Address
        {
            public final String country;
            public final String countrySubd;
            public final String line1;
            public final String line2;
            public final String locality;
            public final String matchCode;
            public final String oneLine;
            public final String postal1;
            public final String postal2;
            public final String postal3;

            @JsonCreator
            public Address(@JsonProperty("country") String country, @JsonProperty("countrySubd") String countrySubd,
                           @JsonProperty("line1") String line1, @JsonProperty("line2") String line2,
                           @JsonProperty("locality") String locality, @JsonProperty("matchCode") String matchCode,
                           @JsonProperty("oneLine") String oneLine, @JsonProperty("postal1") String postal1,
                           @JsonProperty("postal2") String postal2, @JsonProperty("postal3") String postal3)
            {
                this.country = country;
                this.countrySubd = countrySubd;
                this.line1 = line1;
                this.line2 = line2;
                this.locality = locality;
                this.matchCode = matchCode;
                this.oneLine = oneLine;
                this.postal1 = postal1;
                this.postal2 = postal2;
                this.postal3 = postal3;
            }
        }

        public static final class Location
        {
            public final String accuracy;
            public final long elevation;
            public final String latitude;
            public final String longitude;
            public final long distance;
            public final String geoid;

            @JsonCreator
            public Location(@JsonProperty("accuracy") String accuracy, @JsonProperty("elevation") long elevation,
                            @JsonProperty("latitude") String latitude, @JsonProperty("longitude") String longitude,
                            @JsonProperty("distance") long distance, @JsonProperty("geoid") String geoid)
            {
                this.accuracy = accuracy;
                this.elevation = elevation;
                this.latitude = latitude;
                this.longitude = longitude;
                this.distance = distance;
                this.geoid = geoid;
            }
        }

        public static final class Summary
        {
            public final String propclass;
            public final String propsubtype;
            public final String proptype;
            public final String propLandUse;

            public final long yearbuilt;

            @JsonCreator
            public Summary(@JsonProperty("propclass") String propclass, @JsonProperty("propsubtype") String propsubtype,
                           @JsonProperty("proptype") String proptype, @JsonProperty("propLandUse") String propLandUse,
                           @JsonProperty("yearbuilt") long yearbuilt)
            {
                this.propclass = propclass;
                this.propsubtype = propsubtype;
                this.proptype = proptype;
                this.propLandUse = propLandUse;
                this.yearbuilt = yearbuilt;
            }
        }

        public static final class Building
        {
            public final Size size;
            public final Rooms rooms;

            @JsonCreator
            public Building(@JsonProperty("size") Size size, @JsonProperty("rooms") Rooms rooms)
            {
                this.size = size;
                this.rooms = rooms;
            }

            public static final class Size
            {
                public final long universalsize;

                @JsonCreator
                public Size(@JsonProperty("universalsize") long universalsize)
                {
                    this.universalsize = universalsize;
                }
            }

            public static final class Rooms
            {
                public final long bathstotal;
                public final long beds;

                @JsonCreator
                public Rooms(@JsonProperty("bathstotal") long bathstotal, @JsonProperty("beds") long beds)
                {
                    this.bathstotal = bathstotal;
                    this.beds = beds;
                }
            }
        }

        public static final class Vintage
        {
            public final String lastModified;
            public final String pubDate;

            @JsonCreator
            public Vintage(@JsonProperty("lastModified") String lastModified, @JsonProperty("pubDate") String pubDate)
            {
                this.lastModified = lastModified;
                this.pubDate = pubDate;
            }
        }
    }
}
