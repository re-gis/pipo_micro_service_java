package com.manning.bippo.service.oi.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.beans.Transient;

public class AnalyticsResponse
{

    public Status status;
    public SalesTrends[] salestrends;

    @JsonCreator
    public AnalyticsResponse(@JsonProperty("status") Status status, @JsonProperty("salestrends") SalesTrends[] salestrends)
    {
        this.status = status;
        this.salestrends = salestrends;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Status
    {
        public String version;
        public Integer code;
        public String msg;
        public Integer total;

        @JsonCreator
        public Status(@JsonProperty("version") String version, @JsonProperty("code") Integer code,
                      @JsonProperty("msg") String msg, @JsonProperty("total") Integer total)
        {
            this.version = version;
            this.code = code;
            this.msg = msg;
            this.total = total;
        }
    }

    public static class SalesTrends
    {
        public Location location;
        public DateRange daterange;
        public SalesTrend salestrend;
        public Vintage vintage;

        @JsonCreator
        public SalesTrends(@JsonProperty("location") Location location, @JsonProperty("daterange") DateRange daterange,
                        @JsonProperty("SalesTrend") SalesTrend salestrend, @JsonProperty("vintage") Vintage vintage)
        {
            this.location = location;
            this.daterange = daterange;
            this.salestrend = salestrend;
            this.vintage = vintage;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Location
        {
            public String geoid;

            @JsonCreator
            public Location(@JsonProperty("geoid") String geoid)
            {
                this.geoid = geoid;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class DateRange
        {
            public String start;
            public String end;
            public String interval;

            @JsonCreator
            public DateRange(@JsonProperty("start") String start, @JsonProperty("end") String end, @JsonProperty("interval") String interval)
            {
                this.start = start;
                this.end = end;
                this.interval = interval;
            }

            @Transient
            public int getStartQuarter() {
                return Integer.parseInt(this.start.split(" ")[0].replaceAll("\\D", ""));
            }

            @Transient
            public int getStartYear() {
                final String[] split = this.start.split(" ");
                return Integer.parseInt(split[split.length - 1]);
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class SalesTrend
        {
            public int homesalecount;
            public int avgsaleprice;
            public int medsaleprice;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Vintage
        {
            public String pubDate;

            @JsonCreator
            public Vintage(@JsonProperty("pubDate") String pubDate)
            {
                this.pubDate = pubDate;
            }
        }
    }
}
