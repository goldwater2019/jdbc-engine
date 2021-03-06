package com.ane56.engine.jdbc;

import com.ane56.xsql.common.exception.XSQLException;
import com.google.common.primitives.Ints;

import java.io.IOException;
import java.sql.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Math.toIntExact;
import static java.util.Objects.requireNonNull;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/24 11:49 AM
 * @Desc:
 * @Version: v1.0
 */

public class UltraStatement implements Statement {

    private final AtomicLong maxRows = new AtomicLong();
    private final AtomicInteger queryTimeoutSeconds = new AtomicInteger();
    private final AtomicInteger fetchSize = new AtomicInteger();
    private final AtomicBoolean escapeProcessing = new AtomicBoolean(true);
    private final AtomicBoolean closeOnCompletion = new AtomicBoolean();
    private final AtomicReference<UltraConnection> connection;
    private final AtomicReference<StatementClient> executingClient = new AtomicReference<>();
    private final AtomicReference<UltraResultSet> currentResult = new AtomicReference<>();
    private final AtomicLong currentUpdateCount = new AtomicLong(-1);
    private final AtomicReference<String> currentUpdateType = new AtomicReference<>();


    public UltraStatement(UltraConnection connection) {
        this.connection = new AtomicReference<>(requireNonNull(connection, "connection is null"));
    }

    /**
     * 检查连接
     */
    protected final void checkOpen() throws SQLException {
        connection();
    }

    /**
     * 内部, 连接检查
     *
     * @return
     * @throws SQLException
     */
    protected final UltraConnection connection() throws SQLException {
        UltraConnection connection = this.connection.get();
        if (connection == null) {
            throw new SQLException("Statement is closed");
        }
        if (connection.isClosed()) {
            throw new SQLException("Connection is closed");
        }
        return connection;
    }


    /**
     * 关闭resultSet
     *
     * @throws SQLException
     */
    private void closeResultSet() throws SQLException {
        UltraResultSet resultSet = currentResult.getAndSet(null);
        if (resultSet != null) {
            resultSet.close();
        }
    }

