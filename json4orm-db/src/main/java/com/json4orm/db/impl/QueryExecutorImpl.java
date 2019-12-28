package com.json4orm.db.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.json4orm.db.QueryExecutor;
import com.json4orm.engine.QueryContext;
import com.json4orm.engine.ValueConvertor;
import com.json4orm.engine.impl.QueryVisitor;
import com.json4orm.engine.impl.ValueConvertorImpl;
import com.json4orm.exception.Json4ormException;
import com.json4orm.model.entity.Schema;
import com.json4orm.model.query.Query;

public class QueryExecutorImpl implements QueryExecutor {
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
    public List<Map<String, Object>> execute(final Query query) throws Json4ormException {
        final QueryVisitor queryVisitor = new QueryVisitor(schema);
        queryVisitor.setConvertor(valueConvertor);

        query.accept(queryVisitor);

        final QueryContext queryContext = queryVisitor.getQueryContext();
        Connection conn = null;
        PreparedStatement ps = null;
        final RecordBuilderImpl recordBuilder = new RecordBuilderImpl();
        try {
            conn = getConnection();
            ps = conn.prepareStatement(queryContext.getSql());
            int index = 1;
            for (final Object value : queryContext.getValues()) {
                ps.setObject(index++, value);
            }

            final ResultSet rs = ps.executeQuery();
            return recordBuilder.buildRecord(rs, queryContext);
        } catch (final Exception e) {
            throw new Json4ormException("Failed to execute query: " + queryContext.getSql(), e);
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
