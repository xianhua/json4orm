package com.json4orm.engine.impl;

import com.json4orm.engine.QueryBuilder;
import com.json4orm.exception.Json4ormException;
import com.json4orm.model.entity.Schema;
import com.json4orm.model.query.Query;

public class QueryBuilderImpl implements QueryBuilder {

    public QueryBuilderImpl() {
    }

    @Override
    public String buildSqlQuery(Schema schema, Query query) throws Json4ormException {
        return null;
    }

}
