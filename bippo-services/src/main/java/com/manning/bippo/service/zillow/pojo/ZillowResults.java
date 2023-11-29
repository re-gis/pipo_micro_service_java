package com.manning.bippo.service.zillow.pojo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "results")

public class ZillowResults {

    @JacksonXmlProperty(localName = "result")
    @JacksonXmlElementWrapper(useWrapping = false)
    public ZillowResult[] results;
}
