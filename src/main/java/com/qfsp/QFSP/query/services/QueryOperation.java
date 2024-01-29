package com.qfsp.QFSP.query.services;

import com.qfsp.QFSP.query.QueryData;
import com.qfsp.QFSP.query.model.Query;

import java.util.Collection;

public interface QueryOperation<T extends QueryData, Q> {
    Q makeQuery(Query<T> query);

    default boolean isCollection(Class<?> clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    default boolean isBoolean(Class<?> clazz) {
        return clazz == Boolean.class;
    }
}
