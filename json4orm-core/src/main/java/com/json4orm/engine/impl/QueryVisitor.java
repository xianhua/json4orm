package com.json4orm.engine.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.json4orm.engine.QueryContext;
import com.json4orm.engine.ValueConvertor;
import com.json4orm.engine.Visitor;
import com.json4orm.exception.Json4ormException;
import com.json4orm.model.query.Filter;
import com.json4orm.model.query.FilterOperator;
import com.json4orm.model.query.Pagination;
import com.json4orm.model.query.Query;
import com.json4orm.model.query.Result;
import com.json4orm.model.query.SortBy;
import com.json4orm.model.schema.Entity;
import com.json4orm.model.schema.Property;
import com.json4orm.model.schema.Schema;
import com.json4orm.util.Constants;
import com.json4orm.util.EngineUtil;

public class QueryVisitor implements Visitor {
    private Query query;
    private Schema schema;
    private String baseEntity;
    private ValueConvertor convertor;

    Map<String, String> fromTablesForFilter = new HashMap<>();
    Map<String, String> fromTablesForResult = new HashMap<>();

    List<String> joins = new ArrayList<>();
    Map<String, String> aliasMapForFilter = new HashMap<>();
    Map<String, String> aliasMapForResult = new HashMap<>();
    List<String> selectedColumns = new ArrayList<>();
    List<String> selectedProperties = new ArrayList<>();

    StringBuffer whereForFilter = new StringBuffer();
    StringBuffer whereForResult = new StringBuffer();

    List<String> orderByList = new ArrayList<>();

    List<Object> values = new ArrayList<>();
    Set<String> entitySet = new HashSet<>();

