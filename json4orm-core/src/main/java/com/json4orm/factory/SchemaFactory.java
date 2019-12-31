package com.json4orm.factory;

import com.json4orm.exception.Json4ormException;
import com.json4orm.model.schema.Schema;

public interface SchemaFactory {
    public Schema createSchema() throws Json4ormException;
}
