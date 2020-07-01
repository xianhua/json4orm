package com.json4orm.parser;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import com.json4orm.exception.Json4ormException;

public interface Parser<T> {
    public T parse(final InputStream inputStream) throws Json4ormException;
    public T parse(final File file) throws Json4ormException;
    public T parse(final String string) throws Json4ormException;
    public T parse(final Map<String, Object> jsonMap) throws Json4ormException;   
}
