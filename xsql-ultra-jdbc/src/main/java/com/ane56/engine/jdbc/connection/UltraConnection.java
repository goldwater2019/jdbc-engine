package com.ane56.engine.jdbc.connection;

import com.ane56.engine.jdbc.GatewayInfo;
import com.ane56.engine.jdbc.NotImplementedException;
import com.ane56.engine.jdbc.QueryExecutor;
import com.ane56.engine.jdbc.UltraDriverUri;
import com.ane56.engine.jdbc.common.client.ClientSession;
import com.ane56.engine.jdbc.common.client.StatementClient;
import com.ane56.engine.jdbc.metadata.UltraDatabaseMetaData;
import com.ane56.engine.jdbc.preparedstatement.UltraPreparedStatement;
import com.ane56.engine.jdbc.UltraStatement;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Ints;

import java.net.URI;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.common.collect.Maps.fromProperties;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/24 12:06 AM
 * @Desc: 网关连接对象
 * @Version: v1.0
 */

public class UltraConnection implements Connection {

    private final AtomicBoolean closed = new AtomicBoolean();
    private final AtomicBoolean autoCommit = new AtomicBoolean(true);
    private final AtomicInteger isolationLevel = new AtomicInteger(TRANSACTION_READ_UNCOMMITTED);
    private final AtomicBoolean readOnly = new AtomicBoolean();
    private final AtomicReference<String> catalog = new AtomicReference<>();
    private final AtomicReference<String> schema = new AtomicReference<>();
    private final AtomicReference<Locale> locale = new AtomicReference<>();
    private final AtomicReference<Integer> networkTimeoutMillis = new AtomicReference<>(Ints.saturatedCast(MINUTES.toMillis(2)));
    private final AtomicReference<GatewayInfo> gatewayInfo = new AtomicReference<>();
    private final AtomicLong nextStatementId = new AtomicLong(1);

    private final URI jdbcUri;
    private final URI httpUri;
    private final String user;
    private final Optional<String> applicationName;
    private final Map<String, String> clientInfo = new ConcurrentHashMap<>();
    private final Map<String, String> preparedStatements = new ConcurrentHashMap<>();
    private final AtomicReference<String> transactionId = new AtomicReference<>();
    private final QueryExecutor queryExecutor;


    public UltraConnection(UltraDriverUri uri, QueryExecutor queryExecutor) throws SQLException {
        requireNonNull(uri, "uri is null");
        jdbcUri = uri.getJdbcUri();
        httpUri = uri.getHttpUri();
        this.catalog.set(uri.getCatalog());  // TODO 测试catalog为空时的数据
        this.schema.set(uri.getSchema());
        this.user = uri.getUser();
        this.applicationName = uri.getApplicationName();
        this.queryExecutor = requireNonNull(queryExecutor, "queryExecutor is null");
    }

    /**
     * TODO 解析Statement对象
     * @return
     * @throws SQLException
     */
    @Override
    public Statement createStatement() throws SQLException {
        checkOpen();
        return new UltraStatement(this);
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        checkOpen();
        String name = "statement" + nextStatementId.getAndIncrement();
        return new UltraPreparedStatement(this, name, sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        throw new NotImplementedException("Connection", "prepareCall");
    }

    /**
     * 输出原始的sql
     * @param sql an SQL statement that may contain one or more '?'
     * parameter placeholders
     * @return
     * @throws SQLException
     */
    @Override
    public String nativeSQL(String sql) throws SQLException {
        return sql;
    }


    /**
     * 修改auto commit属性
     * 如果原本auto commit 为 false, 现在为true, 则需要手动提交一次
     *
     * @param autoCommit <code>true</code> to enable auto-commit mode;
     *                   <code>false</code> to disable it
     * @throws SQLException
     */
    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        checkOpen();
        throw new NotImplementedException("Connection", "setAutoCommit");
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        checkOpen();
        return autoCommit.get();
    }

    @Override
    public void commit() throws SQLException {
        checkOpen();
        throw new NotImplementedException("Connection", "commit");
    }

    @Override
    public void rollback() throws SQLException {
        checkOpen();
        throw new NotImplementedException("Connection", "rollback");
    }

