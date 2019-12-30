package com.json4orm.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.json4orm.exception.Json4ormException;
import com.json4orm.model.query.Filter;
import com.json4orm.model.query.FilterOperator;
import com.json4orm.model.query.Pagination;
import com.json4orm.model.query.Query;
import com.json4orm.model.query.Result;
import com.json4orm.model.query.SortBy;
import com.json4orm.util.Constants;
import com.json4orm.util.EngineUtil;

public class QueryParser {
    private static final ObjectMapper OBJ_MAPPER;
    static {
        OBJ_MAPPER = new ObjectMapper();
        OBJ_MAPPER.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    public Query parse(final InputStream inputStream) throws Json4ormException {
        Map<String, Object> jsonMap = null;
        try {
            jsonMap = OBJ_MAPPER.readValue(inputStream, new TypeReference<Map<String, Object>>() {
            });
        } catch (final IOException e) {
            throw new Json4ormException(e);
        }
        return parse(jsonMap);
    }

    public Query parse(final File queryFile) throws Json4ormException {
        Map<String, Object> jsonMap = null;
        try {
            jsonMap = OBJ_MAPPER.readValue(queryFile, new TypeReference<Map<String, Object>>() {
            });
        } catch (final IOException e) {
            throw new Json4ormException(e);
        }

        return parse(jsonMap);
    }

    public Query parse(final String queryString) throws Json4ormException {

        Map<String, Object> jsonMap = null;
        try {
            jsonMap = OBJ_MAPPER.readValue(queryString, new TypeReference<Map<String, Object>>() {
            });
        } catch (final JsonProcessingException e) {
            throw new Json4ormException(e);
        }
        return parse(jsonMap);

    }

    private Query parse(final Map<String, Object> jsonMap) throws Json4ormException {
        final Query query = new Query();
        // get query target
        final String queryFor = (String) jsonMap.get(Constants.QUERY);
        if (StringUtils.isBlank(queryFor)) {
            throw new Json4ormException("No query specified.");
        }
        query.setQueryFor(queryFor);
        query.setFilter(generateFilter(jsonMap.get(Constants.FILTER)));
        query.setPagination(generatePagination(jsonMap.get(Constants.PAGINATION)));
        query.setSortBy(generateSortBy(jsonMap.get(Constants.SORT_BY)));
        query.setResult(generateResult(jsonMap.get(Constants.RESULT), null));
        return query;
    }

    private List<SortBy> generateSortBy(final Object object) throws Json4ormException {
        if (object == null) {
            return null;
        }

        final List<SortBy> sortByList = new ArrayList<>();
        if (object instanceof String) {
            // order by single field ascending
            final SortBy sortBy = createSortBy((String) object);
            sortByList.add(sortBy);
        } else if (object instanceof List) {
            for (final Object o : (List<?>) object) {
                if (o instanceof String) {
                    final SortBy sortBy = createSortBy((String) o);
                    sortByList.add(sortBy);
                } else {
                    throw new Json4ormException("Invalid sortBy: '" + o.toString() + "'");
                }
            }
        }

        return sortByList;
    }

    private SortBy createSortBy(final String str) throws Json4ormException {
        final String[] parts = str.trim().split("\\s+");
        if (parts.length == 0 || parts.length > 2) {
            throw new Json4ormException("Invalid sortBy '" + str + "'");
        }

        final SortBy sortBy = new SortBy(parts[0]);
        if (parts.length == 2) {
            if (Constants.ORDER_ASC.equalsIgnoreCase(parts[1]) || Constants.ORDER_DESC.equalsIgnoreCase(parts[1])) {
                sortBy.setOrder(parts[1]);
            } else {
                throw new Json4ormException("Invalid sortBy order '" + parts[1] + "'");
            }
        }

        return sortBy;
    }

    private Pagination generatePagination(final Object object) throws Json4ormException {
        final Pagination pagination = new Pagination();
        if (object != null) {
            if (!(object instanceof Map)) {
                throw new Json4ormException("Invalid pagination specified.");
            }

            final Map<String, Object> map = (Map<String, Object>) object;
            for (final Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(Constants.OFFSET)) {
                    pagination.setOffset(Long.valueOf(entry.getValue().toString()));
                } else if (entry.getKey().equalsIgnoreCase(Constants.LIMIT)) {
                    pagination.setLimit(Integer.valueOf(entry.getValue().toString()));
                } else if (entry.getKey().equalsIgnoreCase(Constants.TOTAL)) {
                    pagination.setTotal(Long.valueOf(entry.getValue().toString()));
                } else if (entry.getKey().equalsIgnoreCase(Constants.COUNT)) {
                    pagination.setCount(Integer.valueOf(entry.getValue().toString()));
                } else {
                    throw new Json4ormException("Invalid pagination property: " + entry.getKey());
                }
            }
        }
        return pagination;
    }

