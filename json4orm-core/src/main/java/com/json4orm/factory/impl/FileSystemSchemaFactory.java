package com.json4orm.factory.impl;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.json4orm.exception.Json4ormException;
import com.json4orm.factory.SchemaFactory;
import com.json4orm.model.entity.Entity;
import com.json4orm.model.entity.Schema;

public class FileSystemSchemaFactory implements SchemaFactory {
    private String entitiesFolder;
    private static final ObjectMapper OBJ_MAPPER;
    static {
        OBJ_MAPPER = new ObjectMapper();
        OBJ_MAPPER.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }
    public FileSystemSchemaFactory() {
    }

    public FileSystemSchemaFactory(String entitiesFolder) {
        super();
        this.entitiesFolder = entitiesFolder;
        
    }

    @Override
    public Schema createSchema() throws Json4ormException {
        if (entitiesFolder == null) {
            throw new Json4ormException("No folder specified for entities.");
        }
       
        Schema schema = new Schema();
        File dir = new File(entitiesFolder);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File file : directoryListing) {
                try {
                    Entity entity = OBJ_MAPPER.readValue(file, Entity.class);
                    schema.addEntity(entity);
                } catch (IOException e) {
                    throw new Json4ormException("Failed to create schema.", e);
                }
            }
        } else {
            throw new Json4ormException("No folder found: " + entitiesFolder);
        }
        return schema;
    }

    public String getEntitiesFolder() {
        return entitiesFolder;
    }

    public void setEntitiesFolder(String entitiesFolder) {
        this.entitiesFolder = entitiesFolder;
    }

    
}
