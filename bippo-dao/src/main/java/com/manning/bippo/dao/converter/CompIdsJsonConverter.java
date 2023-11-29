package com.manning.bippo.dao.converter;

import com.manning.bippo.commons.LogUtil;

import javax.persistence.AttributeConverter;
import java.util.Arrays;

public class CompIdsJsonConverter implements AttributeConverter<int[], String> {

    public String convertToDatabaseColumn(int[] ids) {
        return Arrays.toString(ids);
    }

    public int[] convertToEntityAttribute(String stored) {
        if (stored == null || "null".equalsIgnoreCase(stored) || stored.length() < 3) {
            return null;
        }

        final String[] ids;

        try {
            ids = stored.substring(stored.indexOf('[') + 1, stored.indexOf(']')).split("\\s*,\\s*");
        } catch (Exception ex) {
            LogUtil.error("Error converting array string.", ex);
            return null;
        }

        final int[] parsedIds = new int[ids.length];

        try {
            for (int i = 0; i < ids.length; i++) {
                parsedIds[i] = Integer.parseInt(ids[i]);
            }
        } catch (NumberFormatException e) {
            LogUtil.error("Error parsing int array value.", e);
            return null;
        }

        return parsedIds;
    }
}
