package com.json4orm.db.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.json4orm.db.Record;
import com.json4orm.db.Record2JsonUtil;
import com.json4orm.db.RecordBuilder;
import com.json4orm.engine.QueryContext;
import com.json4orm.exception.Json4ormException;
import com.json4orm.model.entity.Entity;
import com.json4orm.model.query.Result;

public class RecordBuilderImpl implements RecordBuilder {
    private final List<Record> records = new ArrayList<>();

    public List<Record> getRecords() {
        return records;
    }

    @Override
    public List<Map<String, Object>> buildRecord(final ResultSet rs, final QueryContext context)
            throws Json4ormException {
        if (rs == null) {
            return null;
        }

        try {
            while (rs.next()) {
                buildTopLevelRecord(rs, context.getQuery().getResult(), context);
            }
        } catch (final SQLException e) {
            throw new Json4ormException("Failed to build records.", e);
        }

        return Record2JsonUtil.toJsonObject(getRecords());
    }

    private void buildTopLevelRecord(final ResultSet rs, final Result result, final QueryContext context)
            throws Json4ormException {
        Record record = retrieveValues(rs, result, context);
        final Record existingRecord = findRecord(record);
        if (existingRecord == null) {
            records.add(record);
        } else {
            record = existingRecord;
        }

        for (final Result associatedResult : result.getAssociates()) {
            buildRecord(rs, associatedResult, context, record);
        }
    }

    public void buildRecord(final ResultSet rs, final Result result, final QueryContext context, final Record parent)
            throws Json4ormException {
        Record record = retrieveValues(rs, result, context);
        final Record existingRecord = parent.findChildRecord(result.getPropertyName(), record);
        if (existingRecord == null) {
            parent.addAssociateRecord(result.getPropertyName(), record);
        } else {
            record = existingRecord;
        }

        for (final Result associatedResult : result.getAssociates()) {
            buildRecord(rs, associatedResult, context, record);
        }
    }

    private Record retrieveValues(final ResultSet rs, final Result result, final QueryContext context)
            throws Json4ormException {

        final Map<String, Object> values = new LinkedHashMap<>();

        final String alias = result.getAlias();
        final List<String> properties = result.getProperties();
        if (properties != null && !properties.isEmpty()) {
            for (final String property : properties) {
                final String fieldName = alias + "." + property;
                final int index = context.getFieldIndex(fieldName);
                if (index == 0) {
                    throw new Json4ormException("No field found for: " + fieldName);
                }
                try {
                    values.put(property, rs.getObject(index));
                } catch (final SQLException e) {
                    throw new Json4ormException("Failed to retrieve value for: " + fieldName, e);
                }
            }
        }

        final Entity entity = result.getEntity();
        final Record record = new Record(entity, values);
        return record;
    }

    private Record findRecord(final Record record) {
        for (final Record r : records) {
            if (r.equalTo(record)) {
                return r;
            }
        }

        return null;
    }
}
