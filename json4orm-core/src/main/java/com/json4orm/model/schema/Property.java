package com.json4orm.model.schema;

import java.util.List;

public class Property {
    private String name;
    private String type;
    private String column;
    private List<String> columns;
    private String itemType;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(final String column) {
        this.column = column;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(final String itemType) {
        this.itemType = itemType;
    }

    public String getEntityType() {
        if (PropertyType.PTY_LIST.equalsIgnoreCase(type)) {
            return this.itemType;
        }

        return type;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(final List<String> columns) {
        this.columns = columns;
    }

}