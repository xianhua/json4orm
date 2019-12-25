package com.json4orm.engine.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.json4orm.engine.VisitingResult;
import com.json4orm.engine.Visitor;
import com.json4orm.exception.Json4ormException;
import com.json4orm.model.entity.Entity;
import com.json4orm.model.entity.Property;
import com.json4orm.model.entity.Schema;
import com.json4orm.model.query.Filter;
import com.json4orm.model.query.FilterOperator;
import com.json4orm.model.query.Query;
import com.json4orm.model.query.Result;
import com.json4orm.util.EngineUtil;

public class QueryVisitor implements Visitor {
    private Schema schema;
    private String baseEntity;

    Map<String, String> fromTables = new HashMap<>();
    List<String> joins = new ArrayList<>();
    Map<String, String> aliasMap = new HashMap<>();

    StringBuffer where = new StringBuffer();
    List<String> select = new ArrayList<>();
    StringBuffer fromBuf = new StringBuffer();

    StringBuffer queryBuf = new StringBuffer();
    Map<String, Object> values = new HashMap<>();
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

    @Override
    public VisitingResult visit(final Query query) throws Json4ormException {
        baseEntity = query.getQueryFor();
        final String baseAlias = EngineUtil.getAlias(query.getQueryFor());
        aliasMap.put(baseEntity, baseAlias);

        // visit filter
        visit(query.getFilter(), FilterOperator.AND);
        visit(query.getResult(), "");
        createFrom();
        return getVisitingResult();
    }

    public VisitingResult getVisitingResult() {
        final VisitingResult visitResult = new VisitingResult();
        String sql = "SELECT " + StringUtils.join(select, ",") + " FROM ";
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

        visitResult.setSql(sql);
        visitResult.setValues(values);
        return visitResult;
    }

