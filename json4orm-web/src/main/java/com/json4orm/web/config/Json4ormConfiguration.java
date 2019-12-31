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

package com.json4orm.web.config;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.json4orm.db.QueryExecutor;
import com.json4orm.db.impl.QueryExecutorImpl;
import com.json4orm.engine.impl.ValueConvertorImpl;
import com.json4orm.exception.Json4ormException;
import com.json4orm.factory.impl.FileSystemSchemaFactory;
import com.json4orm.model.schema.Schema;
import com.json4orm.model.schema.SchemaValidator;
import com.json4orm.parser.QueryParser;

@Configuration
@ConfigurationProperties(prefix = "json4orm")
public class Json4ormConfiguration {
    private static final Logger LOG = LogManager.getLogger(Json4ormConfiguration.class);
    
    private String entityFolder;
    private JdbcConfig jdbcConfig;

    public String getEntityFolder() {
        return entityFolder;
    }

    public void setEntityFolder(final String entityFolder) {
        this.entityFolder = entityFolder;
    }

    public JdbcConfig getJdbcConfig() {
        return jdbcConfig;
    }

    public void setJdbcConfig(final JdbcConfig jdbcConfig) {
        this.jdbcConfig = jdbcConfig;
    }

    @Bean
    public QueryExecutor queryExecutor() throws Json4ormException {
        LOG.debug("Initializing json4orm query executor.");
        final FileSystemSchemaFactory schemaFactory = new FileSystemSchemaFactory();
        schemaFactory.setEntitiesFolder(new File(entityFolder));
        final QueryExecutorImpl executor = new QueryExecutorImpl();
        final Schema schema = schemaFactory.createSchema();
        final List<String> errors = SchemaValidator.validate(schema);
        if(!errors.isEmpty()) {
             throw new Json4ormException("json4orm schema read from folder: "+ entityFolder+" is not valid due to following error(s): " + StringUtils.join(errors, ", "));
        }
        LOG.debug("Successfully read and validated schema in folder: " + entityFolder);
        executor.setSchema(schema);
        
        //check JDBC config
        if(StringUtils.isBlank(jdbcConfig.getDbUrl())) {
            throw new Json4ormException("json4orm.jdbcConfig.dbUrl is not defined in application.yml.");
        }
        if(StringUtils.isBlank(jdbcConfig.getDbUser())) {
            throw new Json4ormException("json4orm.jdbcConfig.dbDbUser is not defined in application.yml.");
        }
        if(StringUtils.isBlank(jdbcConfig.getDbPassword())) {
            throw new Json4ormException("json4orm.jdbcConfig.dbPassword is not defined in application.yml.");
        }
        executor.setDbPassword(jdbcConfig.getDbPassword());
        executor.setDbUrl(jdbcConfig.getDbUrl());
        executor.setDbUser(jdbcConfig.getDbUser());
        executor.setValueConvertor(new ValueConvertorImpl());

        return executor;
    }

    @Bean
    public QueryParser queryParser() {
        return new QueryParser();
    }
}
