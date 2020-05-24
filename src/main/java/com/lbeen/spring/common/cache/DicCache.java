package com.lbeen.spring.common.cache;

import com.lbeen.spring.common.util.SpringUtil;
import com.lbeen.spring.sys.bean.Dic;
import com.lbeen.spring.sys.service.DicService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class DicCache {
    private static Map<String, Map<String, Dic>> CHILD_CACHE = new HashMap<>();

    static {
        loadCache();
    }

    public static void loadCache() {
        Map<String, Dic> typesCache = new HashMap<>();
        Map<String, Map<String, Dic>> childCache = new HashMap<>();

        DicService dicService = SpringUtil.getBean("dicServiceImpl");
        List<Dic> dics = dicService.selectAllUsedDic();
        if (!CollectionUtils.isEmpty(dics)) {
            Iterator<Dic> it = dics.iterator();
            while (it.hasNext()) {
                Dic dic = it.next();
                if (StringUtils.isBlank(dic.getParentId())) {
                    typesCache.put(dic.getId(), dic);
                    it.remove();
                }
            }

            for (Dic dic : dics) {
                Dic type = typesCache.get(dic.getParentId());
                if (type == null) {
                    continue;
                }
                String typeCode = type.getCode();
                Map<String, Dic> children = childCache.computeIfAbsent(typeCode, k -> new HashMap<>());
                children.put(dic.getCode(), dic);
            }
        }

        CHILD_CACHE = childCache;
    }

    public static Collection<Dic> getDics(String type) {
        if (StringUtils.isBlank(type)) {
            return null;
        }
        Map<String, Dic> children = CHILD_CACHE.get(type);
        if (children == null) {
            return null;
        }
        return children.values();
    }

    public static Dic getDic(String type, String code) {
        if (StringUtils.isBlank(type) || StringUtils.isBlank(code)) {
            return null;
        }
        Map<String, Dic> children = CHILD_CACHE.get(type);
        if (children == null) {
            return null;
        }
        return children.get(code);
    }
}
