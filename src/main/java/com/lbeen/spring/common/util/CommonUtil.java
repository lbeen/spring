package com.lbeen.spring.common.util;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CommonUtil {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String tosString(Object obj) {
        return obj == null ? null : obj.toString();
    }

    public static int toInt(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Integer) {
            return (int) obj;
        }
        if (obj instanceof String) {
            String str = obj.toString();
            return str.isEmpty() ? 0 : Integer.parseInt(obj.toString());
        }
        if (obj instanceof Long) {
            return Integer.parseInt(obj.toString());
        }
        return 0;
    }

    public static long toLong(Object obj) {
        if (obj == null) {
            return 0L;
        }
        if (obj instanceof Long) {
            return (long) obj;
        }
        if (obj instanceof String) {
            String str = obj.toString();
            return str.isEmpty() ? 0L : Long.parseLong(obj.toString());
        }
        if (obj instanceof Integer) {
            return Long.parseLong(obj.toString());
        }
        return 0L;
    }

    public static double toDouble(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Double) {
            return (double) obj;
        }
        if (obj instanceof String) {
            String str = obj.toString();
            return str.isEmpty() ? 0 : Double.parseDouble(obj.toString());
        }
        if (obj instanceof Long || obj instanceof Integer) {
            return Double.parseDouble(obj.toString());
        }
        return 0;
    }

    public static void putIfNotBlank(Map<String, Object> map, String key, String value) {
        if (StringUtils.isNotBlank(value)) {
            map.put(key, value);
        }
    }

    private static Document map2Document(Map<String, Object> map) {
        Document document = new Document();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            document.put(entry.getKey(), entry.getValue());
        }
        return document;
    }

    public static List<Document> mapList2Documents(List<Map<String, Object>> mapList) {
        List<Document> documents = new ArrayList<>();
        if (CollectionUtils.isEmpty(mapList)) {
            return documents;
        }
        for (Map<String, Object> map : mapList) {
            documents.add(map2Document(map));
        }
        return documents;
    }
}
