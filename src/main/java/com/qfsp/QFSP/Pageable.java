package com.qfsp.QFSP;


import com.qfsp.QFSP.query.model.Query;

import java.util.*;

public class Pageable {
    private PageData pageData;

    List<Query<?>> queries = new ArrayList<>();

    public Pageable() {
    }

    public Pageable(PageData pageData, List<Query<?>> queries) {
        this.pageData = pageData;
        this.queries = queries;
    }

    public PageData getPageData() {
        return pageData;
    }

    public void setPageData(PageData pageData) {
        this.pageData = pageData;
    }

    public List<Query<?>> getQueries() {
        return queries;
    }

    public void setQueries(List<Query<?>> queries) {
        this.queries = queries;
    }
}