    private void visit(final Result result, final String entityChain) throws Json4ormException {
        String entity = result.getEntity();

        if (entity == null) {
            entity = baseEntity;
        } else {
            if (!entity.equalsIgnoreCase(baseEntity)) {
                entity = entityChain + "." + entity;
            }
        }

        if (result.getProperties() != null && result.getProperties().size() > 0) {
            final String alias = getOrCreateAlias(entity);
            for (final String s : result.getProperties()) {
                select.add(alias + "." + s);
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

            final String placeHolder = EngineUtil.getPlaceHolder(propertyName);

            if (FilterOperator.EQUAL.equalsIgnoreCase(operator)) {
                where.append(alias + "." + propertyName + " = :" + placeHolder);
                values.put(placeHolder, filter.getValue());
            } else if (FilterOperator.EQUAL_CASE_INSENSITIVE.equalsIgnoreCase(operator)) {
                where.append("LOWER(" + alias + "." + propertyName + ") = :" + placeHolder);
                values.put(placeHolder, ((String) filter.getValue()).toLowerCase());
            } else if (FilterOperator.NOT_EQUAL.equalsIgnoreCase(operator)) {
                where.append(alias + "." + propertyName + " != :" + placeHolder);
                values.put(placeHolder, filter.getValue());
            } else if (FilterOperator.NOT_EQUAL_CASE_INSENSITIVE.equalsIgnoreCase(operator)) {
                where.append("LOWER(" + alias + "." + propertyName + ") != :" + placeHolder);
                values.put(placeHolder, ((String) filter.getValue()).toLowerCase());
            } else if (FilterOperator.CONTAINS.equalsIgnoreCase(operator)) {
                where.append(alias + "." + propertyName + " LIKE :" + placeHolder);
                values.put(placeHolder, "%" + filter.getValue() + "%");
            } else if (FilterOperator.CONTAINS_CASE_INSENSITIVE.equalsIgnoreCase(operator)) {
                where.append("LOWER(" + alias + "." + propertyName + ") LIKE :" + placeHolder);
                values.put(placeHolder, "%" + ((String) filter.getValue()).toLowerCase() + "%");
            } else if (FilterOperator.NOT_CONTAINS.equalsIgnoreCase(operator)) {
                where.append(alias + "." + propertyName + " NOT LIKE :" + placeHolder);
                values.put(placeHolder, "%" + filter.getValue() + "%");
            } else if (FilterOperator.NOT_CONTAINS_CASE_INSENSITIVE.equalsIgnoreCase(operator)) {
                where.append("LOWER(" + alias + "." + propertyName + ") NOT LIKE :" + placeHolder);
                values.put(placeHolder, "%" + ((String) filter.getValue()).toLowerCase() + "%");
            } else if (FilterOperator.ENDS_WITH.equalsIgnoreCase(operator)) {
                where.append(alias + "." + propertyName + " LIKE :" + placeHolder);
                values.put(placeHolder, "%" + filter.getValue());
            } else if (FilterOperator.ENDS_WITH_CASE_INSENSITIVE.equalsIgnoreCase(operator)) {
                where.append("LOWER(" + alias + "." + propertyName + ") LIKE :" + placeHolder);
                values.put(placeHolder, "%" + ((String) filter.getValue()).toLowerCase());
            } else if (FilterOperator.NOT_ENDS_WITH.equalsIgnoreCase(operator)) {
                where.append(alias + "." + propertyName + " NOT LIKE :" + placeHolder);
                values.put(placeHolder, "%" + filter.getValue());
            } else if (FilterOperator.NOT_ENDS_WITH_CASE_INSENSITIVE.equalsIgnoreCase(operator)) {
                where.append("LOWER(" + alias + "." + propertyName + ") NOT LIKE :" + placeHolder);
                values.put(placeHolder, "%" + ((String) filter.getValue()).toLowerCase());
            } else if (FilterOperator.STARTS_WITH.equalsIgnoreCase(operator)) {
                where.append(alias + "." + propertyName + " LIKE :" + placeHolder);
                values.put(placeHolder, filter.getValue() + "%");
            } else if (FilterOperator.STARTS_WITH_CASE_INSENSITIVE.equalsIgnoreCase(operator)) {
                where.append("LOWER(" + alias + "." + propertyName + ") LIKE :" + placeHolder);
                values.put(placeHolder, ((String) filter.getValue()).toLowerCase() + "%");
            } else if (FilterOperator.NOT_STARTS_WITH.equalsIgnoreCase(operator)) {
                where.append(alias + "." + propertyName + " NOT LIKE :" + placeHolder);
                values.put(placeHolder, filter.getValue() + "%");
            } else if (FilterOperator.NOT_STARTS_WITH_CASE_INSENSITIVE.equalsIgnoreCase(operator)) {
                where.append("LOWER(" + alias + "." + propertyName + ") NOT LIKE :" + placeHolder);
                values.put(placeHolder, ((String) filter.getValue()).toLowerCase() + "%");
            } else if (FilterOperator.GREATER_THAN.equalsIgnoreCase(operator)) {
                where.append(alias + "." + propertyName + " > :" + placeHolder);
                values.put(placeHolder, filter.getValue());
            } else if (FilterOperator.LESS_THAN.equalsIgnoreCase(operator)) {
                where.append(alias + "." + propertyName + " < :" + placeHolder);
                values.put(placeHolder, filter.getValue());
            } else if (FilterOperator.NOT_GREATER_THAN.equalsIgnoreCase(operator)) {
                where.append(alias + "." + propertyName + " = <= :" + placeHolder);
                values.put(placeHolder, filter.getValue());
            } else if (FilterOperator.NOT_LESS_THAN.equalsIgnoreCase(operator)) {
                where.append(alias + "." + propertyName + " >= :" + placeHolder);
                values.put(placeHolder, filter.getValue());
            } else if (FilterOperator.IN.equalsIgnoreCase(operator)) {
                where.append(alias + "." + propertyName + " IN (:" + placeHolder + ")");
                values.put(placeHolder, filter.getValue());
            } else if (FilterOperator.NOT_IN.equalsIgnoreCase(operator)) {
                where.append(alias + "." + propertyName + " NOT IN (:" + placeHolder + ")");
                values.put(placeHolder, filter.getValue());
            } else {
                throw new Json4ormException("Invalid operator: " + operator);
            }
        }
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
