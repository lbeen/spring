package com.lbeen.spring.common.util;

import com.lbeen.spring.common.bean.Page;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class SqlUtil {
    public static Page queryPage(Function<Map<String, Object>, Integer> totalFun, Function<Map<String, Object>,
            List<?>> listFun, Map<String, Object> param, Integer skip, Integer limit) {

        if (skip == null || skip < 0) {
            skip = 0;
        }
        if (limit == null || limit < 0) {
            limit = 10;
        }
        param.put("skip", skip);
        param.put("limit", limit);

        Page page = new Page();
        page.setSkip(skip);
        page.setLimit(limit);


        Integer total = totalFun.apply(param);
        if (total == null || total == 0) {
            page.setTotal(0);
            page.setList(Collections.emptyList());
            return page;
        }
        page.setTotal(total);

        List<?> list = listFun.apply(param);
        if (list == null) {
            list = Collections.EMPTY_LIST;
        }
        page.setList(list);
        return page;
    }
}
