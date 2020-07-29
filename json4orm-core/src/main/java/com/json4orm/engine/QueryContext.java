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
package com.json4orm.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.json4orm.model.query.Action;
import com.json4orm.model.query.Query;
import com.json4orm.model.schema.Entity;
import com.json4orm.model.schema.Schema;

/**
 * The Class QueryContext contains contains all required objects supporting
 * query execution.
 * 
 * @author Xianhua Liu
 */
public class QueryContext {

    /** The entity. */
    private Entity entity;
    
    /** The sql string for querying records. */
    private String searchSql;

    /** The sql string for querying total count. */
    private String countSql;

    /** The sql string for querying records within the range defined by offset and limit. */
    private String limitSql;

    /** The insert sql. */
    private String insertSql;
    
    /** The update sql. */
    private String updateSql;
    
    /** The delete sql. */
    private String deleteSql;
    
    /** The values as querying statement parameters. */
    private List<Object> values;

    /** The selected fields for qury. */
    private List<String> selectedFields;

    /** The query object as passed in from client. */
    private Query query;

    /** The schema contains all entities. */
    private Schema schema;

    /** The insert records. */
    private final List<List<Object>> insertRecords = new ArrayList<>();
    
    /** The update records. */
    private final List<List<Object>> updateRecords = new ArrayList<>();
    
    private final List<Map<String, Object>> insertData = new ArrayList<>();
    
    /** The update records. */
    private final List<Map<String, Object>> updateData = new ArrayList<>();
    
    /** The id. */
    private Object id;
    

    /**
     * Gets the SQL string for querying data.
     *
     * @return the search sql string for querying data
     */
    public String getSearchSql() {
        return searchSql;
    }

    /**
     * Sets the search Sql.
     *
     * @param searchSql the new searchSql
     */
    public void setSearchSql(final String searchSql) {
        this.searchSql = searchSql;
    }

    /**
     * Gets the count sql for querying total count of records.
     *
     * @return the count sql string for querying total count of records.
     */
    public String getCountSql() {
        return countSql;
    }

    /**
     * Sets the count sql.
     *
     * @param countSql the new count sql
     */
    public void setCountSql(final String countSql) {
        this.countSql = countSql;
    }

    /**
     * Gets the limit sql for querying records with limit and offset.
     *
     * @return the limit sql for querying records with limit and offset.
     */
    public String getLimitSql() {
        return limitSql;
    }

    /**
     * Sets the limit sql.
     *
     * @param limitSql the new limit sql
     */
    public void setLimitSql(final String limitSql) {
        this.limitSql = limitSql;
    }

    /**
     * Gets the values used for querying parameters of PreparedStatement for count
     * and limit queries.
     *
     * @return the values used for querying parameters of PreparedStatement for
     *         count and limit queries
     */
    public List<Object> getValues() {
        return values;
    }

    /**
     * Sets the values used for querying parameters of PreparedStatement for count
     * and limit queries.
     *
     * @param values the new values used for querying parameters of
     *               PreparedStatement for count and limit queries.
     */
    public void setValues(final List<Object> values) {
        this.values = values;
    }

    /**
     * Gets the selected fields for the query.
     *
     * @return the selected fields for the query. Each field contains table alias
     *         and column name, like alias.column
     */
    public List<String> getSelectedFields() {
        return selectedFields;
    }

    /**
     * Sets the selected fields.
     *
     * @param selectedFields the new selected fields
     */
    public void setSelectedFields(final List<String> selectedFields) {
        this.selectedFields = selectedFields;
    }

    /**
     * Gets the field index in the selectedFields list.
     *
     * @param field the field name
     * @return the field index in the selectedFields list
     */
    public int getFieldIndex(final String field) {
        return selectedFields.indexOf(field) + 1;
    }

    /**
     * Gets the query.
     *
     * @return the query object 
     */
    public Query getQuery() {
        return query;
    }

    /**
     * Sets the query.
     *
     * @param query the new query
     */
    public void setQuery(final Query query) {
        this.query = query;
    }

    /**
     * Gets the schema.
     *
     * @return the schema
     */
    public Schema getSchema() {
        return schema;
    }

    /**
     * Sets the schema.
     *
     * @param schema the new schema
     */
    public void setSchema(final Schema schema) {
        this.schema = schema;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(final Entity entity) {
        this.entity = entity;
    }

    public String getInsertSql() {
        return insertSql;
    }

    public void setInsertSql(final String insertSql) {
        this.insertSql = insertSql;
    }

    public String getUpdateSql() {
        return updateSql;
    }

    public void setUpdateSql(final String updateSql) {
        this.updateSql = updateSql;
    }

    public String getDeleteSql() {
        return deleteSql;
    }

    public void setDeleteSql(final String deleteSql) {
        this.deleteSql = deleteSql;
    }

    public Object getId() {
        return id;
    }

    public void setId(final Object object) {
        this.id = object;
    }

    public List<List<Object>> getInsertRecords() {
        return insertRecords;
    }

    public List<List<Object>> getUpdateRecords() {
        return updateRecords;
    }
    
    public void addInsertRecord(final List<Object> record) {
        this.insertRecords.add(record);
    }
    
    public void addUpdateRecord(final List<Object> record) {
        this.updateRecords.add(record);
    }
    
    public void addInsertData(final Map<String, Object> record) {
        this.insertData.add(record);
    }
    
    public void addUpdateData(final Map<String, Object> record) {
        this.updateData.add(record);
    }
    
    public Action getAction() {
        return query.getAction();
    }

	public List<Map<String, Object>> getInsertData() {
		return insertData;
	}

	public List<Map<String, Object>> getUpdateData() {
		return updateData;
	}
    
    
}
