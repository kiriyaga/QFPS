package com.qfsp.QFSP.query.services;

import com.qfsp.QFSP.PageData;

import java.util.List;

public interface QueryGrouper<P, O> {
    P group(List<O> operations, PageData pageData);
}
