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
package com.json4orm.db;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.json4orm.model.schema.Entity;
import com.json4orm.model.schema.Property;
import com.json4orm.model.schema.PropertyType;
import com.json4orm.util.EngineUtil;

/**
 * The Class Record defines data structure to represent a record, including the
 * Entity and values of the record. It can also contains the associated Entity
 * records as nested value.
 *
 * @author Xianhua Liu
 */
public class Record {

    /** The entity. */
    private Entity entity;

    /** The values map. */
    private Map<String, Object> valuesMap = new LinkedHashMap<>();

    /**
     * Instantiates a new record.
     *
     * @param entity the entity
     * @param values the values
     */
    public Record(final Entity entity, final Map<String, Object> values) {
        this.entity = entity;
        this.valuesMap = values;
    }

    /**
     * Gets the values map.
     *
     * @return the values map
     */
    public Map<String, Object> getValuesMap() {
        return valuesMap;
    }

    /**
     * Sets the record values.
     *
     * @param properties the properties
     * @param values     the values
     */
    public void setRecordValues(final List<String> properties, final List<Object> values) {
        for (int i = 0; i < properties.size(); i++) {
            this.valuesMap.put(properties.get(i), values.get(i));
        }
    }

    /**
     * Adds the associate record.
     *
     * @param propertyName the property name
     * @param record       the record
     */
    public void addAssociateRecord(final String propertyName, final Record record) {
        final Property property = entity.getProperty(propertyName);
        if (PropertyType.PTY_LIST.equalsIgnoreCase(property.getType())) {
            List<Record> records = new ArrayList<>();
            if (valuesMap.containsKey(propertyName)) {
                records = (List<Record>) valuesMap.get(propertyName);
            } else {
                valuesMap.put(propertyName, records);
            }
            records.add(record);
        } else {
            valuesMap.put(propertyName, record);
        }

    }

    /**
     * Gets the record values.
     *
     * @return the record values
     */
    public List<Object> getRecordValues() {
        return new ArrayList<Object>(this.valuesMap.values());
    }

    /**
     * Gets the associate records.
     *
     * @param entity the entity
     * @return the associate records
     */
    public Object getAssociateRecords(final String entity) {
        if (valuesMap.containsKey(entity)) {
            return valuesMap.get(entity);
        }
        return null;
    }

    /**
     * Find child record.
     *
     * @param entity the entity
     * @param record the record
     * @return the record
     */
    public Record findChildRecord(final String entity, final Record record) {
        final Object obj = getAssociateRecords(entity);
        if (obj == null) {
            return null;
        }
        if (obj instanceof List) {
            for (final Record child : (List<Record>) obj) {
                if (child.equalTo(record)) {
                    return child;
                }
            }
        } else if (obj instanceof Record) {
            final Record child = (Record) obj;
            if (child.equalTo(record)) {
                return child;
            }
        }

        return null;
    }

    /**
     * Gets the entity.
     *
     * @return the entity
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Sets the entity.
     *
     * @param entity the new entity
     */
    public void setEntity(final Entity entity) {
        this.entity = entity;
    }

    /**
     * Equal to.
     *
     * @param record the record
     * @return true, if successful
     */
    public boolean equalTo(final Record record) {
        if (record == null) {
            return false;
        }

        if (!record.getEntity().getName().equalsIgnoreCase(this.getEntity().getName())) {
            return false;
        }

        // check PK id field values
        final Property idProperty = entity.getIdProperty();
        if (StringUtils.isNotBlank(idProperty.getColumn())) {
            final Property p = entity.getPropertyByColumn(idProperty.getColumn());
            final Object obj1 = this.getValuesMap().get(p.getName());
            final Object obj2 = record.getValuesMap().get(p.getName());
            if (!obj1.equals(obj2)) {
                return false;
            }
        } else if (!EngineUtil.isEmpty(idProperty.getColumns())) {
            for (final String idColumn : idProperty.getColumns()) {
                final Property p = entity.getPropertyByColumn(idColumn);
                final Object obj1 = this.getValuesMap().get(p.getName());
                final Object obj2 = record.getValuesMap().get(p.getName());
                if (!obj1.equals(obj2)) {
                    return false;
                }
            }
        }

        return true;
    }

}