    @Override
    public void close() throws SQLException {
        try {
            if (!closed.get() && (transactionId.get() != null)) {  // 连接未关闭并且存在事务ID时
                try (UltraStatement statement = new UltraStatement(this)) {
                     statement.internalExecute("ROLLBACK");
                }
            }
        } finally {
            closed.set(true);
            Throwable innerException = null;
        }
    }

    @Override
    public boolean isClosed() throws SQLException {
        return closed.get();
    }

    /**
     * 返回元数据信息
     * TODO 解析数据库元数据
     * 通过自己实现的MyDatabaseMetaData 完成部分
     * @return
     * @throws SQLException
     */
    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return new UltraDatabaseMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        checkOpen();
        this.readOnly.set(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return readOnly.get();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        checkOpen();
        this.catalog.set(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        checkOpen();
        return this.catalog.get();
    }

    /**
     * 事务隔离级别设置
     *
     * @param level one of the following <code>Connection</code> constants:
     *              <code>Connection.TRANSACTION_READ_UNCOMMITTED</code>,
     *              <code>Connection.TRANSACTION_READ_COMMITTED</code>,
     *              <code>Connection.TRANSACTION_REPEATABLE_READ</code>, or
     *              <code>Connection.TRANSACTION_SERIALIZABLE</code>.
     *              (Note that <code>Connection.TRANSACTION_NONE</code> cannot be used
     *              because it specifies that transactions are not supported.)
     * @throws SQLException
     */
    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        checkOpen();
        throw new NotImplementedException("Connection", "setTransactionIsolation");
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        throw new NotImplementedException("Connection", "getTransactionIsolation");
//        return isolationLevel.get();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        checkOpen();
        return null;
    }

    @Override
    public void clearWarnings() throws SQLException {
        checkOpen();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        checkResultSet(resultSetType, resultSetConcurrency);
        return createStatement();
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        checkResultSet(resultSetType, resultSetConcurrency);
        return prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        checkResultSet(resultSetType, resultSetConcurrency);
        throw new SQLFeatureNotSupportedException("prepareCall");
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        throw new SQLFeatureNotSupportedException("getTypeMap");
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        throw new SQLFeatureNotSupportedException("setTypeMap");
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        checkOpen();
        if (holdability != ResultSet.HOLD_CURSORS_OVER_COMMIT) {
            throw new SQLFeatureNotSupportedException("Changing holdability not supported");
        }
    }

    @Override
    public int getHoldability() throws SQLException {
        checkOpen();
        return ResultSet.HOLD_CURSORS_OVER_COMMIT;
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        throw new SQLFeatureNotSupportedException("setSavepoint");
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        throw new SQLFeatureNotSupportedException("setSavepoint");
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        throw new SQLFeatureNotSupportedException("rollback");
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        throw new SQLFeatureNotSupportedException("releaseSavepoint");
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        checkHoldability(resultSetHoldability);
        return createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        checkHoldability(resultSetHoldability);
        return prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        checkHoldability(resultSetHoldability);
        return prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        if (autoGeneratedKeys != Statement.RETURN_GENERATED_KEYS) {
            throw new SQLFeatureNotSupportedException("Auto generated keys must be NO_GENERATED_KEYS");
        }
        return prepareStatement(sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        throw new SQLFeatureNotSupportedException("prepareStatement");
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        throw new SQLFeatureNotSupportedException("prepareStatement");
    }

    @Override
    public Clob createClob() throws SQLException {
        throw new SQLFeatureNotSupportedException("createClob");
    }

    @Override
    public Blob createBlob() throws SQLException {
        throw new SQLFeatureNotSupportedException("createBlob");
    }

    @Override
    public NClob createNClob() throws SQLException {
        throw new SQLFeatureNotSupportedException("createNClob");
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        throw new SQLFeatureNotSupportedException("createSQLXML");
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        if (timeout < 0) {
            throw new SQLException("Timeout is negative");
        }
        return !isClosed();
    }


    /**
     * 如果传入的value为null, 则置空
     * @param name          The name of the client info property to set
     * @param value         The value to set the client info property to.  If the
     *                                      value is null, the current value of the specified
     *                                      property is cleared.
     * <p>
     * @throws SQLClientInfoException
     */
    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        requireNonNull(name, "name is null");
        if (value != null) {
            clientInfo.put(name, value);
        } else {
            clientInfo.remove(name);
        }
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        clientInfo.putAll(fromProperties(properties));
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return clientInfo.get(name);

    }

