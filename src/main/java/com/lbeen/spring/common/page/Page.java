package com.lbeen.spring.common.page;

import java.util.Collections;
import java.util.List;

public class Page {
    private int total;
    private List<?> list;

    public Page() {
        this.total = 0;
        this.list = Collections.emptyList();
    }

    public Page(int total, List<?> list) {
        this.total = total;
        if (list == null) {
            this.list = Collections.emptyList();
        } else {
            this.list = list;
        }
    }

    public int getTotal() {
        return total;
    }

    public List<?> getList() {
        return list;
    }
}
