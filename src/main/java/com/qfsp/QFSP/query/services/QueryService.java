package com.qfsp.QFSP.query.services;

import com.qfsp.QFSP.PageData;
import com.qfsp.QFSP.Pageable;
import com.qfsp.QFSP.query.QueryData;
import com.qfsp.QFSP.query.model.Query;
import com.qfsp.QFSP.query.model.Sortable;
import com.qfsp.QFSP.query.queryable.Queryable;
import com.qfsp.QFSP.query.queryable.utils.QueryableProvider;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public abstract class QueryService<T extends QueryData, O, M extends QueryOperation<T, O>, P> {
    private final QueryMaker<T, O, M> queryMaker;
    private final QueryProvider<P, O> queryProvider;
    private List<Queryable> queryables;

    public QueryService(QueryMaker<T, O, M> queryMaker,
                        QueryProvider<P, O> queryProvider) {
        this.queryMaker = queryMaker;
        this.queryProvider = queryProvider;
        this.queryables = QueryableProvider.generateQueryables(queryMethod());
    }

    public abstract String serviceName();
    public abstract Method queryMethod();

    public List<Query<T>> transform(List<Query<T>> queries) {
        return queries
                .stream()
                .peek(query -> {
                    var queryable = queryables
                            .stream().anyMatch(q -> q.isApplicable(query));

                    if(!queryable) {
                        throw new IllegalStateException("No queryable found for query: " + query);
                    }
                })
                .collect(Collectors.toList());
    }

    public P generateQueries(List<Query<T>> queries, PageData pageData) {
        return generateQueries(new Pageable(pageData, queries));
    }

    public P generateQueries(Pageable pageable) {
        var queries = transform(pageable.getQueries());
        return queryProvider
                .generateQuery(queries
                        .stream()
                        .map(queryMaker::getOperation)
                        .collect(Collectors.toList()),
                        pageable.getPageData(),
                        generateSortable(queries)
                );
    }

    public P generateQueries(List<Query<?>> queries) {
        return generateQueries(queries, null);
    }

    public List<Sortable> generateSortable(List<Query<?>> queries) {
        return queries
                .stream()
                .map(q -> new Sortable(q.getField(), q.getSortType()))
                .collect(Collectors.toList());
    }
}
