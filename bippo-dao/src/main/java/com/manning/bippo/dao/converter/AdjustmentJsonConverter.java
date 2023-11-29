package com.manning.bippo.dao.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manning.bippo.commons.LogUtil;
import com.manning.bippo.dao.pojo.Adjustment;

import javax.persistence.AttributeConverter;
import java.io.IOException;

public class AdjustmentJsonConverter implements AttributeConverter<Adjustment, String>
{
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Adjustment meta)
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
    public Adjustment convertToEntityAttribute(String dbData)
    {
        try
        {
            return objectMapper.readValue(dbData, Adjustment.class);
        } catch (IOException ex)
        {
            LogUtil.error("Error converting to JSON String.", ex);
            return null;
        }
    }

}
