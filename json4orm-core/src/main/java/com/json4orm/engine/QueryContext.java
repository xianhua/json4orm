package com.json4orm.engine;

import java.util.List;

import com.json4orm.model.entity.Schema;
import com.json4orm.model.query.Query;

public class QueryContext {
    private String sql;
    private List<Object> values;
    private List<String> selectedFields;
    private Query query;
    private Schema schema;

    public String getSql() {
        return sql;
    }

    public void setSql(final String sql) {
        this.sql = sql;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(final List<Object> values) {
        this.values = values;
    }

    public List<String> getSelectedFields() {
        return selectedFields;
    }

    public void setSelectedFields(final List<String> selectedFields) {
        this.selectedFields = selectedFields;
    }

    public int getFieldIndex(final String field) {
        return selectedFields.indexOf(field) + 1;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(final Query query) {
        this.query = query;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(final Schema schema) {
        this.schema = schema;
    }
}
