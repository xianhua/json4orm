package com.json4orm.engine;

import com.json4orm.exception.Json4ormException;
import com.json4orm.model.query.Query;

public interface Visitor {
    public QueryContext visit(Query query) throws Json4ormException;
}
