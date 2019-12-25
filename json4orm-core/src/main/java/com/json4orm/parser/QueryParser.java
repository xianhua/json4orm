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
import com.json4orm.model.query.FilterOperator;
import com.json4orm.model.query.Query;
import com.json4orm.model.query.Result;
import com.json4orm.util.Constants;
import com.json4orm.util.EngineUtil;

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
            jsonMap = OBJ_MAPPER.readValue(queryString, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            throw new Json4ormException(e);
        }

        // get query target
        String queryFor = (String) jsonMap.get(Constants.QUERY);
        if (StringUtils.isBlank(queryFor)) {
            throw new Json4ormException("No query specified.");
        }
        query.setQueryFor(queryFor);
        query.setFilter(generateFilter(jsonMap.get(Constants.FILTER)));
        query.setResult(generateResult(jsonMap.get(Constants.RESULT), null));
        return query;
    }

    private Result generateResult(Object object, Result parent) throws Json4ormException {
        if (object == null) {
            return null;
        }
        if (!(object instanceof Map)) {
            throw new Json4ormException("Invalid result specified.");
        }

        Map<String, Object> resultMap = (Map<String, Object>) object;
        if (resultMap.size() > 1) {
            throw new Json4ormException("Mare than one objects specified for result.");
        }

        Map.Entry<String, Object> topEntry = resultMap.entrySet().iterator().next();
        Result result = generateResult(topEntry, topEntry.getKey(), null);
        return result;
    }

    private Filter generateFilter(Object object) throws Json4ormException {
        Filter filter = null;
        if (object == null) {
            return null;
        }

        if (!(object instanceof Map)) {
            throw new Json4ormException("Invalid filter specified.");
        }

        Map<String, Object> filterMap = (Map<String, Object>) object;
        if (filterMap.size() == 0) {
            throw new Json4ormException("Invalid filter specified.");
        }

        Filter parent = null;
        if (filterMap.size() > 1
                || (filterMap.size() == 1 && !EngineUtil.isLogicOperator(filterMap.keySet().iterator().next()))) {
            parent = new Filter();
            parent.setOperator(FilterOperator.AND);
        }

        for (Map.Entry<String, Object> childEntry : filterMap.entrySet()) {
            filter = generateFilter(childEntry, parent);
        }

        if (parent != null) {
            return parent;
        }

        return filter;
    }

    private Filter generateFilter(Map.Entry<String, Object> entry, Filter parent) throws Json4ormException {
        Filter filter = new Filter();
        if (entry.getValue() instanceof List) {
            if (EngineUtil.isLogicOperator(entry.getKey())) {
                filter.setOperator(entry.getKey());
                for (Object child : (List) entry.getValue()) {
                    if (!(child instanceof Map)) {
                        throw new Json4ormException("Invalid filter object specified for: " + entry.getKey());
                    }
                    for (Map.Entry<String, Object> childEntry : ((Map<String, Object>) child).entrySet()) {
                        Filter childFilter = generateFilter(childEntry, filter);
                    }
                }
            } else {
                filter.setOperator(FilterOperator.IN);
                filter.setProperty(entry.getKey());
                filter.setValue(entry.getValue());
            }
        } else if (entry.getValue() instanceof String || entry.getValue() instanceof Number
                || entry.getValue() instanceof Boolean) {
            filter.setOperator(FilterOperator.EQUAL_CASE_INSENSITIVE);
            filter.setProperty(entry.getKey());
            filter.setValue(entry.getValue());
        } else if (entry.getValue() instanceof Map) {
            filter.setProperty(entry.getKey());
            Map<String, Object> valueMap = (Map<String, Object>) entry.getValue();
            if (valueMap.size() > 1) {
                throw new Json4ormException("More than one operators specified for: " + entry.getKey());
            }
            if (valueMap.size() == 0) {
                throw new Json4ormException("No operator specified for: " + entry.getKey());
            }
            Map.Entry<String, Object> optEntry = valueMap.entrySet().iterator().next();

            filter.setOperator(optEntry.getKey());
            filter.setProperty(entry.getKey());
            filter.setValue(entry.getValue());
        }

        if (filter != null && parent != null) {
            parent.addFilter(filter);
        }

        return filter;
    }

    private Result generateResult(Map.Entry<String, Object> entry, String entity, Result parent)
            throws Json4ormException {
        Result result = new Result();
        if (Constants.PROPERTIES.equalsIgnoreCase(entry.getKey())) {
            if (StringUtils.isBlank(entity)) {
                throw new Json4ormException("Entity is not specifies for properties.");
            }
            result.setEntity(entity);
            List<String> properties = (List<String>) entry.getValue();
            if (properties != null) {
                result.setProperties(properties);
            }
        } else {
            if (entry.getValue() instanceof List) {
                result.setEntity(entry.getKey());
                result.setProperties((List<String>) entry.getValue());
            } else if (entry.getValue() instanceof Map) {
                result.setEntity(entry.getKey());
                Map<String, Object> valueMap = (Map<String, Object>) entry.getValue();
                for (Map.Entry<String, Object> childEntry : valueMap.entrySet()) {
                    generateResult(childEntry, entry.getKey(), result);
                }
            }
        }
        if (result != null && parent != null) {
            parent.addAssociate(result);
        }

        return result;
    }

}
