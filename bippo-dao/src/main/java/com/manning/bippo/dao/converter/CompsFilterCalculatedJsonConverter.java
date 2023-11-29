package com.manning.bippo.dao.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.commons.dto.CompsFilterCalculated;

import javax.persistence.AttributeConverter;
import java.io.IOException;

public class CompsFilterCalculatedJsonConverter implements AttributeConverter<CompsFilterCalculated, String>
{
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(CompsFilterCalculated meta)
    {
        try
        {
            if(meta != null)
            {
                return objectMapper.writeValueAsString(meta);
            }
        } catch (JsonProcessingException ex)
        {
            LogUtil.error("Error converting to JSON String.", ex);
        }
        return null;
    }

    @Override
    public CompsFilterCalculated convertToEntityAttribute(String dbData)
    {
        try
        {
            if(dbData != null)
            {
                return objectMapper.readValue(dbData, CompsFilterCalculated.class);
            }
        } catch (IOException ex)
        {
            LogUtil.error("Error converting to JSON String.", ex);
        }
        return null;
    }
}
