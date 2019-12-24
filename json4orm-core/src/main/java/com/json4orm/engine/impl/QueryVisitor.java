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

	public QueryVisitor(Schema schema) {
		super();
		this.schema = schema;
	}

	public Schema getSchema() {
		return schema;
	}

	public void setSchema(Schema schema) {
		this.schema = schema;
	}

	public Map<String, String> getFromTables() {
		return fromTables;
	}

	public void setFromTables(Map<String, String> fromTables) {
		this.fromTables = fromTables;
	}

	@Override
	public VisitingResult visit(Query query) throws Json4ormException {
		baseEntity = query.getQueryFor();
		String baseAlias = EngineUtil.getAlias(query.getQueryFor());
		aliasMap.put(baseEntity, baseAlias);

		// visit filter
		visit(query.getFilter(), FilterOperator.AND);
		visit(query.getResult(), "");
		createFrom();
		return getVisitingResult();
	}

	public VisitingResult getVisitingResult() {
		VisitingResult visitResult = new VisitingResult();
		String sql = "SELECT " + StringUtils.join(select, ",") + " FROM ";
		boolean first = true;
		for (String key : fromTables.keySet()) {
			if (!first) {
				sql += ", ";
			}

			Entity entity = schema.findEntity(key);
			String alias = fromTables.get(key);
			sql += entity.getTable() + " " + alias;

			first = false;
		}
		sql += " WHERE " + where.toString();

		visitResult.setSql(sql);
		visitResult.setValues(values);
		return visitResult;
	}

	private void visit(Result result, String entityChain) throws Json4ormException {
		String entity = result.getEntity();

		if (entity == null) {
			entity = baseEntity;
		} else {
			entity = entityChain + "." + entity;
		}

		if (result.getProperties() != null && result.getProperties().size() > 0) {
			String alias = getOrCreateAlias(entity);
			for (String s : result.getProperties()) {
				select.add(alias + "." + s);
			}
		}

		if (result.getAssociates() != null && result.getAssociates().size() > 0) {
			for (Result assocResult : result.getAssociates()) {
				visit(assocResult, entity);
			}
		}

	}

	private String getOrCreateAlias(String property) throws Json4ormException {
		if (aliasMap.containsKey(property)) {
			return aliasMap.get(property);
		}
		Entity entity = schema.findEntity(property);
		String alias = EngineUtil.getAlias(entity.getName());
		aliasMap.put(property, alias);
		return alias;
	}

	private void visit(Filter filter, String logic) throws Json4ormException {
		String operator = filter.getOperator();
		if (where.length() > 0 && where.charAt(where.length() - 1) != '(') {
			where.append(" " + logic + " ");
		}
		
		if (EngineUtil.isLogicOperator(operator)) {
			if (filter.getFilters() != null && filter.getFilters().size() > 0) {
				where.append("(");
				for (Filter f : filter.getFilters()) {
					visit(f, operator);
				}
				where.append(")");
			}
		} else {
			String propertyChain = filter.getProperty();
			if (!propertyChain.startsWith(baseEntity)) {
				propertyChain = baseEntity + "." + propertyChain;
			}

			String entityChain = EngineUtil.getEntityChain(propertyChain);
			String alias = getOrCreateAlias(entityChain);
			String propertyName = EngineUtil.getLast(propertyChain);

			String placeHolder = EngineUtil.getPlaceHolder(propertyName);

			if (FilterOperator.EQUAL.equalsIgnoreCase(operator)) {
				where.append(alias + "." + propertyName + " = :" + placeHolder);
				values.put(placeHolder, filter.getValue());
			} else if (FilterOperator.CONTAINS.equalsIgnoreCase(operator)) {
				where.append(alias + "." + propertyName + " like :" + placeHolder);
				values.put(placeHolder, "%" + filter.getValue() + "%");
			} else if (FilterOperator.END_WITH.equalsIgnoreCase(operator)) {
				where.append(alias + "." + propertyName + " like :" + placeHolder);
				values.put(placeHolder, "%" + filter.getValue());
			} else if (FilterOperator.STARTS_WITH.equalsIgnoreCase(operator)) {
				where.append(alias + "." + propertyName + " like :" + placeHolder);
				values.put(placeHolder, filter.getValue() + "%");
			} else if (FilterOperator.GREATER_THAN.equalsIgnoreCase(operator)) {
				where.append(alias + "." + propertyName + " > :" + placeHolder);
				values.put(placeHolder, filter.getValue());
			} else if (FilterOperator.IN.equalsIgnoreCase(operator)) {
				where.append(alias + "." + propertyName + " in (:" + placeHolder + ")");
				values.put(placeHolder, filter.getValues());
			} else if (FilterOperator.NOT_EQUAL.equalsIgnoreCase(operator)) {
				where.append(alias + "." + propertyName + " != :" + placeHolder);
				values.put(placeHolder, filter.getValue());
			} else if (FilterOperator.NOT_GREATER_THAN.equalsIgnoreCase(operator)) {
				where.append(alias + "." + propertyName + " = <= :" + placeHolder);
				values.put(placeHolder, filter.getValue());
			} else if (FilterOperator.NOT_LESS_THAN.equalsIgnoreCase(operator)) {
				where.append(alias + "." + propertyName + " >= :" + placeHolder);
				values.put(placeHolder, filter.getValue());
			} else {
				throw new Json4ormException("Invalid operator: " + operator);
			}
		}
	}

	private void createFrom() throws Json4ormException {

		for (String entityChain : aliasMap.keySet()) {
			String from = null, to = null, fromAlias = null, toAlias = null;
			String ss[] = entityChain.split("\\.");

			String entity = "";
			for (String s : ss) {
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

	private void createJoins(String from, String fromAlias, String to, String toAlias) throws Json4ormException {
		String key = from + " join " + to;
		if (joins.contains(key)) {
			return;
		}

		joins.add(key);

		String propertyName = EngineUtil.getLast(to);
		Entity toEntity = schema.findEntity(to);
		Entity fromEntity = schema.findEntity(from);
		Property fromProperty = fromEntity.getProperty(propertyName);
		String fromColumn = fromProperty.getColumn();
		List<Property> linkedProperties = toEntity.findPropertiesByType(fromEntity.getName());
		if (linkedProperties.isEmpty()) {
			linkedProperties = fromEntity.findPropertiesByType(toEntity.getName());
		}
		if (linkedProperties.isEmpty()) {
			throw new Json4ormException(
					"No join is found between: " + fromEntity.getName() + " and " + toEntity.getName());
		}
		String toColumn = linkedProperties.get(0).getColumn();
		if (where.length() > 0) {
			where.append(" AND ");
		}
		where.append(" " + fromAlias + "." + fromColumn + " = " + toAlias + "." + toColumn + " ");
	}
}
