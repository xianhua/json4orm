package com.json4orm.model.entity;

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

    public void setName(String name) {
        this.name = name;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public void addProperty(Property property) {
        properties.add(property);
    }
    
    public Property getProperty(String name) {
    	for (Property p : properties) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    public Property getIdProperty() {
        for (Property p : properties) {
            if (PropertyType.PTY_ID.equalsIgnoreCase(p.getType())) {
                return p;
            }
        }
        return null;
    }
    
    public List<Property> findPropertiesByType(String entityName){
    	List<Property> results = new ArrayList<>();
    	for(Property p: properties) {
    		if(entityName.equalsIgnoreCase(p.getType())) {
    			results.add(p);
    		}else if(entityName.equalsIgnoreCase(p.getItemType())) {
    			results.add(p);
    		}
    	}
    	
    	return results;
    }
}
