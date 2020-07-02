package com.json4orm.engine.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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

public class QueryBuilderImplTest {
    @Test
    public void testBuildQuery()
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

        final QueryBuilderImpl builder = new QueryBuilderImpl(schema);
        builder.setConvertor(new ValueConvertorImpl());
        final QueryContext result = builder.build(q);

        System.out.println(result.getSearchSql());
        int index = 1;
        for (final Object v : result.getValues()) {
            System.out.println((index++) + ": " + v.toString());
        }
    }
    
    @Test
    public void testBuildAddOrUpdate()
            throws JsonParseException, JsonMappingException, IOException, Json4ormException, URISyntaxException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        final InputStream in = this.getClass().getClassLoader().getResourceAsStream("query/class-add-update.json");
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

        final QueryBuilderImpl builder = new QueryBuilderImpl(schema);
        builder.setConvertor(new ValueConvertorImpl());
        final QueryContext result = builder.build(q);

        System.out.println("------------ Insert ------------");
        System.out.println(result.getInsertSql());
        
        for (final List<Object> v : result.getInsertRecords()) {
            System.out.println(StringUtils.join(v,", "));
        }
        
        System.out.println("------------ Update ------------");
        System.out.println(result.getUpdateSql());

        for (final List<Object> v : result.getUpdateRecords()) {
            System.out.println(StringUtils.join(v,", "));
        }
    }
}
