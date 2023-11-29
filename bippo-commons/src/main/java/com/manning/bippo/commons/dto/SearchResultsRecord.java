package com.manning.bippo.commons.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResultsRecord
{
    public int total, passed, date, status, subtype, year, gla, pool, stories;

    public SearchResultsRecord() {
    }

    public SearchResultsRecord(int total, int passed, int date, int status, int subtype, int year, int gla, int pool, int stories)
    {
        this.total = total;
        this.passed = passed;
        this.date = date;
        this.status = status;
        this.subtype = subtype;
        this.year = year;
        this.gla = gla;
        this.pool = pool;
        this.stories = stories;
    }
}
