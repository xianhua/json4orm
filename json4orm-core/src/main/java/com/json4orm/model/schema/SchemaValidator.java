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
package com.json4orm.model.schema;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.json4orm.util.EngineUtil;

/**
 * The Class SchemaValidator validates a schema, including all entities and
 * their properties.
 *
 * @author Xianhua Liu
 */
public class SchemaValidator {

    /** The Constant PROPERTY_NAME_PATTERN. */
    public static final String PROPERTY_NAME_PATTERN = "^[a-zA-Z_][a-zA-Z_0-9]*$";

    /**
     * Validate.
     *
     * @param schema the schema
     * @return the list
     */
    public static List<String> validate(final Schema schema) {

        final List<String> errors = new ArrayList<>();
        if (schema == null) {
            errors.add("No schema defined.");
            return errors;
        }

        if (EngineUtil.isEmpty(schema.getEntities())) {
            errors.add("No entity defined.");
            return errors;
        }

        for (final Entity entity : schema.getEntities().values()) {
            errors.addAll(validate(entity, schema));
        }
        return errors;

    }

    /**
     * Validate.
     *
     * @param entity the entity
     * @param schema the schema
     * @return the list
     */
    public static List<String> validate(final Entity entity, final Schema schema) {
        final List<String> errors = new ArrayList<>();
        if (entity == null) {
            errors.add("Entity is null.");
            return errors;
        }
        String entityName = entity.getName();
        if (StringUtils.isBlank(entityName)) {
            errors.add("No name defined for entity.");
            entityName = "Unknown Entity";
        } else {
            if (!entityName.matches(PROPERTY_NAME_PATTERN)) {
                errors.add("Invalid characters used for entity name: " + entity.getName());
            }
        }

        if (StringUtils.isBlank(entity.getTable())) {
            errors.add(entityName + ": No table name defined for entity.");
        }

        if (EngineUtil.isEmpty(entity.getProperties())) {
            errors.add(entityName + ": no property defined.");
            return errors;
        }

        int index = 0;
        boolean hasIdProperty = false;
        for (final Property property : entity.getProperties()) {
            index++;
            errors.addAll(validate(property, index, entityName, schema));
            // check ID property
            if (PropertyType.PTY_ID.equalsIgnoreCase(property.getType())) {
                hasIdProperty = true;
            }
        }

        if (!hasIdProperty) {
            errors.add(entityName + ": No ID property defined.");
        }

        return errors;
    }

    /**
     * Validate.
     *
     * @param property   the property
     * @param index      the index
     * @param entityName the entity name
     * @param schema     the schema
     * @return the list
     */
    public static List<String> validate(final Property property, final int index, final String entityName,
            final Schema schema) {
        final List<String> errors = new ArrayList<>();
        String propertyName = property.getName();
        if (StringUtils.isBlank(property.getName())) {
            errors.add(entityName + ": No property name defined for " + index + "th property.");
            propertyName = "unknown";
        } else {
            if (!property.getName().matches(PROPERTY_NAME_PATTERN)) {
                errors.add(entityName + ": Invalid characters used for property name: " + property.getName());
            }
        }
        if (StringUtils.isBlank(property.getType())) {
            errors.add(entityName + "." + propertyName + ": No property type defined.");
        }
        if (!PropertyType.isTypeValid(property.getType()) && schema.getEntity(property.getType()) == null) {
            errors.add(entityName + "." + propertyName + ": Invalid property type: " + property.getType());
        }
        if (!PropertyType.PTY_ID.equalsIgnoreCase(property.getType()) && StringUtils.isBlank(property.getColumn())) {
            errors.add(entityName + "." + propertyName + ": No column defined.");
        }

        if (PropertyType.PTY_ID.equalsIgnoreCase(property.getType()) && StringUtils.isBlank(property.getColumn())
                && EngineUtil.isEmpty(property.getColumns())) {
            errors.add(entityName + "." + propertyName + ": No column or columns defined.");
        }

        if (PropertyType.PTY_LIST.equalsIgnoreCase(property.getType())) {
            if (StringUtils.isBlank(property.getItemType())) {
                errors.add(entityName + "." + propertyName + ": No itemType defined.");
            } else if (schema.getEntity(property.getItemType()) == null) {
                errors.add(entityName + "." + propertyName + ": No entity found with name: " + property.getItemType());
            }
        }

        return errors;
    }
}