    /**
     * 执行Query, 并且获得结果
     *
     * @param sql an SQL statement to be sent to the database, typically a
     *            static SQL <code>SELECT</code> statement
     * @return
     * @throws SQLException
     */
    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        if (!execute(sql)) {
            throw new SQLException("SQL statement is not a query: " + sql);
        }
        return currentResult.get();
    }

    /**
     * 关闭连接
     * @throws SQLException
     */
    @Override
    public void close() throws SQLException {
        connection.set(null);
        closeResultSet();
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        checkOpen();
        return 0;
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        checkOpen();
        if (max < 0) {
            throw new SQLException("Max field size must be positive");
        }
    }

    @Override
    public int getMaxRows() throws SQLException {
        long result = getLargeMaxRows();
        if (result > Integer.MAX_VALUE) {
            throw new SQLException("Max rows exceeds limit of 2147483647");
        }
        return toIntExact(result);
    }

    @Override
    public long getLargeMaxRows() throws SQLException {
        checkOpen();
        return maxRows.get();
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        setLargeMaxRows(max);
    }

    @Override
    public void setLargeMaxRows(long max)
            throws SQLException {
        checkOpen();
        if (max < 0) {
            throw new SQLException("Max rows must be positive");
        }
        maxRows.set(max);
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        checkOpen();
        escapeProcessing.set(enable);
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        checkOpen();
        return queryTimeoutSeconds.get();
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        checkOpen();
        if (seconds < 0) {
            throw new SQLException("Query timeout seconds must be positive");
        }
        queryTimeoutSeconds.set(seconds);
    }

    @Override
    public void cancel() throws SQLException {
        checkOpen();
        StatementClient client = executingClient.get();
        if (client != null) {
            // TODO close it
        }
        closeResultSet();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {

    }

    @Override
    public void setCursorName(String name) throws SQLException {
        checkOpen();
        // TODO not support feature
    }

    /**
     * true -> 返回结果
     * false -> 不返回结果
     * @param sql any SQL statement
     * @return
     * @throws SQLException
     */
    @Override
    public boolean execute(String sql) throws SQLException {
        return internalExecute(sql);
    }

    /**
     * statement 真正执行SQL的地方
     * 1. 获得connection
     * 2. 通过connection执行query, 获得statementClient对象
     * 3. 将正在执行的client置为获得的client
     * 4. 获得相应的resetSet
     * 5. 如果是query,将currentResultSet更新
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public final boolean internalExecute(String sql) throws SQLException {
        clearCurrentResults();
        checkOpen();

        StatementClient client = null;
        UltraResultSet resultSet = null;
        boolean intercepted = false;

        try {
            // 开始查询
            client = connection().startQuery(sql);
            boolean isQuery = client.advance();
            executingClient.set(client);
            if (isQuery) {
                client.refreshResultSet(this);
                resultSet = client.getResultSet();
                currentResult.set(resultSet);
                return true;
            }
            // this is an update
            while (resultSet!= null && resultSet.next()) {
                // no-ops
            }

            return isQuery;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (XSQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // TODO 别的catch补全
            executingClient.set(null);
            if (currentResult.get() == null) {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (client != null) {
                    // TODO 关闭client
                }
            }
        }
    }

    private void clearCurrentResults() throws SQLException {
        UltraResultSet rs = currentResult.getAndSet(null);
        if (rs != null) {
            rs.close();
        }
        currentUpdateCount.set(-1);
        currentUpdateType.set(null);
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return currentResult.get();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return Ints.saturatedCast(getLargeUpdateCount());
    }

    @Override
    public long getLargeUpdateCount() throws SQLException {
        checkOpen();
        return currentUpdateCount.get();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return getMoreResults(CLOSE_CURRENT_RESULT);
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        checkOpen();

        currentUpdateCount.set(-1);
        currentUpdateType.set(null);

        if (current == CLOSE_CURRENT_RESULT) {
            closeResultSet();
            return false;
        }
        if (current != KEEP_CURRENT_RESULT && current != CLOSE_ALL_RESULTS) {
            throw new SQLException("Invalid argument: " + current);
        }
        throw new SQLFeatureNotSupportedException("Multiple open results not supported");
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        checkOpen();
        if (!validFetchDirection(direction)) {
            throw new SQLException("Invalid fetch direction");
        }
        // fetch direction is always forward
    }

    @Override
    public int getFetchDirection() throws SQLException {
        checkOpen();
        return ResultSet.FETCH_FORWARD;
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        checkOpen();
        if (rows < 0) {
            throw new SQLException("Fetch size must be positive");
        }
        fetchSize.set(rows);
    }

    @Override
    public int getFetchSize() throws SQLException {
        checkOpen();
        return fetchSize.get();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        checkOpen();
        return ResultSet.CONCUR_READ_ONLY;
    }

    @Override
    public int getResultSetType() throws SQLException {
        checkOpen();
        return ResultSet.TYPE_FORWARD_ONLY;
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        checkOpen();
        throw new SQLFeatureNotSupportedException("Batches not supported");
    }

    @Override
    public void clearBatch() throws SQLException {
        checkOpen();
        throw new SQLFeatureNotSupportedException("Batches not supported");
    }

    @Override
    public int[] executeBatch() throws SQLException {
        checkOpen();
        throw new SQLFeatureNotSupportedException("Batches not supported");
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connection();
    }


    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        throw new SQLFeatureNotSupportedException("getGeneratedKeys");
    }

    @Override
    public int executeUpdate(String sql)
            throws SQLException {
        return Ints.saturatedCast(executeLargeUpdate(sql));
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return executeUpdate(sql);
    }

    @Override
    public long executeLargeUpdate(String sql)
            throws SQLException {
        if (execute(sql)) {
            throw new SQLException("SQL is not an update statement: " + sql);
        }
        return currentUpdateCount.get();
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return executeUpdate(sql);
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return executeUpdate(sql);
    }

    @Override
    public long executeLargeUpdate(String sql, int autoGeneratedKeys)
            throws SQLException {
        return executeLargeUpdate(sql);
    }

    @Override
    public long executeLargeUpdate(String sql, int[] columnIndexes)
            throws SQLException {
        return executeLargeUpdate(sql);
    }

    @Override
    public long executeLargeUpdate(String sql, String[] columnNames)
            throws SQLException {
        return executeLargeUpdate(sql);
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return execute(sql);
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return execute(sql);
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return execute(sql);
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return ResultSet.CLOSE_CURSORS_AT_COMMIT;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return connection.get() == null;
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        checkOpen();
        // TODO impl, not support now
    }

    @Override
    public boolean isPoolable() throws SQLException {
        checkOpen();
        return false;
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        checkOpen();
        closeOnCompletion.set(true);
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        checkOpen();
        return closeOnCompletion.get();
    }

    @Override
    public <T> T unwrap(Class<T> iface)
            throws SQLException {
        if (isWrapperFor(iface)) {
            return (T) this;
        }
        throw new SQLException("No wrapper for " + iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface)
            throws SQLException {
        return iface.isInstance(this);
    }

    private static boolean validFetchDirection(int direction) {
        return (direction == ResultSet.FETCH_FORWARD) ||
                (direction == ResultSet.FETCH_REVERSE) ||
                (direction == ResultSet.FETCH_UNKNOWN);
    }
}