    private Result generateResult(final Object object, final Result parent) throws Json4ormException {
        if (object == null) {
            return null;
        }
        if (!(object instanceof Map)) {
            throw new Json4ormException("Invalid result specified.");
        }

        final Map<String, Object> resultMap = (Map<String, Object>) object;
        if (resultMap.size() > 1) {
            throw new Json4ormException("Mare than one objects specified for result.");
        }

        final Map.Entry<String, Object> topEntry = resultMap.entrySet().iterator().next();
        final Result result = generateResult(topEntry, null);
        return result;
    }

    private Filter generateFilter(final Object object) throws Json4ormException {
        Filter filter = null;
        if (object == null) {
            return null;
        }

        if (!(object instanceof Map)) {
            throw new Json4ormException("Invalid filter specified.");
        }

        final Map<String, Object> filterMap = (Map<String, Object>) object;
        if (filterMap.size() == 0) {
            throw new Json4ormException("Invalid filter specified.");
        }

        Filter parent = null;
        if (filterMap.size() > 1
                || (filterMap.size() == 1 && !EngineUtil.isLogicOperator(filterMap.keySet().iterator().next()))) {
            parent = new Filter();
            parent.setOperator(FilterOperator.AND);
        }

        for (final Map.Entry<String, Object> childEntry : filterMap.entrySet()) {
            filter = generateFilter(childEntry, parent);
        }

        if (parent != null) {
            return parent;
        }

        return filter;
    }

    private Filter generateFilter(final Map.Entry<String, Object> entry, final Filter parent) throws Json4ormException {
        final Filter filter = new Filter();
        if (entry.getValue() instanceof List) {
            if (EngineUtil.isLogicOperator(entry.getKey())) {
                filter.setOperator(entry.getKey());
                for (final Object child : (List) entry.getValue()) {
                    if (!(child instanceof Map)) {
                        throw new Json4ormException("Invalid filter object specified for: " + entry.getKey());
                    }
                    for (final Map.Entry<String, Object> childEntry : ((Map<String, Object>) child).entrySet()) {
                        final Filter childFilter = generateFilter(childEntry, filter);
                    }
                }
            } else {
                filter.setOperator(FilterOperator.IN);
                filter.setProperty(entry.getKey());
                filter.setValue(entry.getValue());
            }
        } else if (entry.getValue() instanceof String || entry.getValue() instanceof Number
                || entry.getValue() instanceof Boolean) {
            filter.setOperator(FilterOperator.EQUAL);
            filter.setProperty(entry.getKey());
            filter.setValue(entry.getValue());
        } else if (entry.getValue() instanceof Map) {
            filter.setProperty(entry.getKey());
            final Map<String, Object> valueMap = (Map<String, Object>) entry.getValue();
            if (valueMap.size() > 1) {
                throw new Json4ormException("More than one operators specified for: " + entry.getKey());
            }
            if (valueMap.size() == 0) {
                throw new Json4ormException("No operator specified for: " + entry.getKey());
            }
            final Map.Entry<String, Object> optEntry = valueMap.entrySet().iterator().next();

            filter.setOperator(optEntry.getKey());
            filter.setValue(optEntry.getValue());
        }

        if (filter != null && parent != null) {
            parent.addFilter(filter);
        }

        return filter;
    }

    private Result generateResult(final Map.Entry<String, Object> entry, final Result parent) throws Json4ormException {
        Result result = new Result();
        if (Constants.PROPERTIES.equalsIgnoreCase(entry.getKey())) {
            final List<String> properties = (List<String>) entry.getValue();
            if (parent == null) {
                throw new Json4ormException("Entity is not specifies for properties: " + properties.toString());
            }

            result = parent;
            if (properties != null) {
                result.setProperties(properties);
            }
        } else {
            if (entry.getValue() instanceof List) {
                result.setPropertyName(entry.getKey());
                result.setProperties((List<String>) entry.getValue());
            } else if (entry.getValue() instanceof Map) {
                result.setPropertyName(entry.getKey());
                final Map<String, Object> valueMap = (Map<String, Object>) entry.getValue();
                for (final Map.Entry<String, Object> childEntry : valueMap.entrySet()) {
                    generateResult(childEntry, result);
                }
            }
        }
        if (result != null && parent != null && result != parent) {
            parent.addAssociate(result);
        }

        return result;
    }

}
