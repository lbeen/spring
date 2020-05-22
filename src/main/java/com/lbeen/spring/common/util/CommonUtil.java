package com.lbeen.spring.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonUtil {
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
