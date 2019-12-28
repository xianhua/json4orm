package com.json4orm.db;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.json4orm.engine.QueryContext;
import com.json4orm.exception.Json4ormException;

public interface RecordBuilder {
    public List<Map<String, Object>> buildRecord(final ResultSet rs, final QueryContext context)
            throws Json4ormException;
}
