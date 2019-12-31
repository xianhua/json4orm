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

import java.util.HashMap;
import java.util.Map;

/**
 * The Class Schema defines data structure to represent schema object, and
 * functions to get and find entities by name or by a chain of entities
 * delimited by dot.
 *
 * @author Xianhua Liu
 */
public class Schema {

    /** The entities included in this schema. */
    Map<String, Entity> entities = new HashMap<>();

    /**
     * Adds the entity.
     *
     * @param entity the entity
     */
    public void addEntity(final Entity entity) {
        entities.put(entity.getName().toLowerCase(), entity);
    }

    /**
     * Gets the entity.
     *
     * @param name the name
     * @return the entity
     */
    public Entity getEntity(final String name) {
        return entities.get(name.toLowerCase());
    }

    /**
     * Gets the entities.
     *
     * @return the entities
     */
    public Map<String, Entity> getEntities() {
        return entities;
    }

    /**
     * Sets the entities.
     *
     * @param entities the entities
     */
    public void setEntities(final Map<String, Entity> entities) {
        this.entities = entities;
    }

    /**
     * Find entity by traveling through the entity name chain delimited by dot.
     *
     * @param propertyChain the property chain l=delimited by dot, such as
     *                      entity1.entity2.entity3
     * @return the entity if found, or null if not found
     */
    public Entity findEntity(final String propertyChain) {
        final String[] properties = propertyChain.split("\\.");
        Entity entity = getEntity(properties[0]);
        if (entity == null) {
            return null;
        }

        for (int i = 1; i < properties.length; i++) {
            final String p = properties[i];
            final Property property = entity.getProperty(p);
            if (property == null) {
                return null;
            }

            final Entity nextEntity = getEntity(property.getEntityType());
            if (nextEntity != null) {
                entity = nextEntity;
            }
        }

        return entity;
    }
}
