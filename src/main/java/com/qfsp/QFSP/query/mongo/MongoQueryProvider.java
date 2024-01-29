package com.qfsp.QFSP.query.mongo;

import com.qfsp.QFSP.PageData;
import com.qfsp.QFSP.query.model.SortType;
import com.qfsp.QFSP.query.model.Sortable;
import com.qfsp.QFSP.query.services.QueryProvider;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.stream.Collectors;

public class MongoQueryProvider extends QueryProvider<Query, Criteria> {
    @Override
    public Query group(List<Criteria> operations, PageData pageData) {
        Query query = new Query();

        if(groupType == GroupType.AND) {
            query.addCriteria(new Criteria()
                    .andOperator(operations
                            .toArray(new Criteria[operations.size()])
                    )
            );
        } else if(groupType == GroupType.OR) {
            query.addCriteria(new Criteria()
                    .orOperator(operations
                            .toArray(new Criteria[operations.size()])
                    )
            );
        } else {
            operations.forEach(query::addCriteria);
        }

        return query;
    }

    @Override
    public Query sort(Query query, List<Sortable> sortableList) {
        if(sortableList != null) {
            var orders = sortableList
                    .stream()
                    .filter(s -> s.getSortType() != SortType.NONE)
                    .map(Sortable::toOrder)
                    .collect(Collectors.toList());
            query.with(Sort.by(orders));
        }
        return query;
    }

    @Override
    public Query pages(Query query, PageData pageData) {
        if(pageData != null) {
            query.skip((long) (pageData.getPage() - 1) * pageData.getSize());
            query.limit(pageData.getSize());
        }

        return query;
    }
}
