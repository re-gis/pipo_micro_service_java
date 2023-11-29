package com.manning.bippo.dao.converter;

import java.util.ArrayList;
import java.util.Arrays;

import javax.persistence.AttributeConverter;

import com.manning.bippo.commons.LogUtil;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

public class LongListConverter implements AttributeConverter<List<Long>, String> {

    @Override
    public String convertToDatabaseColumn(List<Long> ids) {
        if (ids == null) {
            return "";
        }
        return ids.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    public List<Long> convertToEntityAttribute(String stored) {
        if (StringUtils.isBlank(stored)) {
            return Collections.emptyList();
        }
        return Arrays.stream(stored.split(",")).map(Long::parseLong).collect(Collectors.toList());
    }
}
