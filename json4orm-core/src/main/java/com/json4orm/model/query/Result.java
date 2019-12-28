package com.json4orm.model.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.json4orm.model.entity.Entity;
import com.json4orm.model.entity.Property;

public class Result {
    private String propertyName;
    private Entity entity;
    private Property property;
    private String alias;
    private List<String> properties = new ArrayList<>();
    private List<Result> associates = new ArrayList<>();

    public Result() {
    }

    public List<String> getProperties() {
        if (properties == null) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableList(properties);
    }

    public void setProperties(final List<String> properties) {
        this.properties = properties;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(final String propertyName) {
        this.propertyName = propertyName;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(final Entity entity) {
        this.entity = entity;
    }

    public List<Result> getAssociates() {
        return associates;
    }

    public void setAssociates(final List<Result> associates) {
        this.associates = associates;
    }

    public void addAssociate(final Result result) {
        associates.add(result);
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(final String alias) {
        this.alias = alias;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(final Property property) {
        this.property = property;
    }

    public void addProperty(final String property) {
        this.properties.add(property);
    }
}
