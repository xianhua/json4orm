package com.json4orm.model.entity;

import java.util.HashMap;
import java.util.Map;

public class Schema {
    Map<String, Entity> entities = new HashMap<>();
    
    public void addEntity(Entity entity) {
        entities.put(entity.getName(), entity);
    }
    
    public Entity getEntity(String name) {
        return entities.get(name);
    }

    public Map<String, Entity> getEntities() {
        return entities;
    }

    public void setEntities(Map<String, Entity> entities) {
        this.entities = entities;
    }
    
    public Entity findEntity(String propertyChain) {   	
    	String[] properties = propertyChain.split("\\.");
    	Entity entity = getEntity(properties[0]);
    	if(entity==null) {
    	  return null;
    	}
    	
    	for(int i=1; i<properties.length; i++) {
    		String p = properties[i];
    		Property property = entity.getProperty(p);
	    	if(property==null) {
	    	  return null;
	    	}
	    		
	    	Entity nextEntity = getEntity(property.getEntityType());
	    	if(nextEntity != null) {
	    		entity = nextEntity;
	    	}
    	}
    	
    	return entity;
    }
}
