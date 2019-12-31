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
package com.json4orm.model.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.json4orm.model.schema.Entity;
import com.json4orm.model.schema.Property;

/**
 * The Class Result defines data structure for querying result template, which
 * contains what entities and properties the client wants the result to include. 
 *
 * @author Xianhua Liu
 */
public class Result {

    /** The property name. */
    private String propertyName;

    /** The entity. */
    private Entity entity;

    /** The property. */
    private Property property;

    /** The alias. */
    private String alias;

    /** The properties. */
    private List<String> properties = new ArrayList<>();

    /** The associates. */
    private List<Result> associates = new ArrayList<>();

    /**
     * Instantiates a new result.
     */
    public Result() {
    }

    /**
     * Gets the properties.
     *
     * @return the properties
     */
    public List<String> getProperties() {
        if (properties == null) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableList(properties);
    }

    /**
     * Sets the properties.
     *
     * @param properties the new properties
     */
    public void setProperties(final List<String> properties) {
        this.properties = properties;
    }

    /**
     * Gets the property name.
     *
     * @return the property name
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Sets the property name.
     *
     * @param propertyName the new property name
     */
    public void setPropertyName(final String propertyName) {
        this.propertyName = propertyName;
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
     * Gets the associates.
     *
     * @return the associates
     */
    public List<Result> getAssociates() {
        return associates;
    }

    /**
     * Sets the associates.
     *
     * @param associates the new associates
     */
    public void setAssociates(final List<Result> associates) {
        this.associates = associates;
    }

    /**
     * Adds the associate.
     *
     * @param result the result
     */
    public void addAssociate(final Result result) {
        associates.add(result);
    }

    /**
     * Gets the alias.
     *
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Sets the alias.
     *
     * @param alias the new alias
     */
    public void setAlias(final String alias) {
        this.alias = alias;
    }

    /**
     * Gets the property.
     *
     * @return the property
     */
    public Property getProperty() {
        return property;
    }

    /**
     * Sets the property.
     *
     * @param property the new property
     */
    public void setProperty(final Property property) {
        this.property = property;
    }

    /**
     * Adds the property.
     *
     * @param property the property
     */
    public void addProperty(final String property) {
        this.properties.add(property);
    }
}
