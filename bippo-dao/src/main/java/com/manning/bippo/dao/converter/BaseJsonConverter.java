package com.manning.bippo.dao.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manning.bippo.commons.LogUtil;
import java.io.IOException;
import javax.persistence.AttributeConverter;

public class BaseJsonConverter<T> implements AttributeConverter<T, String> {

    private static final ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    
    private final Class<T> cls;
    
    public BaseJsonConverter(Class<T> cls) {
        this.cls = cls;
    }
    
    public String convertToDatabaseColumn(T object) {
        if (object == null) {
            return "NULL";
        } else try {
            return BaseJsonConverter.mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LogUtil.error("Error converting " + this.cls.getSimpleName() + " to JSON String.", e);
            return null;
        }
    }
    
    public T convertToEntityAttribute(String content) {
        if (content == null || "null".equalsIgnoreCase(content)) {
            return null;
        } else try {
            return BaseJsonConverter.mapper.readValue(content, this.cls);
        } catch (IOException e) {
            LogUtil.error("Error converting String to " + this.cls.getSimpleName() + ".", e);
            return null;
        }
    }
}
