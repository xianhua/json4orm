package com.json4orm.engine;

import com.json4orm.exception.Json4ormException;
import com.json4orm.model.entity.Schema;
import com.json4orm.model.query.Query;

public interface QueryBuilder {
    public String buildSqlQuery(Schema schema, Query query) throws Json4ormException;
}
