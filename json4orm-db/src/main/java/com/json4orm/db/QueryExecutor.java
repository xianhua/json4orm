package com.json4orm.db;

import com.json4orm.exception.Json4ormException;
import com.json4orm.model.query.Query;

public interface QueryExecutor {
    public QueryResult execute(final Query query) throws Json4ormException;
}
