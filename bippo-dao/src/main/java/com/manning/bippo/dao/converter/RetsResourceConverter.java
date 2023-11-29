package com.manning.bippo.dao.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.dao.rets.pojo.RetsResource;

import javax.persistence.AttributeConverter;
import java.io.IOException;

public class RetsResourceConverter implements AttributeConverter<RetsResource, String>
{
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(RetsResource meta)
    {
        try
        {
            return objectMapper.writeValueAsString(meta);
        } catch (JsonProcessingException ex)
        {
            LogUtil.error("Error converting to JSON String.", ex);
            return null;
        }
    }

    @Override
    public RetsResource convertToEntityAttribute(String dbData)
    {
        try
        {
            return objectMapper.readValue(dbData, RetsResource.class);
        } catch (IOException | NullPointerException ex)
        {
            LogUtil.error("Error converting to JSON String.", ex);
            return null;
        }
    }

}
