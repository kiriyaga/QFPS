package com.qfsp.QFSP.query.services;


import com.qfsp.QFSP.PageData;

public interface QueryPageArranger<P> {
    P pages(P query, PageData pageData);
}
