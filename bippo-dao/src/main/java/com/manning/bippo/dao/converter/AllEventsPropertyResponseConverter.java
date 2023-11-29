package com.manning.bippo.dao.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.dao.pojo.Adjustment;
import com.manning.bippo.dao.rets.pojo.RetsResource;
import com.manning.bippo.service.oi.pojo.AllEventsPropertyResponse;

import javax.persistence.AttributeConverter;
import java.io.IOException;

public class AllEventsPropertyResponseConverter implements AttributeConverter<AllEventsPropertyResponse, String>
{
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(AllEventsPropertyResponse attribute)
    {
        try
        {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException ex)
        {
            LogUtil.error("Error converting to JSON String.", ex);
            return null;
        }
    }

    @Override
    public AllEventsPropertyResponse convertToEntityAttribute(String dbData)
    {
        try
        {
            if(dbData != null)
            {
                return objectMapper.readValue(dbData, AllEventsPropertyResponse.class);
            }
        } catch (IOException ex)
        {
            LogUtil.error("Error converting to JSON String.", ex);
        }
        return null;
    }
}
