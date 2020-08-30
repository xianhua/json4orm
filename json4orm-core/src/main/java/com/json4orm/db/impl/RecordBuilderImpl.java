/**
 * Copyright 2020 Xianhua Liu
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.json4orm.db.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.json4orm.db.Record;
import com.json4orm.db.Record2JsonUtil;
import com.json4orm.db.RecordBuilder;
import com.json4orm.engine.DatabaseDriver;
import com.json4orm.engine.QueryContext;
import com.json4orm.engine.ValueConvertor;
import com.json4orm.exception.Json4ormException;
import com.json4orm.model.query.Result;
import com.json4orm.model.schema.Entity;
import com.json4orm.model.schema.Property;
import com.json4orm.model.schema.PropertyType;

/**
 * The Class RecordBuilderImpl implements function to build record from data
 * returned in ResultSet from database.
 *
 * @author Xianhua Liu
 */
public class RecordBuilderImpl implements RecordBuilder {
	ValueConvertor valueConvertor;

	/** The records. */
	private final List<Record> records = new ArrayList<>();

	/**
	 * Gets the records.
	 *
	 * @return the records
	 */
	public List<Record> getRecords() {
		return records;
	}

	/**
	 * Builds the record.
	 *
	 * @param rs      the rs
	 * @param context the context
	 * @return the list
	 * @throws Json4ormException the json 4 orm exception
	 */
	@Override
	public List<Map<String, Object>> buildRecord(final ResultSet rs, final QueryContext context)
			throws Json4ormException {
		if (rs == null) {
			return null;
		}

		try {
			while (rs.next()) {
				buildTopLevelRecord(rs, context.getQuery().getResult(), context);
			}
		} catch (final SQLException e) {
			throw new Json4ormException("Failed to build records.", e);
		}

		return Record2JsonUtil.toJsonObject(getRecords());
	}

	/**
	 * Builds the top level record.
	 *
	 * @param rs      the rs
	 * @param result  the result
	 * @param context the context
	 * @throws Json4ormException the json 4 orm exception
	 */
	private void buildTopLevelRecord(final ResultSet rs, final Result result, final QueryContext context)
			throws Json4ormException {
		Record record = retrieveValues(rs, result, context);
		if(record == null) {
			return;
		}
		
		final Record existingRecord = findRecord(record);
		if (existingRecord == null) {
			records.add(record);
		} else {
			record = existingRecord;
		}

		for (final Result associatedResult : result.getAssociates()) {
			buildRecord(rs, associatedResult, context, record);
		}
	}

	/**
	 * Builds the record.
	 *
	 * @param rs      the rs
	 * @param result  the result
	 * @param context the context
	 * @param parent  the parent
	 * @throws Json4ormException the json 4 orm exception
	 */
	public void buildRecord(final ResultSet rs, final Result result, final QueryContext context, final Record parent)
			throws Json4ormException {
		Record record = retrieveValues(rs, result, context);
		if(record == null) {
			return;
		}
		final Record existingRecord = parent.findChildRecord(result.getEntity(), record);
		if (existingRecord == null) {
			parent.addAssociateRecord(result.getEntity(), record);
		} else {
			record = existingRecord;
		}

		for (final Result associatedResult : result.getAssociates()) {
			buildRecord(rs, associatedResult, context, record);
		}
	}

	/**
	 * Retrieve values.
	 *
	 * @param rs      the rs
	 * @param result  the result
	 * @param context the context
	 * @return the record
	 * @throws Json4ormException the json 4 orm exception
	 */
	private Record retrieveValues(final ResultSet rs, final Result result, final QueryContext context)
			throws Json4ormException {

		final Map<String, Object> values = new LinkedHashMap<>();

		final String alias = result.getAlias();
		final Entity entity = result.getEntityObj();

		final List<String> properties = result.getProperties();
		
		// check if record has none-null values
		boolean hasNoneNullValue = false;

		if (properties != null && !properties.isEmpty()) {
			for (final String property : properties) {
				final String fieldName = alias + "." + property;
				final int index = context.getFieldIndex(fieldName);
				if (index == 0) {
					throw new Json4ormException("No field found for: " + fieldName);
				}
				
				try {
					Object fieldValue = rs.getObject(index);
					if( fieldValue != null ) {
						hasNoneNullValue = true;
					}
					Property p = entity.getProperty(property);
					if (!PropertyType.isTypeValid(p.getType())) {
						// it is object type
						Entity propEntity = context.getSchema().getEntity(p.getType());
						if (propEntity == null) {
							throw new Json4ormException("Invalid property type: " + p.getType());
						}

						Property idProp = propEntity.getIdProperty();

						Map<String, Object> entityValue = new HashMap<>();
						entityValue.put(idProp.getName(), valueConvertor.convertFromDB(idProp, fieldValue));
						values.put(property, entityValue);
					} else {
						values.put(property, valueConvertor.convertFromDB(p, fieldValue));
					}
				} catch (final SQLException e) {
					throw new Json4ormException("Failed to retrieve value for: " + fieldName, e);
				}
			}
		}

		if (hasNoneNullValue) {
			final Record record = new Record(entity, values);
			return record;
		}
		
		return null;
	}

	/**
	 * Find record.
	 *
	 * @param record the record
	 * @return the record
	 */
	private Record findRecord(final Record record) {
		for (final Record r : records) {
			if (r.equalTo(record)) {
				return r;
			}
		}

		return null;
	}

	public ValueConvertor getValueConvertor() {
		return valueConvertor;
	}

	public void setValueConvertor(ValueConvertor valueConvertor) {
		this.valueConvertor = valueConvertor;
	}

}
