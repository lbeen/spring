package com.lbeen.spring.common.page;

public class PageBean {
    private int begin = 1;
    private int end = 1;

    public void setPagePage(Integer pageSize, Integer currentPage) {
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        if (currentPage == null || currentPage < 1) {
            currentPage = 1;
        }
        this.end = currentPage * pageSize;
        this.begin = this.end - pageSize;

    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
