package com.manning.bippo.service.utils;

import com.manning.bippo.commons.LogUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class BippoUtils
{
    public static String getMLSNumber(String subjectId)
    {
        return splitToMap(subjectId).get("mlsNumber");
    }

    public static String getOnBoardId(String subjectId)
    {
        return splitToMap(subjectId).get("obPropId");
    }

    public static String getAddressSemanticsPropId(String subjectId)
    {
        return splitToMap(subjectId).get("as");
    }

    public static String getTrestleId(String subjectId)
    {
        return splitToMap(subjectId).get("tr");
    }

    private static Map<String, String> splitToMap(String subjectId)
    {
        try {
            return Arrays.stream(subjectId.split("-")).collect(Collectors.toMap(s -> s.split(":")[0], s -> s.split(":")[1]));
        } catch (RuntimeException e) {
            LogUtil.error("Could not split bippoId: '" + subjectId + "'", e);
            return Collections.emptyMap();
        }
    }
}
