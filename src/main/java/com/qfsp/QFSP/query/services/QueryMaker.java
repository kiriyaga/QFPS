package com.qfsp.QFSP.query.services;

import com.qfsp.QFSP.query.QueryData;
import com.qfsp.QFSP.query.model.Query;
import com.qfsp.QFSP.query.model.QueryType;

import java.util.Map;

public abstract class QueryMaker<T extends QueryData, M, O extends QueryOperation<T, M>> {

    public abstract Map<QueryType, O> operations();

    public QueryMaker() {}

    public M getOperation(Query<T> query) {

        var operation =  operations().get(query.getQueryType());

        if(operation == null) {
            throw new IllegalStateException("No operation found for query: " + query);
        }

        return operation.makeQuery(query);
    }
}