    @Override
    public Properties getClientInfo() throws SQLException {
        Properties properties = new Properties();
        for (Map.Entry<String, String> entry : clientInfo.entrySet()) {
            properties.setProperty(entry.getKey(), entry.getValue());
        }
        return properties;
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        throw new SQLFeatureNotSupportedException("createArrayOf");
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        throw new SQLFeatureNotSupportedException("createStruct");
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        checkOpen();
        this.schema.set(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        return schema.get();
    }

    /**
     * 取消执行
     * @param executor  The <code>Executor</code>  implementation which will
     * be used by <code>abort</code>.
     * @throws SQLException
     */
    @Override
    public void abort(Executor executor) throws SQLException {
        close();
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        checkOpen();
        if (milliseconds < 0) {
            throw new SQLException("Timeout is negative");
        }
        networkTimeoutMillis.set(milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        checkOpen();
        return networkTimeoutMillis.get();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (isWrapperFor(iface)) {
            return (T) this;
        }
        throw new SQLException("No wrapper for " + iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this);
    }

    private void checkOpen()
            throws SQLException {
        if (isClosed()) {
            throw new SQLException("Connection is closed");
        }
    }

    URI getURI() {
        return jdbcUri;
    }

    String getUser() {
        return user;
    }

    private static void checkResultSet(int resultSetType, int resultSetConcurrency)
            throws SQLFeatureNotSupportedException {
        if (resultSetType != ResultSet.TYPE_FORWARD_ONLY) {
            throw new SQLFeatureNotSupportedException("Result set type must be TYPE_FORWARD_ONLY");
        }
        if (resultSetConcurrency != ResultSet.CONCUR_READ_ONLY) {
            throw new SQLFeatureNotSupportedException("Result set concurrency must be CONCUR_READ_ONLY");
        }
    }

    private static void checkHoldability(int resultSetHoldability)
            throws SQLFeatureNotSupportedException {
        if (resultSetHoldability != ResultSet.HOLD_CURSORS_OVER_COMMIT) {
            throw new SQLFeatureNotSupportedException("Result set holdability must be HOLD_CURSORS_OVER_COMMIT");
        }
    }

    /**
     * 启动query查询
     *
     * @param sql
     * @param sessionPropertiesOverride
     * @return
     */
    public StatementClient startQuery(String sql, Map<String, String> sessionPropertiesOverride) {
        String source = "jdbc-ultra";
        if (applicationName.isPresent()) {
            source = applicationName.get();
        }
        ClientSession session = ClientSession.builder()
                .server(httpUri)
                .user(user)
                .source(source)
                .clientInfo(clientInfo.get("ClientInfo"))
                .catalog(catalog.get())
                .schema(schema.get())
                .resourceEstimates(ImmutableMap.of())
                .properties(ImmutableMap.copyOf(allProperties))
                .preparedStatements(ImmutableMap.copyOf(preparedStatements))
                .transactionId(transactionId.get())
                .clientRequestTimeout(millis)
                .sessionFunctions(ImmutableMap.of())
                .build();

        return queryExecutor.startQuery(session, sql);
    }

    /**
     * 根据statementClient更新connection的session信息
     *
     * @param client
     */
    public void updateSession(StatementClient client) {
        sessionProperties.putAll(client.getSetSessionProperties());
        client.getResetSessionProperties().forEach(sessionProperties::remove);

        preparedStatements.putAll(client.getAddedPreparedStatements());
        client.getDeallocatedPreparedStatements().forEach(preparedStatements::remove);

        client.getSetCatalog().ifPresent(catalog::set);
        client.getSetSchema().ifPresent(schema::set);

        if (client.getStartedTransactionId() != null) {
            transactionId.set(client.getStartedTransactionId());
        }
        if (client.isClearTransactionId()) {
            transactionId.set(null);
        }
    }
}
