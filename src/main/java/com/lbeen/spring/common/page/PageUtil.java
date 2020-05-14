package com.lbeen.spring.common.page;


import java.util.List;
import java.util.function.Supplier;

public class PageUtil {
    public static Page getPage(Supplier<Integer> totalFun, Supplier<List<?>> listFun) {
        Integer total = totalFun.get();
        if (total == null || total < 1) {
            return new Page();
        }
        return new Page(total, listFun.get());
    }
}
