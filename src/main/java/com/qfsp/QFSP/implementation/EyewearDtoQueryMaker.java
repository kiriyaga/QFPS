package com.qfsp.QFSP.implementation;


import com.qfsp.QFSP.query.model.QueryType;
import com.qfsp.QFSP.query.mongo.MongoQueryOperation;
import com.qfsp.QFSP.query.mongo.operations.IsStringOperation;
import com.qfsp.QFSP.query.services.QueryMaker;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Map;

public class EyewearDtoQueryMaker extends QueryMaker<Criteria, MongoQueryOperation<?>> {
    @Override
    public Map<QueryType, MongoQueryOperation> operations() {
        return Map.of(
                QueryType.IS, new IsStringOperation()
        );
    }
}
