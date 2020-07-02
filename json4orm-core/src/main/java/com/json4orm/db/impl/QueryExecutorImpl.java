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
     * Execute the query.
     *
     * @param query the query
     * @return the query result
     * @throws Json4ormException the json 4 orm exception
     */
    @Override
    public QueryResult execute(final Query query) throws Json4ormException {
        final QueryBuilderImpl queryBuilder = new QueryBuilderImpl(schema, dbUrl);
        queryBuilder.setConvertor(valueConvertor);
        final QueryContext queryContext = queryBuilder.build(query);
        
        switch(query.getAction()) {
        case SEARCH:
            return executeSearch(queryContext);
        case ADD_OR_UPDATE:
            return executeAddOrUpdate(queryContext);
        case DELETE:
            return executeDelete(queryContext);
        default:
            throw new Json4ormException("Invliad action.");
        }
    }

    public QueryResult executeDelete(final QueryContext queryContext) throws Json4ormException {
        final QueryResult result = new QueryResult();

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();
            // execute Delete query
            LOG.debug("Delete entity with id: " + queryContext.getId());
            LOG.debug("Executing insert query: " + queryContext.getDeleteSql());
            ps = conn.prepareStatement(queryContext.getDeleteSql());
            ps.setObject(1, queryContext.getId());
   
            final int res =  ps.executeUpdate();

            ps.close();
            LOG.debug("Finished delete query.");
            result.setTotal(res);
            return result;
        } catch (final SQLException e) {
            if (conn != null) {
                try {
                    LOG.error("Failed to addOrUpdate.", e);
                    conn.rollback();
                } catch (final SQLException excep) {
                    LOG.error("Failed to roll back.", excep);
                    throw new Json4ormException(excep);
                }
            }
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

    public QueryResult executeSearch(final QueryContext queryContext) throws Json4ormException {
        final QueryResult result = new QueryResult();
        
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
                String sql = queryContext.getSearchSql();
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

    public QueryResult executeAddOrUpdate(final QueryContext queryContext) throws Json4ormException {
        final QueryResult result = new QueryResult();

        Connection conn = null;
        PreparedStatement psInsert = null;
        PreparedStatement psUpdate = null;
        int total = 0;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            // execute insert query
            LOG.debug("Insert query for records: " + queryContext.getInsertRecords().size());
            LOG.debug("Executing insert query: " + queryContext.getInsertSql());
            psInsert = conn.prepareStatement(queryContext.getInsertSql());
            for (final List<Object> record : queryContext.getInsertRecords()) {
                for (int i=0;i< record.size(); i++) {
                    psInsert.setObject(i+1, record.get(i));
                }
                psInsert.executeUpdate();
                total++;
            }
            psInsert.close();
            
            // execute update query
            LOG.debug("Update query for records: " + queryContext.getUpdateRecords().size());
            LOG.debug("Executing update query: " + queryContext.getUpdateSql());
            psUpdate = conn.prepareStatement(queryContext.getUpdateSql());
            for (final List<Object> record : queryContext.getUpdateRecords()) {
                for (int i=0;i< record.size(); i++) {
                    psUpdate.setObject(i+1, record.get(i));
                }
                psUpdate.executeUpdate();
                total++;
            }
            psUpdate.close();
            
            conn.commit();
            LOG.debug("Finished query.");
            result.setTotal(total);
            return result;
        } catch (final SQLException e) {
            if (conn != null) {
                try {
                    LOG.error("Failed to addOrUpdate.", e);
                    conn.rollback();
                } catch (final SQLException excep) {
                    LOG.error("Failed to roll back.", excep);
                    throw new Json4ormException(excep);
                }
            }
            throw new Json4ormException(e);
        } finally {
            try {
                if (psInsert != null) {
                    psInsert.close();
                }
                
                if (psUpdate != null) {
                    psUpdate.close();
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
