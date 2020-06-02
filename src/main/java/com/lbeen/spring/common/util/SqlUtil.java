package com.lbeen.spring.common.util;

import com.lbeen.spring.common.bean.Page;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class SqlUtil {
    public static Page queryPage(Function<Map<String, Object>, Integer> totalFun, Function<Map<String, Object>,
            List<?>> listFun, Map<String, Object> param, Integer skip, Integer limit) {
        Page page = new Page(skip, limit);

        param.put("skip", page.getSkip());
        param.put("limit", page.getLimit());

        int total = totalFun.apply(param);
        if (total == 0) {
            return page.empty();
        }
        return page.setTotal(total).setList(listFun.apply(param));
    }
}
