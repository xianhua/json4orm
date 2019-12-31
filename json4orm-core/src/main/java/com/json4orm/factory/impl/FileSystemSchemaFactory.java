package com.json4orm.factory.impl;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.json4orm.exception.Json4ormException;
import com.json4orm.factory.SchemaFactory;
import com.json4orm.model.schema.Entity;
import com.json4orm.model.schema.Schema;

public class FileSystemSchemaFactory implements SchemaFactory {
    private File entitiesFolder;
    private static final ObjectMapper OBJ_MAPPER;
    static {
        OBJ_MAPPER = new ObjectMapper();
        OBJ_MAPPER.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    public FileSystemSchemaFactory() {
    }

    public FileSystemSchemaFactory(final String entitiesFolder) {
        super();
        this.entitiesFolder = new File(entitiesFolder);
    }

    public FileSystemSchemaFactory(final File entitiesFolder) {
        super();
        this.entitiesFolder = entitiesFolder;
    }

    @Override
    public Schema createSchema() throws Json4ormException {
        if (entitiesFolder == null) {
            throw new Json4ormException("No folder specified for entities.");
        }

        final Schema schema = new Schema();

        final File[] directoryListing = entitiesFolder.listFiles();
        if (directoryListing != null) {
            for (final File file : directoryListing) {
                try {
                    final Entity entity = OBJ_MAPPER.readValue(file, Entity.class);
                    schema.addEntity(entity);
                } catch (final IOException e) {
                    throw new Json4ormException("Failed to create schema.", e);
                }
            }
        } else {
            throw new Json4ormException("No folder found: " + entitiesFolder);
        }
        return schema;
    }

    public File getEntitiesFolder() {
        return entitiesFolder;
    }

    public void setEntitiesFolder(final File entitiesFolder) {
        this.entitiesFolder = entitiesFolder;
    }

}
