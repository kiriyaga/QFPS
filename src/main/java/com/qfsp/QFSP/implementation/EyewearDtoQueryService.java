package com.qfsp.QFSP.implementation;

import com.qfsp.QFSP.query.QueryData;
import com.qfsp.QFSP.query.mongo.MongoQueryOperation;
import com.qfsp.QFSP.query.mongo.MongoQueryProvider;
import com.qfsp.QFSP.query.services.QueryService;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.lang.reflect.Method;
import java.util.Arrays;

public class EyewearDtoQueryService<T extends QueryData> extends QueryService<T, Criteria, MongoQueryOperation<T>, Query> {

    public EyewearDtoQueryService(EyewearDtoQueryMaker queryMaker, MongoQueryProvider queryProvider) {
        super(queryMaker, queryProvider);
    }

    @Override
    public String serviceName() {
        return "dam_eyewearDto_index";
    }

    @Override
    public Method queryMethod() {
        return Arrays.stream(EyewearController.class.getDeclaredMethods()).filter(m -> m.getName().equals("indexPage")).findFirst().orElseThrow();
    }
}
