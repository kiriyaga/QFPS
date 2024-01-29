package com.qfsp.QFSP.query.model;

import org.springframework.data.domain.Sort;

public class Sortable {
    private String field;
    private SortType sortType;

    public Sortable() {
    }

    public Sortable(String field, SortType sortType) {
        this.field = field;
        this.sortType = sortType;
    }

    public String getField() {
        return field;
    }

    public SortType getSortType() {
        return sortType;
    }

    public Sort.Order toOrder() {
        return new Sort.Order(typeToDirection(), field);
    }

    public Sort.Direction typeToDirection() {
        return sortType == SortType.ASC ? Sort.Direction.ASC : Sort.Direction.DESC;
    }
}
