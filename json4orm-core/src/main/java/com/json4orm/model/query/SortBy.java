package com.json4orm.model.query;

import com.json4orm.util.Constants;

public class SortBy {
    private String property;
    private String order = Constants.ORDER_ASC;

    public SortBy(final String property) {
        super();
        this.property = property;
    }

    public SortBy(final String property, final String order) {
        super();
        this.property = property;
        this.order = order;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(final String property) {
        this.property = property;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(final String order) {
        this.order = order;
    }

}
