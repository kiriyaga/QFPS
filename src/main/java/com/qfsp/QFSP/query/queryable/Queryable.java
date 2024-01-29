package com.qfsp.QFSP.query.queryable;

import com.qfsp.QFSP.query.model.Query;
import com.qfsp.QFSP.query.model.QueryType;
import com.qfsp.QFSP.query.model.SortType;

import java.util.List;

public class Queryable {
    private String fieldName;
    private List<QueryType> queryTypes;
    private List<SortType> sortTypes;

    public Queryable(String fieldName, List<QueryType> types, List<SortType> sortTypes) {
        this.fieldName = fieldName;
        this.queryTypes = types;
        this.sortTypes = sortTypes;
    }
    public Queryable() {
    }

    public String getFieldName() {
        return fieldName;
    }

    public boolean isApplicable(Query<?> query) {
        return queryTypes.contains(query.getQueryType())
                && sortTypes.contains(query.getSortType())
                && fieldName.equals(query.getField());
    }

    public List<QueryType> getQueryTypes() {
        return queryTypes;
    }

    public List<SortType> getSortTypes() {
        return sortTypes;
    }
}
