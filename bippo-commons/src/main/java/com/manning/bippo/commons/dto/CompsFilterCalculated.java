package com.manning.bippo.commons.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString(callSuper = true)
public class CompsFilterCalculated extends CompsFilter
{
    private Integer sqftTotalFrom;
    private Integer sqftTotalTo;
    private Integer yearBuiltFrom;
    private Integer yearBuiltTo;
    private List<String> propertySubType;
    private String propertyType;
    private Integer stories;
    private Boolean pool;
    public String region;
    public transient boolean failed;
    public transient boolean primed;
    public SearchResultsRecord resultsDetails;

    public CompsFilterCalculated()
    {
    }

    public CompsFilterCalculated(CompsFilter compsFilter)
    {
        this.proximityInMiles = compsFilter.proximityInMiles;
        this.sqftTotalWithIn = compsFilter.sqftTotalWithIn;
        this.yearBuiltWithIn = compsFilter.yearBuiltWithIn;
        this.status = compsFilter.status;
        this.statusChangeInDays = compsFilter.statusChangeInDays;
    }

    public void setYearBuilt(Integer yearBuilt)
    {
        if (yearBuilt != null) {
            yearBuiltFrom = yearBuilt - yearBuiltWithIn;
            yearBuiltTo = yearBuilt + yearBuiltWithIn;
        }
    }

    public void setSqftTotal(Float sqftTotal)
    {
        if (sqftTotal != null && sqftTotal > 0) {
            sqftTotalFrom = Math.round(sqftTotal - (sqftTotal * sqftTotalWithIn / 100));
            sqftTotalTo = Math.round(sqftTotal + (sqftTotal * sqftTotalWithIn / 100));
        }
    }

    public Integer getSqftTotalFrom()
    {
        return sqftTotalFrom;
    }

    public void setSqftTotalFrom(Integer sqftTotalFrom)
    {
        this.sqftTotalFrom = sqftTotalFrom;
    }

    public Integer getSqftTotalTo()
    {
        return sqftTotalTo;
    }

    public void setSqftTotalTo(Integer sqftTotalTo)
    {
        this.sqftTotalTo = sqftTotalTo;
    }

    public Integer getYearBuiltFrom()
    {
        return yearBuiltFrom;
    }

    public void setYearBuiltFrom(Integer yearBuiltFrom)
    {
        this.yearBuiltFrom = yearBuiltFrom;
    }

    public Integer getYearBuiltTo()
    {
        return yearBuiltTo;
    }

    public void setYearBuiltTo(Integer yearBuiltTo)
    {
        this.yearBuiltTo = yearBuiltTo;
    }

    public List<String> getPropertySubType()
    {
        return propertySubType;
    }

    public void setPropertySubType(List<String> propertySubType)
    {
        this.propertySubType = propertySubType;
    }

    public String getPropertyType()
    {
        return propertyType;
    }

    public void setPropertyType(String propertyType)
    {
        this.propertyType = propertyType;
    }

    public Integer getStories()
    {
        return this.stories;
    }

    public void setStories(Integer stories)
    {
        this.stories = stories;
    }

    public Boolean getPool()
    {
        return this.pool;
    }

    public void setPool(Boolean pool)
    {
        this.pool = pool;
    }
}
