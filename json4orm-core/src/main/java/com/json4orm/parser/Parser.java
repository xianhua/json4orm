package com.json4orm.parser;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import com.json4orm.exception.Json4ormException;
import com.json4orm.model.query.Query;

public interface Parser {
    public Query parse(final InputStream inputStream) throws Json4ormException;
    public Query parse(final File file) throws Json4ormException;
    public Query parse(final String string) throws Json4ormException;
    public Query parse(final Map<String, Object> jsonMap) throws Json4ormException;   
}
