package com.json4orm.engine;

import com.json4orm.exception.Json4ormException;
import com.json4orm.model.entity.Property;

public interface ValueConvertor {
    Object convert(Property property, Object value) throws Json4ormException;
}
