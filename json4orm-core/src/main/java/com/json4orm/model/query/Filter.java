package com.json4orm.model.query;

import java.util.ArrayList;
import java.util.List;

public class Filter {
    private String property;
    private String operator;
    private Object value;
    private List<Filter> filters = new ArrayList<>();

    public Filter() {
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public void addFilter(Filter filter) {
        filters.add(filter);
    }

}
