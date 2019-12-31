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
package com.json4orm.db.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.json4orm.db.QueryExecutor;
import com.json4orm.db.QueryResult;
import com.json4orm.engine.QueryContext;
import com.json4orm.engine.ValueConvertor;
import com.json4orm.engine.impl.QueryBuilderImpl;
import com.json4orm.engine.impl.ValueConvertorImpl;
import com.json4orm.exception.Json4ormException;
import com.json4orm.model.query.Query;
import com.json4orm.model.schema.Schema;
import com.json4orm.util.Constants;

/**
 * The Class QueryExecutorImpl implements function to execute queries.
 *
 * @author Xianhua Liu
 */
public class QueryExecutorImpl implements QueryExecutor {
    
    /** The Constant LOG. */
    private static final Logger LOG = LogManager.getLogger(QueryExecutorImpl.class);
    
    /** The db url. */
    private String dbUrl;
    
    /** The db user. */
    private String dbUser;
    
    /** The db password. */
    private String dbPassword;
    
    /** The schema. */
    private Schema schema;
    
    /** The value convertor. */
    private ValueConvertor valueConvertor = new ValueConvertorImpl();

    /**
     * Gets the db url.
     *
     * @return the db url
     */
    public String getDbUrl() {
        return dbUrl;
    }

    /**
     * Sets the db url.
     *
     * @param dbUrl the new db url
     */
    public void setDbUrl(final String dbUrl) {
        this.dbUrl = dbUrl;
    }

    /**
     * Gets the db user.
     *
     * @return the db user
     */
    public String getDbUser() {
        return dbUser;
    }

    /**
     * Sets the db user.
     *
     * @param dbUser the new db user
     */
    public void setDbUser(final String dbUser) {
        this.dbUser = dbUser;
    }

    /**
     * Gets the db password.
     *
     * @return the db password
     */
    public String getDbPassword() {
        return dbPassword;
    }

    /**
     * Sets the db password.
     *
     * @param dbPassword the new db password
     */
    public void setDbPassword(final String dbPassword) {
        this.dbPassword = dbPassword;
    }

    /**
     * Gets the schema.
     *
     * @return the schema
     */
    public Schema getSchema() {
        return schema;
    }

    /**
     * Sets the schema.
     *
     * @param schema the new schema
     */
    public void setSchema(final Schema schema) {
        this.schema = schema;
    }

    /**
     * Gets the value convertor.
     *
     * @return the value convertor
     */
    public ValueConvertor getValueConvertor() {
        return valueConvertor;
    }

    /**
     * Sets the value convertor.
     *
     * @param valueConvertor the new value convertor
     */
    public void setValueConvertor(final ValueConvertor valueConvertor) {
        this.valueConvertor = valueConvertor;
    }

    /**
     * Gets the connection.
     *
     * @return the connection
     * @throws SQLException the SQL exception
     */
    public Connection getConnection() throws SQLException {
        final Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        return conn;
    }

    /**
     * Execute.
     *
     * @param query the query
     * @return the query result
     * @throws Json4ormException the json 4 orm exception
     */
    @Override
    public QueryResult execute(final Query query) throws Json4ormException {
        final QueryResult result = new QueryResult();
        final QueryBuilderImpl queryBuilder = new QueryBuilderImpl(schema);
        queryBuilder.setConvertor(valueConvertor);
        final QueryContext queryContext = queryBuilder.build(query);
        Connection conn = null;
        PreparedStatement ps = null;
        final RecordBuilderImpl recordBuilder = new RecordBuilderImpl();
        try {
            conn = getConnection();
            // get total count
            LOG.debug("Executing count query: " + queryContext.getCountSql());
            ps = conn.prepareStatement(queryContext.getCountSql());
            int index = 1;
            for (final Object value : queryContext.getValues()) {
                ps.setObject(index++, value);
            }
            ResultSet rs = ps.executeQuery();
            long total = 0;
            if (rs.next()) {
                total = rs.getLong(1);
            }
            result.setTotal(total);
            LOG.debug("Finished count query: " + total);
            ps.close();

            // get limit results
            LOG.debug("Executing limit query: " + queryContext.getLimitSql());
            final List<Long> ids = new ArrayList<>();
            ps = conn.prepareStatement(queryContext.getLimitSql());
            index = 1;
            for (final Object value : queryContext.getValues()) {
                ps.setObject(index++, value);
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                ids.add(rs.getLong(1));
            }
            LOG.debug("Finished limit query: " + ids.toString());
            ps.close();

            if (!ids.isEmpty()) {
                String sql = queryContext.getSql();
                sql = sql.replace(Constants.LIMIT_IDS, StringUtils.join(ids, ","));
                LOG.debug("Executing query: " + sql);
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                result.setRecords(recordBuilder.buildRecord(rs, queryContext));
            }
            LOG.debug("Finished query.");
            return result;
        } catch (final Exception e) {
            throw new Json4ormException(e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (final SQLException e) {
                throw new Json4ormException(e);
            }

        }

    }

}
