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

import java.util.List;

import com.json4orm.model.query.Query;
import com.json4orm.model.schema.Schema;

/**
 * The Class QueryContext.
 * 
 * @author Xianhua Liu
 */
public class QueryContext {
    
    /** The sql. */
    private String sql;
    
    /** The count sql. */
    private String countSql;
    
    /** The limit sql. */
    private String limitSql;
    
    /** The values. */
    private List<Object> values;
    
    /** The selected fields. */
    private List<String> selectedFields;
    
    /** The query. */
    private Query query;
    
    /** The schema. */
    private Schema schema;

    /**
     * Gets the sql.
     *
     * @return the sql
     */
    public String getSql() {
        return sql;
    }

    /**
     * Sets the sql.
     *
     * @param sql the new sql
     */
    public void setSql(final String sql) {
        this.sql = sql;
    }

    /**
     * Gets the count sql.
     *
     * @return the count sql
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
     * Gets the limit sql.
     *
     * @return the limit sql
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
     * Gets the values.
     *
     * @return the values
     */
    public List<Object> getValues() {
        return values;
    }

    /**
     * Sets the values.
     *
     * @param values the new values
     */
    public void setValues(final List<Object> values) {
        this.values = values;
    }

    /**
     * Gets the selected fields.
     *
     * @return the selected fields
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
     * Gets the field index.
     *
     * @param field the field
     * @return the field index
     */
    public int getFieldIndex(final String field) {
        return selectedFields.indexOf(field) + 1;
    }

    /**
     * Gets the query.
     *
     * @return the query
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
}
