package com.json4orm.parser;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.json4orm.exception.Json4ormException;
import com.json4orm.model.query.Query;
import com.json4orm.model.query.Result;

public class QueryParserTest {

    public QueryParserTest() {
    }

    @Test
    public void testQuery() throws Json4ormException, JsonParseException, JsonMappingException, IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        final InputStream in = this.getClass().getClassLoader().getResourceAsStream("query/class-query.json");

        final QueryParser parser = new QueryParser();

        try {
            final Query q = parser.parse(in);
            printResult(q.getResult());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (final IOException e) {

                }
            }
        }
    }

    private void printResult(final Result r) {
        System.out.println(r.getEntity());

        for (final String p : r.getProperties()) {
            System.out.println("\t" + p);
        }

        if (r.getAssociates() != null) {
            for (final Result cld : r.getAssociates()) {
                printResult(cld);
            }
        }
        System.out.println("---------------------------");
    }

}
