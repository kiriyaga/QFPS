package com.qfsp.QFSP.query.mongo.operations;


import com.qfsp.QFSP.query.StringData;
import com.qfsp.QFSP.query.model.Query;
import com.qfsp.QFSP.query.mongo.MongoQueryOperation;
import org.springframework.data.mongodb.core.query.Criteria;

public class IsStringOperation implements MongoQueryOperation<StringData> {

    @Override
    public Criteria makeQuery(Query<StringData> query) {
        return null;
    }
}