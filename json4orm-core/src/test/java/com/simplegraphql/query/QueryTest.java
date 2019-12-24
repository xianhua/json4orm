package com.json4orm.query;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.json4orm.model.query.Query;
import com.json4orm.model.query.Result;

public class QueryTest {

    public QueryTest() {
    }

    @Test
    public void testQuery() throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        String queryFile =
                "C:\\Users\\xliu1002\\workspace\\simple-graphql\\json4orm-core\\src\\main\\resources\\query\\User-Query.json";
        Query q = mapper.readValue(new File(queryFile), Query.class);

        System.out.println(q.getQueryFor());
        printResult(q.getResult());
    }

    private void printResult(Result r) {
        System.out.println(r.getEntity());
        for (String p : r.getProperties()) {
            System.out.println("\t" + p);
        }

        if (r.getAssociates() != null) {
            for (Result cld : r.getAssociates()) {
                printResult(cld);
            }
        }
        System.out.println("---------------------------");
    }

}
