package com.lbeen.spring.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class MapUtil {
    public static void putIfNotBlank(Map<String, Object> map, String key, String value){
        if (StringUtils.isNotBlank(value)) {
            map.put(key, value);
        }
    }
}
