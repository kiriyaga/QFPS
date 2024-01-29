package com.qfsp.QFSP.query.queryable;


import com.qfsp.QFSP.query.model.QueryType;
import com.qfsp.QFSP.query.model.SortType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface QueryableSpec {
    String serviceName();
    String field();
    QueryType[] queryTypes() default {
            QueryType.IS,
            QueryType.IS_EMPTY,
            QueryType.IS_NOT_EMPTY,
            QueryType.NOT_EQUALS_TO,
            QueryType.CONTAINS,
            QueryType.ONE_OF
    };

    SortType[] sortTypes() default {
            SortType.NONE,
            SortType.ASC,
            SortType.DESC
    };
    boolean withPlaceholder() default false;
}
