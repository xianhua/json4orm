package com.json4orm.model.schema;

import java.util.ArrayList;
import java.util.List;

public class Entity {
    private String name;
    private String table;
    private List<Property> properties = new ArrayList<>();

    public Entity() {
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getTable() {
        return table;
    }

    public void setTable(final String table) {
        this.table = table;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(final List<Property> properties) {
        this.properties = properties;
    }

    public void addProperty(final Property property) {
        properties.add(property);
    }

    public Property getProperty(final String name) {
        for (final Property p : properties) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    public Property getPropertyByColumn(final String name) {
        for (final Property p : properties) {
            if (p.getColumn().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    public Property getIdProperty() {
        for (final Property p : properties) {
            if (PropertyType.PTY_ID.equalsIgnoreCase(p.getType())) {
                return p;
            }
        }
        return null;
    }

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
    
    public List<Property> getOwnedPropeties(){
        final List<Property> ownedProperties = new ArrayList<>();
        for(final Property p: properties) {
            if(!PropertyType.PTY_LIST.equalsIgnoreCase(p.getType())) {
                ownedProperties.add(p);
            }
        }
        
        return ownedProperties;
        
    }
}
