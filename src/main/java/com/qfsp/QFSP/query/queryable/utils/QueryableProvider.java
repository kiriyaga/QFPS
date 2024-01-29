package com.qfsp.QFSP.query.queryable.utils;

import com.qfsp.QFSP.query.queryable.Queryable;
import com.qfsp.QFSP.query.queryable.QueryableSpec;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class QueryableProvider {
    public static List<Queryable> generateQueryables(Class<?> clazz, String methodName) {
        try {
            Method method = clazz.getDeclaredMethod(methodName);
            return generateQueryables(method);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Cannot find method to generate Queryables", e);
        }
    }

    public static List<Queryable> generateQueryables(Method method) {
        return Arrays.stream(method.getParameters())
                .filter(p -> p.isAnnotationPresent(QueryableSpec.class))
                .map(s -> {
                    var annotation = s.getAnnotation(QueryableSpec.class);
                    return new Queryable(annotation.field(),
                            Arrays.stream(annotation.queryTypes()).collect(Collectors.toList()),
                            Arrays.stream(annotation.sortTypes()).collect(Collectors.toList())
                    );
                })
                .collect(Collectors.toList());
    }
}

