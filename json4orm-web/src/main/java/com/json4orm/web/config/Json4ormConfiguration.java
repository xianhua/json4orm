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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.json4orm.db.QueryExecutor;
import com.json4orm.db.impl.QueryExecutorImpl;
import com.json4orm.engine.impl.ValueConvertorImpl;
import com.json4orm.exception.Json4ormException;
import com.json4orm.factory.impl.FileSystemSchemaFactory;
import com.json4orm.model.schema.Schema;
import com.json4orm.model.schema.SchemaValidator;
import com.json4orm.parser.QueryParser;

/**
 * The Class Json4ormConfiguration holds configurations read from the
 * application.yml file with prefix "json4orm" and generate beans based on the
 * configurations, including QueryExecutor and QueryParser.
 * 
 * <P>
 * The json4orm configuration can be updated in the application.yml file in the
 * config folder under the same folder where the application jar is deployed.
 * </P>
 * 
 * <P>
 * Here is an example.
 * </P>
 * json4orm: entity-folder: entities jdbc-config: db-url:
 * jdbc:postgresql://localhost:5432/postgres db-user: postgres db-password:
 * postgres allowed-origins: - http://localhost:8080
 * 
 * @author Xianhua Liu
 */
@Configuration
@ConfigurationProperties(prefix = "json4orm")
public class Json4ormConfiguration {

    /** The Constant LOG. */
    private static final Logger LOG = LogManager.getLogger(Json4ormConfiguration.class);

    /** The entity folder. */
    private String entityFolder;

    /** The jdbc config. */
    private JdbcConfig jdbcConfig;

    List<String> allowedOrigins = new ArrayList<>();

    /**
     * Gets the entity folder.
     *
     * @return the entity folder
     */
    public String getEntityFolder() {
        return entityFolder;
    }

    /**
     * Sets the entity folder.
     *
     * @param entityFolder the new entity folder
     */
    public void setEntityFolder(final String entityFolder) {
        this.entityFolder = entityFolder;
    }

    /**
     * Gets the jdbc config.
     *
     * @return the jdbc config
     */
    public JdbcConfig getJdbcConfig() {
        return jdbcConfig;
    }

    /**
     * Sets the jdbc config.
     *
     * @param jdbcConfig the new jdbc config
     */
    public void setJdbcConfig(final JdbcConfig jdbcConfig) {
        this.jdbcConfig = jdbcConfig;
    }

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(final List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    /**
     * Query executor.
     *
     * @return the query executor
     * @throws Json4ormException when schema is invalid or required configurations
     *                           are missing including dbUser, dbPassword and dbUrl.
     */
    @Bean
    public QueryExecutor queryExecutor() throws Json4ormException {
        LOG.debug("Initializing json4orm query executor.");
        final FileSystemSchemaFactory schemaFactory = new FileSystemSchemaFactory();
        schemaFactory.setEntitiesFolder(new File(entityFolder));
        final QueryExecutorImpl executor = new QueryExecutorImpl();
        final Schema schema = schemaFactory.createSchema();
        final List<String> errors = SchemaValidator.validate(schema);
        if (!errors.isEmpty()) {
            throw new Json4ormException("json4orm schema read from folder: " + entityFolder
                    + " is not valid due to following error(s): " + StringUtils.join(errors, ", "));
        }
        LOG.debug("Successfully read and validated schema in folder: " + entityFolder);
        executor.setSchema(schema);

        // check JDBC config
        if (StringUtils.isBlank(jdbcConfig.getDbUrl())) {
            throw new Json4ormException("json4orm.jdbcConfig.dbUrl is not defined in application.yml.");
        }
        if (StringUtils.isBlank(jdbcConfig.getDbUser())) {
            throw new Json4ormException("json4orm.jdbcConfig.dbDbUser is not defined in application.yml.");
        }
        if (StringUtils.isBlank(jdbcConfig.getDbPassword())) {
            throw new Json4ormException("json4orm.jdbcConfig.dbPassword is not defined in application.yml.");
        }
        executor.setDbPassword(jdbcConfig.getDbPassword());
        executor.setDbUrl(jdbcConfig.getDbUrl());
        executor.setDbUser(jdbcConfig.getDbUser());
        executor.setValueConvertor(new ValueConvertorImpl());

        return executor;
    }

    /**
     * Query parser.
     *
     * @return the query parser
     */
    @Bean
    public QueryParser queryParser() {
        return new QueryParser();
    }

    @Bean
    public CorsFilter corsFilter() {
        final CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        if (allowedOrigins == null || allowedOrigins.isEmpty()) {
            config.setAllowedOrigins(Arrays.asList("*"));
        } else {
            config.setAllowedOrigins(this.allowedOrigins);
        }
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "HEAD", "PATCH"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
