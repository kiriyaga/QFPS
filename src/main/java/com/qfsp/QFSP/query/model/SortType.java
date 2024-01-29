package com.qfsp.QFSP.query.model;

public enum SortType {
    NONE(0), ASC(-1), DESC(1);

    private int value;

    SortType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
