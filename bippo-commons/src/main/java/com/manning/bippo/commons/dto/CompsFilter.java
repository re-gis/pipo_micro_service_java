package com.manning.bippo.commons.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CompsFilter
{
    public Double proximityInMiles = 1d;
    public Integer sqftTotalWithIn = 20;
    public Integer yearBuiltWithIn = 10;
    public String status = "Sold";
    public Integer statusChangeInDays = 180;

}
