package com.qfsp.QFSP.query.services;

import com.qfsp.QFSP.query.model.Sortable;

import java.util.List;

public interface QuerySorter <P> {
    P sort(P query, List<Sortable> sortableList);
}
