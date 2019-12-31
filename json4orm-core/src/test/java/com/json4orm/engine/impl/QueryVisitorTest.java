package com.json4orm.engine.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.json4orm.engine.QueryContext;
import com.json4orm.exception.Json4ormException;
import com.json4orm.factory.impl.FileSystemSchemaFactory;
import com.json4orm.model.query.Query;
import com.json4orm.model.schema.Schema;
import com.json4orm.parser.QueryParser;

public class QueryVisitorTest {
    @Test
    public void testQueryVisitor()
            throws JsonParseException, JsonMappingException, IOException, Json4ormException, URISyntaxException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        final InputStream in = this.getClass().getClassLoader().getResourceAsStream("query/class-query.json");
        final QueryParser parser = new QueryParser();
        final Query q;
        try {
            q = parser.parse(in);
        } finally {
            in.close();
        }
        final URL url = this.getClass().getClassLoader().getResource("entities");
        final File folder = new File(url.toURI());
        final FileSystemSchemaFactory schemaFactory = new FileSystemSchemaFactory(folder);
        final Schema schema = schemaFactory.createSchema();

        final QueryVisitor visitor = new QueryVisitor(schema);
        visitor.setConvertor(new ValueConvertorImpl());
        final QueryContext result = visitor.visit(q);

        System.out.println(result.getSql());
        int index = 1;
        for (final Object v : result.getValues()) {
            System.out.println((index++) + ": " + v.toString());
        }
    }
}
