package com.ane56.engine.jdbc;

import com.ane56.engine.jdbc.connection.UltraConnection;
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

    static final String DRIVER_NAME = "com.ane56.engine.jdbc.UltraDriver";
    static final String DRIVER_VERSION;
    static final int DRIVER_VERSION_MAJOR;
    static final int DRIVER_VERSION_MINOR;
    private static final String DRIVER_URL_START = "jdbc:ultra:";

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            // .addInterceptor(userAgent(DRIVER_NAME + "/" + DRIVER_VERSION))
            .socketFactory(new SocketChannelSocketFactory())
            .build();

    static {
        try {
            DRIVER_VERSION_MAJOR = 3;
            DRIVER_VERSION_MINOR = 1;
            DRIVER_VERSION = "1.0.0";
            UltraDriver driver = new UltraDriver();
            DriverManager.registerDriver(driver);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭连接池
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        httpClient.dispatcher().executorService().shutdown();
        httpClient.connectionPool().evictAll();
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        // first check url is validate
        if (acceptsURL(url)) {
            return new UltraConnection(url, info);
        }

        UltraDriverUri uri = new UltraDriverUri(url, info);

        OkHttpClient.Builder builder = httpClient.newBuilder();
        uri.setupClient(builder);
        QueryExecutor executor = new QueryExecutor(builder.build());

        return new PrestoConnection(uri, executor);
        return null;
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return url.startsWith(DRIVER_URL_START);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
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
