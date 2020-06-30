package com.json4orm.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.json4orm.exception.Json4ormException;
import com.json4orm.model.addupdate.AddOrUpdate;

public class AddOrUpdateParser {
    /** The Constant OBJ_MAPPER. */
    private static final ObjectMapper OBJ_MAPPER;
    static {
        OBJ_MAPPER = new ObjectMapper();
        OBJ_MAPPER.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    /**
     * Parses the InputStream into AddOrUpdate object.
     *
     * @param inputStream the input stream
     * @return the AddOrUpdate object
     * @throws Json4ormException the json4orm exception
     */
    public AddOrUpdate parse(final InputStream inputStream) throws Json4ormException {
        try {
            return  OBJ_MAPPER.readValue(inputStream, AddOrUpdate.class);
        } catch (final IOException e) {
            throw new Json4ormException(e);
        }
    }

    /**
     * Parses file contents into AddOrUpdate object.
     *
     * @param queryFile the query file
     * @return the AddOrUpdate object
     * @throws Json4ormException the json4orm exception
     */
    public AddOrUpdate parse(final File queryFile) throws Json4ormException {
        try {
            return  OBJ_MAPPER.readValue(queryFile, AddOrUpdate.class);
        } catch (final IOException e) {
            throw new Json4ormException(e);
        }
    }

    /**
     * Parses string into AddOrUpdate object
     *
     * @param string the json string
     * @return the AddOrUpdate object
     * @throws Json4ormException the json4orm exception
     */
    public AddOrUpdate parse(final String string) throws Json4ormException {
        try {
            return  OBJ_MAPPER.readValue(string, AddOrUpdate.class);
        } catch (final JsonProcessingException e) {
            throw new Json4ormException(e);
        }
    }
}
