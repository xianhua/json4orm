package com.json4orm.engine.impl;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.json4orm.engine.VisitingResult;
import com.json4orm.exception.Json4ormException;
import com.json4orm.factory.impl.FileSystemSchemaFactory;
import com.json4orm.model.entity.Schema;
import com.json4orm.model.query.Query;

public class QueryVisitorTest {
	 @Test
	    public void testQueryVisitor() throws JsonParseException, JsonMappingException, IOException, Json4ormException {
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

	        String queryFile =
	                "C:\\workspace\\simple-graphql\\json4orm-core\\src\\main\\resources\\query\\User-Query.json";
	        Query q = mapper.readValue(new File(queryFile), Query.class);
	        
	        String folder = "C:\\workspace\\simple-graphql\\json4orm-core\\src\\main\\resources\\entities";
	        FileSystemSchemaFactory schemaFactory = new FileSystemSchemaFactory(folder);
	        Schema schema = schemaFactory.createSchema();
	        
	        QueryVisitor visitor = new QueryVisitor(schema);
	        VisitingResult result = visitor.visit(q);
	        
	        System.out.println(result.getSql());
	        for(String key: result.getValues().keySet()) {
	        	System.out.println(key+"="+result.getValues().get(key));
	        }
	    }
}
