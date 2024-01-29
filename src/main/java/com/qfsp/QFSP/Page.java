package com.qfsp.QFSP;

import java.util.List;

public class Page<T> {
    private PageData pageData;
    private List<T> data;

    public Page() {
    }

    public Page(PageData pageData, List<T> data) {
        this.pageData = pageData;
        this.data = data;
    }

    public PageData getPageData() {
        return pageData;
    }

    public void setPageData(PageData pageData) {
        this.pageData = pageData;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
