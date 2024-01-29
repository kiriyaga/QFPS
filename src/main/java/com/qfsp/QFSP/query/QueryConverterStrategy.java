package com.qfsp.QFSP.query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qfsp.QFSP.query.model.Queries;
import com.qfsp.QFSP.query.model.Query;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class QueryConverterStrategy implements Converter<String, Queries> {

    private ObjectMapper mapper = new ObjectMapper();


    @Override
    public Queries convert(String source) {
        Queries queries = new Queries();
        try {
            var q = mapper.readValue(source, Query[].class);
            Arrays.stream(q).forEach(queries::add);
            return queries;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
