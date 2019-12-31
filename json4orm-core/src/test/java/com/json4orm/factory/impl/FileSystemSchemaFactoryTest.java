package com.json4orm.factory.impl;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

import com.json4orm.exception.Json4ormException;
import com.json4orm.model.schema.Entity;
import com.json4orm.model.schema.Property;
import com.json4orm.model.schema.Schema;

public class FileSystemSchemaFactoryTest {

    public FileSystemSchemaFactoryTest() {

    }

    @Test
    public void testCreateSchema() throws Json4ormException, URISyntaxException {
        final URL url = this.getClass().getClassLoader().getResource("entities");
        final File folder = new File(url.toURI());
        final FileSystemSchemaFactory schemaFactory = new FileSystemSchemaFactory(folder);
        final Schema schema = schemaFactory.createSchema();

        System.out.println(schema.getEntities().size());
        for (final Entity entity : schema.getEntities().values()) {
            System.out.println(entity.getName());

            for (final Property p : entity.getProperties()) {
                System.out.println("\t" + p.getName());
            }

            System.out.println("--------------------------------");
        }
    }

}
