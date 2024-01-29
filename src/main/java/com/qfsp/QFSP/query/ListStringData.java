package com.qfsp.QFSP.query;

import java.util.List;

public class ListStringData extends QueryData {
private List<String> value;

    public ListStringData() {
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }
}
