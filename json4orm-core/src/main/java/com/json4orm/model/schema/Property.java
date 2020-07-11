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

import java.util.List;

/**
 * The Class Property defines data structure that is mapped to field of a
 * database table, including the name, type, column for individual field or a
 * list of column names for composite ID field, and item type for associated
 * entities when one-to-many relationship exists.
 * 
 * <P>
 * The type can be any defined entity, or primitive types. Primitive types are
 * defined in {@link com.json4orm.model.schema.PropertyType}, which includes:
 * </P>
 * <ul>
 * <li>id: identifier, usually is the primary key field, or composite key.</li>
 * <li>string</li>
 * <li>byte</li>
 * <li>short</li>
 * <li>integer</li>
 * <li>long</li>
 * <li>float</li>
 * <li>double</li>
 * <li>boolean</li>
 * <li>date: in format: yyyy-MM-dd, such as 2019-12-30</li>
 * <li>time: in format hh:mm:ss</li>
 * <li>datetime: in format: yyyy-MM-dd'T'HH:mm:ss.SSSX, such as 2019-12-30T23:42:13.056+1000</li>
 * <li>timestamp: in format: yyyy-MM-dd'T'HH:mm:ss.SSSX, such as 2019-12-30T23:42:13.056+1000</li>
 * <li>list: a list of associated entity as defined by the itemType</li>
 * </ul>
 * 
 * @author Xianhua Liu
 */
public class Property {

    /** The name. */
    private String name;

    /** The type. */
    private String type;

    /** The column. */
    private String column;

    /** The columns. */
    private List<String> columns;

    /** The item type. */
    private String itemType;

    private String idGenerator;
    
    private String sequenceName;
    
    private boolean required = false;
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
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type the new type
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * Gets the column.
     *
     * @return the column
     */
    public String getColumn() {
        return column;
    }

    /**
     * Sets the column.
     *
     * @param column the new column
     */
    public void setColumn(final String column) {
        this.column = column;
    }

    /**
     * Gets the item type.
     *
     * @return the item type
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * Sets the item type.
     *
     * @param itemType the new item type
     */
    public void setItemType(final String itemType) {
        this.itemType = itemType;
    }

    /**
     * Gets the entity type.
     *
     * @return the entity type
     */
    public String getEntityType() {
        if (PropertyType.PTY_LIST.equalsIgnoreCase(type)) {
            return this.itemType;
        }

        return type;
    }

    /**
     * Gets the columns.
     *
     * @return the columns
     */
    public List<String> getColumns() {
        return columns;
    }

    /**
     * Sets the columns.
     *
     * @param columns the new columns
     */
    public void setColumns(final List<String> columns) {
        this.columns = columns;
    }

    public String getIdGenerator() {
        return idGenerator;
    }

    public void setIdGenerator(final String idGenerator) {
        this.idGenerator = idGenerator;
    }

    public String getSequenceName() {
        return sequenceName;
    }

    public void setSequenceName(final String sequenceName) {
        this.sequenceName = sequenceName;
    }

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

    
}