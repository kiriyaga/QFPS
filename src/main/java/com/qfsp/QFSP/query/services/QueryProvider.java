package com.qfsp.QFSP.query.services;

import com.qfsp.QFSP.PageData;
import com.qfsp.QFSP.query.model.Sortable;

import java.util.List;

public abstract class QueryProvider<P,O> implements QueryGrouper<P, O>, QuerySorter<P>, QueryPageArranger<P> {
    protected GroupType groupType;

    public QueryProvider() {
    }

    public QueryProvider(GroupType groupType) {
        this.groupType = groupType;
    }

    public P generateQuery(List<O> operations, PageData pageData, List<Sortable> sortableList) {
        P query = group(operations, pageData);
        query = pages(query, pageData);
        return sort(query, sortableList);
    }

    public GroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(GroupType groupType) {
        this.groupType = groupType;
    }

    protected enum GroupType {
        AND, OR
    }
}
