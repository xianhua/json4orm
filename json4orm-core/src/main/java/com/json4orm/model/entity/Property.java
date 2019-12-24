package com.json4orm.model.entity;

public class Property {
    private String name;
    private String type;
    private String column;
    private String itemType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }
    
    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
    
    public String getEntityType() {
    	if(PropertyType.PTY_LIST.equalsIgnoreCase(type)) {
    		return this.itemType;
    	}
    	
    	return type;
    }
}