    public QueryVisitor(final Schema schema) {
        super();
        this.schema = schema;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(final Schema schema) {
        this.schema = schema;
    }

    public ValueConvertor getConvertor() {
        return convertor;
    }

    public void setConvertor(final ValueConvertor convertor) {
        this.convertor = convertor;
    }

    @Override
    public QueryContext visit(final Query query) throws Json4ormException {
        EngineUtil.resetAliasPlaceHolderCounts();
        this.query = query;
        baseEntity = query.getQueryFor();
        final String baseAlias = EngineUtil.getAlias(query.getQueryFor());
        aliasMapForFilter.put(baseEntity, baseAlias);
        aliasMapForResult.put(baseEntity, baseAlias);

        // visit filter
        visit(query.getFilter(), FilterOperator.AND);
        if (query.getResult() == null) {
            // set default result to full list of properties of the baseEntity
            final Result result = new Result();
            final List<String> properties= new ArrayList<>();
            final Entity entity = schema.getEntity(baseEntity);
            final String alias = getOrCreateAlias(baseEntity, aliasMapForResult);
            for(final Property p: entity.getOwnedPropeties()) {
                properties.add(p.getName());
            }
            result.setAlias(alias);
            result.setEntity(entity);
            result.setProperties(properties);
            result.setPropertyName(baseEntity);
            query.setResult(result);
        }
        
        visit(query.getResult(), "");
        visit(query.getPagination());
        visit(query.getSortBy());
        createFrom(fromTablesForFilter, aliasMapForFilter, whereForFilter);
        createFrom(fromTablesForResult, aliasMapForResult, whereForResult);

        return getQueryContext();
    }

    private void visit(final List<SortBy> sortBy) throws Json4ormException {
        if (sortBy == null) {
            return;
        }

        for (final SortBy sb : sortBy) {
            // check if property is for the baseEntity
            final Entity entity = schema.getEntity(baseEntity);
            final Property property = entity.getProperty(sb.getProperty());
            if (property == null) {
                throw new Json4ormException("SortBy field is not found for: " + baseEntity + "." + sb.getProperty());
            }

            final String alias = aliasMapForResult.get(baseEntity);

            orderByList.add(alias + "." + property.getColumn() + " " + sb.getOrder());
        }

    }

    private void visit(final Pagination pagination) {

    }

    public QueryContext getQueryContext() throws Json4ormException {
        final QueryContext queryContext = new QueryContext();

        queryContext.setSql(getQuery());
        queryContext.setCountSql(getCountQuery());
        queryContext.setLimitSql(getLimitQuery());
        queryContext.setValues(values);
        queryContext.setSelectedFields(selectedProperties);
        queryContext.setQuery(query);
        queryContext.setSchema(schema);
        return queryContext;
    }

    private String getQuery() {
        final StringBuffer buf = new StringBuffer(100);
        buf.append("SELECT " + StringUtils.join(selectedColumns, ",") + " FROM ");
        boolean first = true;
        for (final String key : fromTablesForResult.keySet()) {
            if (!first) {
                buf.append(", ");
            }

            final Entity entity = schema.findEntity(key);
            final String alias = fromTablesForResult.get(key);
            buf.append(entity.getTable() + " " + alias);

            first = false;
        }

        if (whereForResult.length() > 0) {
            buf.append(" WHERE " + whereForResult.toString());
        }
        if (query.getPagination() != null) {
            if (whereForResult.length() > 0) {
                buf.append(" AND ");
            }else {
                buf.append(" WHERE ");
            }
            final Entity entityBase = schema.getEntity(baseEntity);
            final Property idProperty = entityBase.getIdProperty();
            final String baseAlias = aliasMapForResult.get(baseEntity);
            buf.append(baseAlias + "." + idProperty.getColumn() + " IN (" + Constants.LIMIT_IDS + ")");
        }
        if (!EngineUtil.isEmpty(orderByList)) {
            buf.append(" ORDER BY " + StringUtils.join(orderByList, ","));
        }

        return buf.toString();
    }

    private String getLimitQuery() {
        final String baseAlias = aliasMapForFilter.get(baseEntity);
        final StringBuffer buf = new StringBuffer(100);
        buf.append("SELECT DISTINCT " + baseAlias + ".* FROM ");
        boolean first = true;
        for (final String key : fromTablesForFilter.keySet()) {
            if (!first) {
                buf.append(", ");
            }

            final Entity entity = schema.findEntity(key);
            final String alias = fromTablesForFilter.get(key);
            buf.append(entity.getTable() + " " + alias);

            first = false;
        }
        if (whereForFilter.length() > 0) {
            buf.append(" WHERE " + whereForFilter.toString());
        }
        
        if (!EngineUtil.isEmpty(orderByList)) {
            buf.append(" ORDER BY " + StringUtils.join(orderByList, ","));
        }

        if (query.getPagination() != null) {
            buf.append(" OFFSET " + query.getPagination().getOffset() + " LIMIT " + query.getPagination().getLimit());
        }

        return buf.toString();
    }

    private String getCountQuery() throws Json4ormException {
        final Entity entityBase = schema.getEntity(baseEntity);
        final Property idProperty = entityBase.getIdProperty();
        final String baseAlias = aliasMapForFilter.get(baseEntity);
        final StringBuffer buf = new StringBuffer(100);
        buf.append("SELECT count(*) FROM (SELECT DISTINCT ");
        buf.append(baseAlias + "." + idProperty.getColumn());
        buf.append(" FROM ");
        boolean first = true;
        for (final String key : fromTablesForFilter.keySet()) {
            if (!first) {
                buf.append(", ");
            }

            final Entity entity = schema.findEntity(key);
            final String alias = fromTablesForFilter.get(key);
            buf.append(entity.getTable() + " " + alias);

            first = false;
        }
        if (whereForFilter.length() > 0) {
            buf.append(" WHERE " + whereForFilter.toString());
        }

        buf.append(") AS temp");

        return buf.toString();
    }

    private void visit(final Result result, final String entityChain) throws Json4ormException {
        String entity = result.getPropertyName();

        if (entity == null) {
            entity = baseEntity;
        } else {
            if (!entity.equalsIgnoreCase(baseEntity)) {
                entity = entityChain + "." + entity;
            }
        }

        final Entity entityObj = schema.findEntity(entity);
        if (entityObj == null) {
            throw new Json4ormException("No entity defined for: " + entity);
        }
        result.setEntity(entityObj);

        final String alias = getOrCreateAlias(entity, aliasMapForResult);
        result.setAlias(alias);

        for (final String s : result.getProperties()) {
            final Property p = entityObj.getProperty(s);
            if (p == null) {
                throw new Json4ormException("No property defined for: " + entity + "." + s);
            }
            selectedColumns.add(alias + "." + p.getColumn());
            selectedProperties.add(alias + "." + p.getName());
        }

        // add PK if missing
        final Property idProperty = entityObj.getIdProperty();
        if (idProperty != null) {
            if (StringUtils.isNotBlank(idProperty.getColumn())) {
                final String idField = alias + "." + idProperty.getColumn();
                if (!selectedColumns.contains(idField)) {
                    selectedColumns.add(idField);
                    final Property p = entityObj.getPropertyByColumn(idProperty.getColumn());
                    selectedProperties.add(alias + "." + p.getName());
                    result.addProperty(p.getName());
                }
            } else if (!EngineUtil.isEmpty(idProperty.getColumns())) {
                for (final String idColumn : idProperty.getColumns()) {
                    final String idField = alias + "." + idColumn;
                    if (!selectedColumns.contains(idField)) {
                        selectedColumns.add(idField);
                        final Property p = entityObj.getPropertyByColumn(idColumn);
                        selectedProperties.add(alias + "." + p.getName());
                        result.addProperty(p.getName());
                    }
                }
            }
        }

        if (result.getAssociates() != null && result.getAssociates().size() > 0) {
            for (final Result assocResult : result.getAssociates()) {
                visit(assocResult, entity);
            }
        }

    }

    private String getOrCreateAlias(final String property, final Map<String, String> aliasMap)
            throws Json4ormException {
        if (aliasMap.containsKey(property)) {
            return aliasMap.get(property);
        }
        final Entity entity = schema.findEntity(property);
        final String alias = EngineUtil.getAlias(entity.getName());
        aliasMap.put(property, alias);
        return alias;
    }

    private void visit(final Filter filter, final String logic) throws Json4ormException {
        if (filter == null) {
            return;
        }
        final String operator = filter.getOperator();
        if (whereForFilter.length() > 0 && whereForFilter.charAt(whereForFilter.length() - 1) != '(') {
            whereForFilter.append(" " + logic + " ");
        }

        if (EngineUtil.isLogicOperator(operator)) {
            if (filter.getFilters() != null && filter.getFilters().size() > 0) {
                whereForFilter.append("(");
                for (final Filter f : filter.getFilters()) {
                    visit(f, operator);
                }
                whereForFilter.append(")");
            }
        } else {
            String propertyChain = filter.getProperty();
            if (!propertyChain.startsWith(baseEntity)) {
                propertyChain = baseEntity + "." + propertyChain;
            }

            final String entityChain = EngineUtil.getEntityChain(propertyChain);
            final String alias = getOrCreateAlias(entityChain, aliasMapForFilter);
            final String propertyName = EngineUtil.getLast(propertyChain);
            final Entity entity = schema.findEntity(entityChain);
            final Property property = entity.getProperty(propertyName);
            final String column = property.getColumn();
            if (FilterOperator.EQUAL.equalsIgnoreCase(operator)) {
                whereForFilter.append(alias + "." + column + " = ?");
                values.add(convertor.convert(property, filter.getValue()));
            } else if (FilterOperator.EQUAL_CASE_INSENSITIVE.equalsIgnoreCase(operator)) {
                whereForFilter.append("LOWER(" + alias + "." + column + ") = ?");
                values.add(((String) filter.getValue()).toLowerCase());
            } else if (FilterOperator.NOT_EQUAL.equalsIgnoreCase(operator)) {
                whereForFilter.append(alias + "." + column + " != ?");
                values.add(convertor.convert(property, filter.getValue()));
            } else if (FilterOperator.NOT_EQUAL_CASE_INSENSITIVE.equalsIgnoreCase(operator)) {
                whereForFilter.append("LOWER(" + alias + "." + column + ") != ?");
                values.add(((String) filter.getValue()).toLowerCase());
            } else if (FilterOperator.CONTAINS.equalsIgnoreCase(operator)) {
                whereForFilter.append(alias + "." + column + " LIKE ?");
                values.add("%" + filter.getValue() + "%");
            } else if (FilterOperator.CONTAINS_CASE_INSENSITIVE.equalsIgnoreCase(operator)) {
                whereForFilter.append("LOWER(" + alias + "." + column + ") LIKE ?");
                values.add("%" + ((String) filter.getValue()).toLowerCase() + "%");
            } else if (FilterOperator.NOT_CONTAINS.equalsIgnoreCase(operator)) {
                whereForFilter.append(alias + "." + column + " NOT LIKE ?");
                values.add("%" + filter.getValue() + "%");
            } else if (FilterOperator.NOT_CONTAINS_CASE_INSENSITIVE.equalsIgnoreCase(operator)) {
                whereForFilter.append("LOWER(" + alias + "." + column + ") NOT LIKE ?");
                values.add("%" + ((String) filter.getValue()).toLowerCase() + "%");
            } else if (FilterOperator.ENDS_WITH.equalsIgnoreCase(operator)) {
                whereForFilter.append(alias + "." + column + " LIKE ?");
                values.add("%" + filter.getValue());
            } else if (FilterOperator.ENDS_WITH_CASE_INSENSITIVE.equalsIgnoreCase(operator)) {
                whereForFilter.append("LOWER(" + alias + "." + column + ") LIKE ?");
                values.add("%" + ((String) filter.getValue()).toLowerCase());
            } else if (FilterOperator.NOT_ENDS_WITH.equalsIgnoreCase(operator)) {
                whereForFilter.append(alias + "." + column + " NOT LIKE ?");
                values.add("%" + filter.getValue());
            } else if (FilterOperator.NOT_ENDS_WITH_CASE_INSENSITIVE.equalsIgnoreCase(operator)) {
                whereForFilter.append("LOWER(" + alias + "." + column + ") NOT LIKE ?");
                values.add("%" + ((String) filter.getValue()).toLowerCase());
            } else if (FilterOperator.STARTS_WITH.equalsIgnoreCase(operator)) {
                whereForFilter.append(alias + "." + column + " LIKE ?");
                values.add(filter.getValue() + "%");
            } else if (FilterOperator.STARTS_WITH_CASE_INSENSITIVE.equalsIgnoreCase(operator)) {
                whereForFilter.append("LOWER(" + alias + "." + column + ") LIKE ?");
                values.add(((String) filter.getValue()).toLowerCase() + "%");
            } else if (FilterOperator.NOT_STARTS_WITH.equalsIgnoreCase(operator)) {
                whereForFilter.append(alias + "." + column + " NOT LIKE ?");
                values.add(filter.getValue() + "%");
            } else if (FilterOperator.NOT_STARTS_WITH_CASE_INSENSITIVE.equalsIgnoreCase(operator)) {
                whereForFilter.append("LOWER(" + alias + "." + column + ") NOT LIKE ?");
                values.add(((String) filter.getValue()).toLowerCase() + "%");
            } else if (FilterOperator.GREATER_THAN.equalsIgnoreCase(operator)) {
                whereForFilter.append(alias + "." + column + " > ?");
                values.add(convertor.convert(property, filter.getValue()));
            } else if (FilterOperator.LESS_THAN.equalsIgnoreCase(operator)) {
                whereForFilter.append(alias + "." + column + " < ?");
                values.add(convertor.convert(property, filter.getValue()));
            } else if (FilterOperator.NOT_GREATER_THAN.equalsIgnoreCase(operator)) {
                whereForFilter.append(alias + "." + column + " = <= ?");
                values.add(convertor.convert(property, filter.getValue()));
            } else if (FilterOperator.NOT_LESS_THAN.equalsIgnoreCase(operator)) {
                whereForFilter.append(alias + "." + column + " >= ?");
                values.add(convertor.convert(property, filter.getValue()));
            } else if (FilterOperator.IN.equalsIgnoreCase(operator)) {
                handleFilterWithListValues(whereForFilter, values, alias, property, "IN", filter.getValue());
            } else if (FilterOperator.NOT_IN.equalsIgnoreCase(operator)) {
                handleFilterWithListValues(whereForFilter, values, alias, property, "NOT IN", filter.getValue());
            } else {
                throw new Json4ormException("Invalid operator: " + operator);
            }
        }
    }

    private void handleFilterWithListValues(final StringBuffer buf, final List<Object> values, final String alias,
            final Property property, final String operator, final Object value) throws Json4ormException {
        if (value == null || !(value instanceof List)) {
            throw new Json4ormException("List of values is expected for operator: IN");
        }

        final List<?> list = (List<?>) value;
        if (list.size() == 0) {
            throw new Json4ormException("At least one value is expected in the list for operator: IN");
        }
        buf.append(alias + "." + property.getColumn() + " " + operator + "(?");
        values.add(convertor.convert(property, list.get(0)));

        for (int i = 1; i < list.size(); i++) {
            buf.append(",?");
            values.add(convertor.convert(property, list.get(i)));
        }

        buf.append(")");
    }

    private void createFrom(final Map<String, String> fromTables, final Map<String, String> aliasMap,
            final StringBuffer where) throws Json4ormException {

        for (final String entityChain : aliasMap.keySet()) {
            String from = null, to = null, fromAlias = null, toAlias = null;
            final String ss[] = entityChain.split("\\.");

            String entity = "";
            for (final String s : ss) {
                if (entity.length() > 0) {
                    entity += ".";
                }
                entity += s;
                to = entity;
                toAlias = this.getOrCreateAlias(to, aliasMap);

                if (!fromTables.containsKey(to)) {
                    toAlias = this.getOrCreateAlias(to, aliasMap);
                }
                fromTables.put(to, toAlias);

                if (from != null) {
                    createJoins(from, fromAlias, entity, toAlias, where);
                }

                from = to;
                fromAlias = toAlias;
            }
        }
    }

    private void createJoins(final String from, final String fromAlias, final String to, final String toAlias,
            final StringBuffer where) throws Json4ormException {
        final String key = from + " join " + to;
        if (joins.contains(key)) {
            return;
        }

        joins.add(key);

        final String propertyName = EngineUtil.getLast(to);
        final Entity toEntity = schema.findEntity(to);
        final Entity fromEntity = schema.findEntity(from);
        final Property fromProperty = fromEntity.getProperty(propertyName);
        final String fromColumn = fromProperty.getColumn();
        List<Property> linkedProperties = toEntity.findPropertiesByType(fromEntity.getName());
        if (linkedProperties.isEmpty()) {
            linkedProperties = fromEntity.findPropertiesByType(toEntity.getName());
        }
        if (linkedProperties.isEmpty()) {
            throw new Json4ormException(
                    "No join is found between: " + fromEntity.getName() + " and " + toEntity.getName());
        }
        final String toColumn = linkedProperties.get(0).getColumn();
        if (where.length() > 0) {
            where.append(" AND ");
        }
        where.append(" " + fromAlias + "." + fromColumn + " = " + toAlias + "." + toColumn + " ");
    }
}
