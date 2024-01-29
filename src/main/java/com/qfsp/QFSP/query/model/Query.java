package com.qfsp.QFSP.query.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.qfsp.QFSP.query.ListStringData;
import com.qfsp.QFSP.query.QueryData;
import com.qfsp.QFSP.query.StringData;

public class Query<T extends QueryData> {
    private QueryType queryType;
    private SortType sortType = SortType.NONE;
    private String field;

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = StringData.class),
            @JsonSubTypes.Type(value = ListStringData.class)})
    private T data;

    public Query() {
    }

    public Query(QueryType queryType, String field, T data, SortType sortType) {
        this.queryType = queryType;
        this.sortType = sortType;
        this.field = field;
        this.data = data;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }

    public SortType getSortType() {
        return sortType;
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
