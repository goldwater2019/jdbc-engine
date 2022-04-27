package com.ane56.engine.jdbc;

import com.ane56.engine.jdbc.connection.UltraConnection;
import com.ane56.engine.jdbc.connection.UltraConnectionProperties;
import com.ane56.engine.jdbc.socket.SocketChannelSocketFactory;
import okhttp3.OkHttpClient;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/24 12:01 AM
 * @Desc:
 * @Version: v1.0
 */

public class UltraDriver implements Driver, Closeable {

    // 驱动名称
    static final String DRIVER_NAME = "com.ane56.engine.jdbc.UltraDriver";
    static final String DRIVER_VERSION;
    static final int DRIVER_VERSION_MAJOR;
    static final int DRIVER_VERSION_MINOR;
    private static final String DRIVER_URL_START = "jdbc:ultra:";

    // 只要一建立连接, 就创建一个客户端
    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .socketFactory(new SocketChannelSocketFactory())
            .build();

    static {
        try {
            DRIVER_VERSION_MAJOR = 0;
            DRIVER_VERSION_MINOR = 1;
            DRIVER_VERSION = "1.0.1";
            UltraDriver driver = new UltraDriver();
            DriverManager.registerDriver(driver);  // 注册驱动
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭连接池
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        httpClient.dispatcher().executorService().shutdown();
        httpClient.connectionPool().evictAll();
    }

    /**
     * 建立连接, 用于连接网关
     *
     * @param url  the URL of the database to which to connect
     * @param info a list of arbitrary string tag/value pairs as
     *             connection arguments. Normally at least a "user" and
     *             "password" property should be included.
     * @return
     * @throws SQLException
     */
    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        // first check url is validate
        if (!acceptsURL(url)) {
            return null;
        }

        UltraDriverUri uri = new UltraDriverUri(url, info);

        OkHttpClient.Builder builder = httpClient.newBuilder();
        // 配置相应的客户端创建请求
        uri.setupClient(builder);
        QueryExecutor executor = new QueryExecutor(builder.build());

        return new UltraConnection(uri, executor);
    }


    /**
     * 确保连接的url串没问题
     *
     * @param url the URL of the database
     * @return
     * @throws SQLException
     */
    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return url.startsWith(DRIVER_URL_START);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        UltraDriverUri ultraDriverUri = new UltraDriverUri(url, info);
        return UltraConnectionProperties.allProperties().stream()
                .map(property -> property.getDriverPropertyInfo(ultraDriverUri.getProperties()))
                .toArray(DriverPropertyInfo[]::new);
    }

    @Override
    public int getMajorVersion() {
        return DRIVER_VERSION_MAJOR;
    }

    @Override
    public int getMinorVersion() {
        return DRIVER_VERSION_MINOR;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
