package com.json4orm.db;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.json4orm.db.impl.QueryExecutorImpl;
import com.json4orm.exception.Json4ormException;
import com.json4orm.factory.impl.FileSystemSchemaFactory;
import com.json4orm.model.entity.Schema;
import com.json4orm.model.query.Query;
import com.json4orm.parser.QueryParser;

public class QueryExecutorTest {
    @Test
    public void testExecute() throws Json4ormException, URISyntaxException, IOException {
        final QueryExecutorImpl executor = new QueryExecutorImpl();
        executor.setDbUser("postgres");
        executor.setDbPassword("L*");
        executor.setDbUrl("jdbc:postgresql://localhost:5432/postgres");
        final URL url = this.getClass().getClassLoader().getResource("entities");
        final File folder = new File(url.toURI());
        final FileSystemSchemaFactory schemaFactory = new FileSystemSchemaFactory(folder);
        final Schema schema = schemaFactory.createSchema();
        executor.setSchema(schema);

        final InputStream in = this.getClass().getClassLoader().getResourceAsStream("query/class-query.json");
        final QueryParser parser = new QueryParser();
        final Query q;
        try {
            q = parser.parse(in);
        } finally {
            in.close();
        }

        final List<Map<String, Object>> records = executor.execute(q);

        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        System.out.println(mapper.writeValueAsString(records));
    }
}
