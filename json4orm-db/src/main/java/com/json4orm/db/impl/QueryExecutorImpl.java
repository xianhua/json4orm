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

public class QueryExecutorImpl implements QueryExecutor {
    private static final Logger LOG = LogManager.getLogger(QueryExecutorImpl.class);
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private Schema schema;
    private ValueConvertor valueConvertor = new ValueConvertorImpl();

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(final String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(final String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(final String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(final Schema schema) {
        this.schema = schema;
    }

    public ValueConvertor getValueConvertor() {
        return valueConvertor;
    }

    public void setValueConvertor(final ValueConvertor valueConvertor) {
        this.valueConvertor = valueConvertor;
    }

    public Connection getConnection() throws SQLException {
        final Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        return conn;
    }

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
