package com.json4orm.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.json4orm.exception.Json4ormException;
import com.json4orm.model.query.Filter;
import com.json4orm.model.query.Query;
import com.json4orm.model.query.Result;
import com.json4orm.util.Constants;

public class QueryParser {
    private static final ObjectMapper OBJ_MAPPER;
    static {
        OBJ_MAPPER = new ObjectMapper();
        OBJ_MAPPER.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }
    
    public Query parse(String queryString) throws Json4ormException {
        Query query = new Query();
        Map<String, Object> jsonMap = null;
        try {
            jsonMap = OBJ_MAPPER.readValue(queryString,
                    new TypeReference<Map<String,Object>>(){});
        } catch (JsonProcessingException e) {
            throw new Json4ormException(e);
        }
        
        //get query target
        String queryFor =(String) jsonMap.get(Constants.QUERY);
        if(StringUtils.isBlank(queryFor)) {
            throw new Json4ormException("No query specified.");
        }
        query.setQueryFor(queryFor);
        query.setFilter(generateFilter(jsonMap.get(Constants.FILTER)));
        query.setResult(generateResult(jsonMap.get(Constants.RESULT), null));
        return query;
    }

    private Result generateResult(Object object, Result parent) throws Json4ormException{
        if(object == null || object instanceof Map) {
            throw new Json4ormException("No result or invalid result specified.");
        }
        
        Map<String, Object> resultMap = (Map<String, Object>)object;
        if(resultMap.size()>1) {
            throw new Json4ormException("Mare than one objects specified for result.");
        }
        
        Result result = new Result();
        Map.Entry<String, Object> topEntry = resultMap.entrySet().iterator().next();
        result.setEntity(topEntry.getKey());
        if(topEntry.getValue() == null || topEntry.getValue() instanceof Map) {
            throw new Json4ormException("No result or invalid result specified: "+topEntry.getKey());
        }
        Map<String, Object> valueMap = (Map<String, Object>)topEntry.getValue();
        for(Map.Entry<String, Object> entry: valueMap.entrySet()) {
          if(Constants.PROPERTIES.equalsIgnoreCase(entry.getKey())){
            List<String> properties = (List<String>) valueMap.get(Constants.PROPERTIES);
            if(properties!=null) {
              result.setProperties(properties); 
            }
            
          }
        }
        
        
        
        Map<String, Object> resultMap = (Map<String, Object>)object;
        result.setProperties(properties);
        
        for(Map.Entry<String, Object> entry: resultMap.entrySet()) {
            if(! (entry.getValue() instanceof List)) {
                throw new Json4ormException("Property list is required for result."+entry.getKey());
            }
            Result result=new Result();
            result.setEntity(entry.getKey());
            result.setProperties((List<String>) entry.getValue());
            results.add(result);
        }
        
        return result;
    }

    private Filter generateFilter(Object object) throws Json4ormException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
}
