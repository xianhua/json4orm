package com.json4orm.factory.impl;

import org.junit.Test;

import com.json4orm.exception.Json4ormException;
import com.json4orm.model.entity.Entity;
import com.json4orm.model.entity.Property;
import com.json4orm.model.entity.Schema;

public class FileSystemSchemaFactoryTest {

    public FileSystemSchemaFactoryTest() {
        
    }
    
    @Test
    public void testCreateSchema() throws Json4ormException {
        String folder = "C:\\Users\\xliu1002\\workspace\\simple-graphql\\json4orm-core\\src\\main\\resources\\odx-provisioning";
        FileSystemSchemaFactory schemaFactory = new FileSystemSchemaFactory(folder);
        Schema schema = schemaFactory.createSchema();
        
        System.out.println(schema.getEntities().size());
        for(Entity entity: schema.getEntities().values()) {
            System.out.println(entity.getName());
            
            
            for(Property p: entity.getProperties()) {
                System.out.println("\t"+p.getName());
            }
            
            System.out.println("--------------------------------");
        }
    }

}
