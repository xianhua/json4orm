/**
 * Copyright 2020 Xianhua Liu
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.json4orm.factory.impl;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.json4orm.exception.Json4ormException;
import com.json4orm.factory.SchemaFactory;
import com.json4orm.model.schema.Entity;
import com.json4orm.model.schema.Schema;

/**
 * A schema factory implementation for creating Schema objects from all files in
 * a specific folder.
 */
public class FileSystemSchemaFactory implements SchemaFactory {

    /** The entities folder. */
    private File entitiesFolder;

    /** The Constant OBJ_MAPPER. */
    private static final ObjectMapper OBJ_MAPPER;
    static {
        OBJ_MAPPER = new ObjectMapper();
        OBJ_MAPPER.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    /**
     * Instantiates a new file system schema factory.
     */
    public FileSystemSchemaFactory() {
    }

    /**
     * Instantiates a new file system schema factory.
     *
     * @param entitiesFolder the entities folder
     */
    public FileSystemSchemaFactory(final String entitiesFolder) {
        super();
        this.entitiesFolder = new File(entitiesFolder);
    }

    /**
     * Instantiates a new file system schema factory.
     *
     * @param entitiesFolder the entities folder
     */
    public FileSystemSchemaFactory(final File entitiesFolder) {
        super();
        this.entitiesFolder = entitiesFolder;
    }

    /**
     * Creates a new Schema object.
     *
     * @return the schema contains all entities read and parsed from files under the
     *         entity folder
     * @throws Json4ormException when the eneity folder is not existed or is empty,
     *                           or entity in in invalid JSON format or structure.
     */
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

    /**
     * Gets the entities folder.
     *
     * @return the entities folder
     */
    public File getEntitiesFolder() {
        return entitiesFolder;
    }

    /**
     * Sets the entities folder.
     *
     * @param entitiesFolder the new entities folder
     */
    public void setEntitiesFolder(final File entitiesFolder) {
        this.entitiesFolder = entitiesFolder;
    }

}
