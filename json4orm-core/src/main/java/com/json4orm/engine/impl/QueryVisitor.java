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
import com.json4orm.model.entity.Entity;
import com.json4orm.model.entity.Property;
import com.json4orm.model.entity.Schema;
import com.json4orm.model.query.Filter;
import com.json4orm.model.query.FilterOperator;
import com.json4orm.model.query.Pagination;
import com.json4orm.model.query.Query;
import com.json4orm.model.query.Result;
import com.json4orm.model.query.SortBy;
import com.json4orm.util.Constants;
import com.json4orm.util.EngineUtil;

public class QueryVisitor implements Visitor {
    private Query query;
    private Schema schema;
    private String baseEntity;
    private ValueConvertor convertor;

    Map<String, String> fromTables = new HashMap<>();
    List<String> joins = new ArrayList<>();
    Map<String, String> aliasMap = new HashMap<>();
    List<String> selectedColumns = new ArrayList<>();
    List<String> selectedProperties = new ArrayList<>();

    StringBuffer where = new StringBuffer();
    StringBuffer fromBuf = new StringBuffer();
    StringBuffer queryBuf = new StringBuffer();
    StringBuffer limitBuf = new StringBuffer();
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

    public Map<String, String> getFromTables() {
        return fromTables;
    }

    public void setFromTables(final Map<String, String> fromTables) {
        this.fromTables = fromTables;
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
        aliasMap.put(baseEntity, baseAlias);

        // visit filter
        visit(query.getFilter(), FilterOperator.AND);
        visit(query.getResult(), "");
        visit(query.getPagination());
        visit(query.getSortBy());
        createFrom();
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

            final String alias = aliasMap.get(baseEntity);

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
        String sql = "SELECT " + StringUtils.join(selectedColumns, ",") + " FROM ";
        boolean first = true;
        for (final String key : fromTables.keySet()) {
            if (!first) {
                sql += ", ";
            }

            final Entity entity = schema.findEntity(key);
            final String alias = fromTables.get(key);
            sql += entity.getTable() + " " + alias;

            first = false;
        }
        sql += " WHERE " + where.toString();
        if (query.getPagination() != null) {
            final Entity entityBase = schema.getEntity(baseEntity);
            final Property idProperty = entityBase.getIdProperty();
            final String baseAlias = aliasMap.get(baseEntity);
            sql += " AND " + baseAlias + "." + idProperty.getColumn() + " IN (" + Constants.LIMIT_IDS + ")";
        }
        if (!EngineUtil.isEmpty(orderByList)) {
            sql += " ORDER BY " + StringUtils.join(orderByList, ",");
        }

        return sql;
    }

    private String getLimitQuery() {
        final Entity entityBase = schema.getEntity(baseEntity);
        final Property idProperty = entityBase.getIdProperty();
        final String baseAlias = aliasMap.get(baseEntity);

        String sql = "SELECT DISTINCT " + idProperty.getColumn() + " FROM ( ";
        sql += "SELECT " + baseAlias + "." + idProperty.getColumn() + " FROM ";
        boolean first = true;
        for (final String key : fromTables.keySet()) {
            if (!first) {
                sql += ", ";
            }

            final Entity entity = schema.findEntity(key);
            final String alias = fromTables.get(key);
            sql += entity.getTable() + " " + alias;

            first = false;
        }
        sql += " WHERE " + where.toString();

        if (!EngineUtil.isEmpty(orderByList)) {
            sql += " ORDER BY " + StringUtils.join(orderByList, ",");
        }

        sql += " ) AS temp ";
        if (query.getPagination() != null) {
            sql += " OFFSET " + query.getPagination().getOffset() + " LIMIT " + query.getPagination().getLimit();
        }

        return sql;
    }

