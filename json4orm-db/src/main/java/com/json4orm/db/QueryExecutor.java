package com.json4orm.db;

import java.util.List;
import java.util.Map;

import com.json4orm.exception.Json4ormException;
import com.json4orm.model.query.Query;

public interface QueryExecutor {
    public List<Map<String, Object>> execute(final Query query) throws Json4ormException;
}
