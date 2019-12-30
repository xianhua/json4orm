package com.json4orm.model.entity;

import java.util.HashMap;
import java.util.Map;

public class Schema {
    Map<String, Entity> entities = new HashMap<>();

    public void addEntity(final Entity entity) {
        entities.put(entity.getName().toLowerCase(), entity);
    }

    public Entity getEntity(final String name) {
        return entities.get(name.toLowerCase());
    }

    public Map<String, Entity> getEntities() {
        return entities;
    }

    public void setEntities(final Map<String, Entity> entities) {
        this.entities = entities;
    }

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