    private String getCountQuery() throws Json4ormException {
        final Entity entityBase = schema.getEntity(baseEntity);
        final Property idProperty = entityBase.getIdProperty();
        final String baseAlias = aliasMap.get(baseEntity);
        String sql = "SELECT count(*) FROM (SELECT DISTINCT ";

        if (idProperty.getColumn() != null) {
            sql += baseAlias + "." + idProperty.getColumn();
        } else {
            throw new Json4ormException("No ID column(s) defined for: " + baseEntity + "." + idProperty.getName());
        }

        sql += " FROM ";
        boolean first = true;
        for (final String key : fromTables.keySet()) {
            if (!first) {
                sql += ", ";
            }

            final Entity entity = schema.findEntity(key);
            final String alias = fromTables.get(key);
            sql += entity.getTable() + " " + alias;

            first = false;
        }
        sql += " WHERE " + where.toString();
        sql += ") AS temp";

        return sql;
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

        final String alias = getOrCreateAlias(entity);
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

    private String getOrCreateAlias(final String property) throws Json4ormException {
        if (aliasMap.containsKey(property)) {
            return aliasMap.get(property);
        }
        final Entity entity = schema.findEntity(property);
        final String alias = EngineUtil.getAlias(entity.getName());
        aliasMap.put(property, alias);
        return alias;
    }

    private void visit(final Filter filter, final String logic) throws Json4ormException {
        final String operator = filter.getOperator();
        if (where.length() > 0 && where.charAt(where.length() - 1) != '(') {
            where.append(" " + logic + " ");
        }

        if (EngineUtil.isLogicOperator(operator)) {
            if (filter.getFilters() != null && filter.getFilters().size() > 0) {
                where.append("(");
                for (final Filter f : filter.getFilters()) {
                    visit(f, operator);
                }
                where.append(")");
            }
        } else {
            String propertyChain = filter.getProperty();
            if (!propertyChain.startsWith(baseEntity)) {
                propertyChain = baseEntity + "." + propertyChain;
            }

            final String entityChain = EngineUtil.getEntityChain(propertyChain);
            final String alias = getOrCreateAlias(entityChain);
            final String propertyName = EngineUtil.getLast(propertyChain);
            final Entity entity = schema.findEntity(entityChain);
            final Property property = entity.getProperty(propertyName);
            final String column = property.getColumn();
            if (FilterOperator.EQUAL.equalsIgnoreCase(operator)) {
                where.append(alias + "." + column + " = ?");
                values.add(convertor.convert(property, filter.getValue()));
            } else if (FilterOperator.EQUAL_CASE_INSENSITIVE.equalsIgnoreCase(operator)) {
                where.append("LOWER(" + alias + "." + column + ") = ?");
                values.add(((String) filter.getValue()).toLowerCase());
            } else if (FilterOperator.NOT_EQUAL.equalsIgnoreCase(operator)) {
                where.append(alias + "." + column + " != ?");
                values.add(convertor.convert(property, filter.getValue()));
            } else if (FilterOperator.NOT_EQUAL_CASE_INSENSITIVE.equalsIgnoreCase(operator)) {
                where.append("LOWER(" + alias + "." + column + ") != ?");
                values.add(((String) filter.getValue()).toLowerCase());
            } else if (FilterOperator.CONTAINS.equalsIgnoreCase(operator)) {
                where.append(alias + "." + column + " LIKE ?");
                values.add("%" + filter.getValue() + "%");
            } else if (FilterOperator.CONTAINS_CASE_INSENSITIVE.equalsIgnoreCase(operator)) {
                where.append("LOWER(" + alias + "." + column + ") LIKE ?");
                values.add("%" + ((String) filter.getValue()).toLowerCase() + "%");
            } else if (FilterOperator.NOT_CONTAINS.equalsIgnoreCase(operator)) {
                where.append(alias + "." + column + " NOT LIKE ?");
                values.add("%" + filter.getValue() + "%");
            } else if (FilterOperator.NOT_CONTAINS_CASE_INSENSITIVE.equalsIgnoreCase(operator)) {
                where.append("LOWER(" + alias + "." + column + ") NOT LIKE ?");
                values.add("%" + ((String) filter.getValue()).toLowerCase() + "%");
            } else if (FilterOperator.ENDS_WITH.equalsIgnoreCase(operator)) {
                where.append(alias + "." + column + " LIKE ?");
                values.add("%" + filter.getValue());
            } else if (FilterOperator.ENDS_WITH_CASE_INSENSITIVE.equalsIgnoreCase(operator)) {
                where.append("LOWER(" + alias + "." + column + ") LIKE ?");
                values.add("%" + ((String) filter.getValue()).toLowerCase());
            } else if (FilterOperator.NOT_ENDS_WITH.equalsIgnoreCase(operator)) {
                where.append(alias + "." + column + " NOT LIKE ?");
                values.add("%" + filter.getValue());
            } else if (FilterOperator.NOT_ENDS_WITH_CASE_INSENSITIVE.equalsIgnoreCase(operator)) {
                where.append("LOWER(" + alias + "." + column + ") NOT LIKE ?");
                values.add("%" + ((String) filter.getValue()).toLowerCase());
            } else if (FilterOperator.STARTS_WITH.equalsIgnoreCase(operator)) {
                where.append(alias + "." + column + " LIKE ?");
                values.add(filter.getValue() + "%");
            } else if (FilterOperator.STARTS_WITH_CASE_INSENSITIVE.equalsIgnoreCase(operator)) {
                where.append("LOWER(" + alias + "." + column + ") LIKE ?");
                values.add(((String) filter.getValue()).toLowerCase() + "%");
            } else if (FilterOperator.NOT_STARTS_WITH.equalsIgnoreCase(operator)) {
                where.append(alias + "." + column + " NOT LIKE ?");
                values.add(filter.getValue() + "%");
            } else if (FilterOperator.NOT_STARTS_WITH_CASE_INSENSITIVE.equalsIgnoreCase(operator)) {
                where.append("LOWER(" + alias + "." + column + ") NOT LIKE ?");
                values.add(((String) filter.getValue()).toLowerCase() + "%");
            } else if (FilterOperator.GREATER_THAN.equalsIgnoreCase(operator)) {
                where.append(alias + "." + column + " > ?");
                values.add(convertor.convert(property, filter.getValue()));
            } else if (FilterOperator.LESS_THAN.equalsIgnoreCase(operator)) {
                where.append(alias + "." + column + " < ?");
                values.add(convertor.convert(property, filter.getValue()));
            } else if (FilterOperator.NOT_GREATER_THAN.equalsIgnoreCase(operator)) {
                where.append(alias + "." + column + " = <= ?");
                values.add(convertor.convert(property, filter.getValue()));
            } else if (FilterOperator.NOT_LESS_THAN.equalsIgnoreCase(operator)) {
                where.append(alias + "." + column + " >= ?");
                values.add(convertor.convert(property, filter.getValue()));
            } else if (FilterOperator.IN.equalsIgnoreCase(operator)) {
                handleFilterWithListValues(where, values, alias, property, "IN", filter.getValue());
            } else if (FilterOperator.NOT_IN.equalsIgnoreCase(operator)) {
                handleFilterWithListValues(where, values, alias, property, "NOT IN", filter.getValue());
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

    private void createFrom() throws Json4ormException {

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
                toAlias = this.getOrCreateAlias(to);

                if (!fromTables.containsKey(to)) {
                    toAlias = this.getOrCreateAlias(to);
                }
                fromTables.put(to, toAlias);

                if (from != null) {
                    createJoins(from, fromAlias, entity, toAlias);
                }

                from = to;
                fromAlias = toAlias;
            }
        }
    }

    private void createJoins(final String from, final String fromAlias, final String to, final String toAlias)
            throws Json4ormException {
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
