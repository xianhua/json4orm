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

/**
 * The Class Entity defines data and functions of Entity as mapped to database
 * tables, including its name, database table name to map to, and a list of
 * properties.
 * 
 * <P>
 * One and only one property should be of type 'ID', which defines the unique
 * identifier for the record.
 * </P>
 *
 * @author Xianhua Liu
 */
public class Entity {

    /** The name. */
    private String name;

    /** The table. */
    private String table;

    /** The properties. */
    private List<Property> properties = new ArrayList<>();

    /**
     * Instantiates a new entity.
     */
    public Entity() {
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets the table.
     *
     * @return the table
     */
    public String getTable() {
        return table;
    }

    /**
     * Sets the table.
     *
     * @param table the new table
     */
    public void setTable(final String table) {
        this.table = table;
    }

    /**
     * Gets the properties.
     *
     * @return the properties
     */
    public List<Property> getProperties() {
        return properties;
    }

    /**
     * Sets the properties.
     *
     * @param properties the new properties
     */
    public void setProperties(final List<Property> properties) {
        this.properties = properties;
    }

    /**
     * Adds the property.
     *
     * @param property the property
     */
    public void addProperty(final Property property) {
        properties.add(property);
    }

    /**
     * Gets the property.
     *
     * @param name the name
     * @return the property
     */
    public Property getProperty(final String name) {
        for (final Property p : properties) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Gets the property by column.
     *
     * @param name the name
     * @return the property by column
     */
    public Property getPropertyByColumn(final String name) {
        for (final Property p : properties) {
            if (p.getColumn().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Gets the id property.
     *
     * @return the id property
     */
    public Property getIdProperty() {
        for (final Property p : properties) {
            if (PropertyType.PTY_ID.equalsIgnoreCase(p.getType())) {
                return p;
            }
        }
        return null;
    }

    /**
     * Find properties by type.
     *
     * @param entityName the entity name
     * @return the list
     */
    public List<Property> findPropertiesByType(final String entityName) {
        final List<Property> results = new ArrayList<>();
        for (final Property p : properties) {
            if (entityName.equalsIgnoreCase(p.getType())) {
                results.add(p);
            } else if (entityName.equalsIgnoreCase(p.getItemType())) {
                results.add(p);
            }
        }

        return results;
    }

    /**
     * Gets the owned propeties.
     *
     * @return the owned propeties
     */
    public List<Property> getOwnedPropeties() {
        final List<Property> ownedProperties = new ArrayList<>();
        for (final Property p : properties) {
            if (!PropertyType.PTY_LIST.equalsIgnoreCase(p.getType())) {
                ownedProperties.add(p);
            }
        }

        return ownedProperties;

    }
}
