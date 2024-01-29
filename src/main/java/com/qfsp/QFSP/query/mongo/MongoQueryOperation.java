package com.qfsp.QFSP.query.mongo;

import com.qfsp.QFSP.query.QueryData;
import com.qfsp.QFSP.query.model.Query;
import com.qfsp.QFSP.query.services.QueryOperation;
import org.springframework.data.mongodb.core.query.Criteria;

public interface MongoQueryOperation<T extends QueryData> extends QueryOperation<T, Criteria> {
    @Override
    Criteria makeQuery(Query<T> query);
}
