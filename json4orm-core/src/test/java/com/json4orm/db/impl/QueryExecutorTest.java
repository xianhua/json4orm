package com.json4orm.db.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.json4orm.db.QueryResult;
import com.json4orm.exception.Json4ormException;
import com.json4orm.factory.impl.FileSystemSchemaFactory;
import com.json4orm.model.query.Action;
import com.json4orm.model.query.Filter;
import com.json4orm.model.query.FilterOperator;
import com.json4orm.model.query.Pagination;
import com.json4orm.model.query.Query;
import com.json4orm.model.query.Result;
import com.json4orm.model.query.SortBy;
import com.json4orm.model.schema.Schema;
import com.json4orm.parser.QueryParser;

public class QueryExecutorTest {
    @Test
    public void testExecute() throws Json4ormException, URISyntaxException, IOException {
        final QueryExecutorImpl executor = new QueryExecutorImpl();
        executor.setDbUser("");
        executor.setDbPassword("");
        executor.setDbUrl("jdbc:h2:mem:test;MODE=MySQL;DB_CLOSE_ON_EXIT=TRUE;TRACE_LEVEL_SYSTEM_OUT=1;INIT=runscript from 'src/test/resources/scripts/test.sql'");
        final URL url = this.getClass().getClassLoader().getResource("entities");
        final File folder = new File(url.toURI());
        final FileSystemSchemaFactory schemaFactory = new FileSystemSchemaFactory(folder);
        final Schema schema = schemaFactory.createSchema();
        executor.setSchema(schema);

        final InputStream in = this.getClass().getClassLoader().getResourceAsStream("query/student-query.json");
        final QueryParser parser = new QueryParser();
        final Query q;
        try {
            q = parser.parse(in);
        } finally {
            in.close();
        }

        final QueryResult result = executor.execute(q);

        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        System.out.println(mapper.writeValueAsString(result.getRecords()));
    }
    
    @Test
    public void testExecuteWithGeneratedQuery() throws Json4ormException, URISyntaxException, IOException {
        //init query executor
        final QueryExecutorImpl executor = new QueryExecutorImpl();
        executor.setDbUser("");
        executor.setDbPassword("");
        executor.setDbUrl("jdbc:h2:mem:test;MODE=MySQL;DB_CLOSE_ON_EXIT=TRUE;TRACE_LEVEL_SYSTEM_OUT=1;INIT=runscript from 'src/test/resources/scripts/test.sql'");
        
        // create schema by reading json mapping files from entities folder
        final URL url = this.getClass().getClassLoader().getResource("entities");
        final File folder = new File(url.toURI());
        final FileSystemSchemaFactory schemaFactory = new FileSystemSchemaFactory(folder);
        final Schema schema = schemaFactory.createSchema();
        executor.setSchema(schema);
        
        //create query
        final Query q = new Query();
        q.setAction(Action.SEARCH);
        q.setEntityName("Student");
        
        //set pagination starting from 0 and retrieve 5 records
        final Pagination pagination=new Pagination();
        pagination.setOffset(0);
        pagination.setLimit(5);
        q.setPagination(pagination);
        
        //set sorting
        final SortBy sortByFirstName= new SortBy();
        sortByFirstName.setProperty("firstName");
        sortByFirstName.setOrder("DESC");
        q.setSortBy(Collections.singletonList(sortByFirstName));
        
        // set filter: firstName starts with 'a' AND lastName ends with 'n'
        final Filter filter = new Filter();
        filter.setOperator(FilterOperator.AND);
        final Filter firstNameFilter = new Filter();
        firstNameFilter.setProperty("firstName");
        firstNameFilter.setOperator(FilterOperator.STARTS_WITH_CASE_INSENSITIVE);
        firstNameFilter.setValue("a");
        
        final Filter lastNameFilter = new Filter();
        lastNameFilter.setProperty("lastName");
        lastNameFilter.setOperator(FilterOperator.ENDS_WITH_CASE_INSENSITIVE);
        lastNameFilter.setValue("n");
        
        filter.addFilter(firstNameFilter);
        filter.addFilter(lastNameFilter);
        q.setFilter(filter);
        
        // set result: return studentId, firstName, lastName
        final Result result = new Result();
        result.setEntity("Student");
        final List<String> properties= new ArrayList<>();
        properties.add("studentId");
        properties.add("firstName");
        properties.add("lastName");
        result.setProperties(properties);
        q.setResult(result);
        
        final QueryResult queryResult = executor.execute(q);
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        System.out.println(mapper.writeValueAsString(queryResult.getRecords()));
    }
    
}