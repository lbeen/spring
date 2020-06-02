package com.lbeen.spring.common.bean;

import java.util.Collections;
import java.util.List;

public class Page {
    private Integer skip;
    private Integer limit;
    private long total;
    private List<?> list;

    public Page(Integer skip, Integer limit) {
        if (skip == null || skip < 0) {
            this.skip = 0;
        } else {
            this.skip = skip;
        }
        if (limit == null || limit < 0) {
            this.limit = 10;
        } else {
            this.limit = limit;
        }
    }

    public Page empty() {
        this.total = 0;
        this.list = Collections.EMPTY_LIST;
        return this;
    }

    public Integer getSkip() {
        return skip;
    }

    public Integer getLimit() {
        return limit;
    }

    public long getTotal() {
        return total;
    }

    public Page setTotal(long total) {
        this.total = total;
        return this;
    }

    public List<?> getList() {
        return list;
    }

    public Page setList(List<?> list) {
        this.list = list;
        return this;
    }
}
