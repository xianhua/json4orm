package com.json4orm.web.config;

import java.io.File;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.json4orm.db.QueryExecutor;
import com.json4orm.db.impl.QueryExecutorImpl;
import com.json4orm.engine.impl.ValueConvertorImpl;
import com.json4orm.exception.Json4ormException;
import com.json4orm.factory.impl.FileSystemSchemaFactory;
import com.json4orm.parser.QueryParser;

@Configuration
@ConfigurationProperties(prefix = "json4orm")
public class Json4ormConfiguration {
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
        final FileSystemSchemaFactory schemaFactory = new FileSystemSchemaFactory();
        schemaFactory.setEntitiesFolder(new File(entityFolder));
        final QueryExecutorImpl executor = new QueryExecutorImpl();
        executor.setSchema(schemaFactory.createSchema());
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
