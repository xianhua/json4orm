package com.json4orm.db;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.json4orm.model.entity.Entity;
import com.json4orm.model.entity.Property;
import com.json4orm.model.entity.PropertyType;
import com.json4orm.util.EngineUtil;

public class Record {
    private Entity entity;
    private Map<String, Object> valuesMap = new LinkedHashMap<>();

    public Record(final Entity entity, final Map<String, Object> values) {
        this.entity = entity;
        this.valuesMap = values;
    }

    public Map<String, Object> getValuesMap() {
        return valuesMap;
    }

    public void setRecordValues(final List<String> properties, final List<Object> values) {
        for (int i = 0; i < properties.size(); i++) {
            this.valuesMap.put(properties.get(i), values.get(i));
        }
    }

    public void addAssociateRecord(final String propertyName, final Record record) {
        final Property property = entity.getProperty(propertyName);
        if (PropertyType.PTY_LIST.equalsIgnoreCase(property.getType())) {
            List<Record> records = new ArrayList<>();
            if (valuesMap.containsKey(propertyName)) {
                records = (List<Record>) valuesMap.get(propertyName);
            } else {
                valuesMap.put(propertyName, records);
            }
            records.add(record);
        } else {
            valuesMap.put(propertyName, record);
        }

    }

    public List<Object> getRecordValues() {
        return new ArrayList<Object>(this.valuesMap.values());
    }

    public Object getAssociateRecords(final String entity) {
        if (valuesMap.containsKey(entity)) {
            return valuesMap.get(entity);
        }
        return null;
    }

    public Record findChildRecord(final String entity, final Record record) {
        final Object obj = getAssociateRecords(entity);
        if (obj == null) {
            return null;
        }
        if (obj instanceof List) {
            for (final Record child : (List<Record>) obj) {
                if (child.equalTo(record)) {
                    return child;
                }
            }
        } else if (obj instanceof Record) {
            final Record child = (Record) obj;
            if (child.equalTo(record)) {
                return child;
            }
        }

        return null;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(final Entity entity) {
        this.entity = entity;
    }

    public boolean equalTo(final Record record) {
        if (record == null) {
            return false;
        }

        if (!record.getEntity().getName().equalsIgnoreCase(this.getEntity().getName())) {
            return false;
        }

        // check PK id field values
        final Property idProperty = entity.getIdProperty();
        if (StringUtils.isNotBlank(idProperty.getColumn())) {
            final Property p = entity.getPropertyByColumn(idProperty.getColumn());
            final Object obj1 = this.getValuesMap().get(p.getName());
            final Object obj2 = record.getValuesMap().get(p.getName());
            if (!obj1.equals(obj2)) {
                return false;
            }
        } else if (!EngineUtil.isEmpty(idProperty.getColumns())) {
            for (final String idColumn : idProperty.getColumns()) {
                final Property p = entity.getPropertyByColumn(idColumn);
                final Object obj1 = this.getValuesMap().get(p.getName());
                final Object obj2 = record.getValuesMap().get(p.getName());
                if (!obj1.equals(obj2)) {
                    return false;
                }
            }
        }

        return true;
    }

}
