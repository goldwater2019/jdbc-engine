package com.ane56.engine.jdbc;

import com.ane56.engine.jdbc.connection.ConnectionProperty;
import com.ane56.engine.jdbc.connection.UltraConnectionProperties;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.net.HostAndPort;
import okhttp3.OkHttpClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.*;

import static com.ane56.engine.jdbc.connection.UltraConnectionProperties.*;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Objects.requireNonNull;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/24 12:48 AM
 * @Desc:
 * @Version: v1.0
 */

public class UltraDriverUri {
    private static final String JDBC_URL_START = "jdbc:";  // TODO 是否跟上ultra

    private static final Splitter QUERY_SPLITTER = Splitter.on('&').omitEmptyStrings();
    private static final Splitter ARG_SPLITTER = Splitter.on('=').limit(2);

    private final HostAndPort address;
    private final URI uri;

    private final Properties properties;

    private String catalog;
     private String schema;

    public UltraDriverUri(String url, Properties driverProperties)
            throws SQLException {
        this(parseDriverUrl(url), driverProperties);
    }

    private UltraDriverUri(URI uri, Properties driverProperties)
            throws SQLException {
        this.uri = requireNonNull(uri, "uri is null");
        address = HostAndPort.fromParts(uri.getHost(), uri.getPort());
        properties = mergeConnectionProperties(uri, driverProperties);

        validateConnectionProperties(properties);

        initCatalogAndSchema();  // 初始化catalog和schema信息
    }

    public URI getJdbcUri() {
        return uri;
    }

    public String getSchema() {
        return schema;
    }

    public String getCatalog() {
        return catalog;
    }

    public URI getHttpUri() {
        return buildHttpUri();
    }

    public String getUser()
            throws SQLException {
        return USER.getRequiredValue(properties);
    }

    public Optional<String> getApplicationNamePrefix()
            throws SQLException {
        return APPLICATION_NAME_PREFIX.getValue(properties);
    }

    public Properties getProperties() {
        return properties;
    }


    public Map<String, String> getSessionProperties()
            throws SQLException {
        return SESSION_PROPERTIES.getValue(properties).orElse(ImmutableMap.of());
    }

    public void setupClient(OkHttpClient.Builder builder)
            throws SQLException {
        // setupSocksProxy(builder, SOCKS_PROXY.getValue(properties));
        // setupHttpProxy(builder, HTTP_PROXY.getValue(properties));

        // add user specified protocols to okhttp3 client if specified
        // getProtocols().ifPresent(builder::protocols);

        // TODO: fix Tempto to allow empty passwords
        String password = PASSWORD.getValue(properties).orElse("");
        if (!password.isEmpty() && !password.equals("***empty***")) {
            throw new SQLException("Authentication using username/password requires SSL to be enabled");
        }

        // TODO 鉴权
    }

    private static Map<String, String> parseParameters(String query)
            throws SQLException {
        Map<String, String> result = new HashMap<>();

        if (query != null) {
            Iterable<String> queryArgs = QUERY_SPLITTER.split(query);
            for (String queryArg : queryArgs) {
                List<String> parts = ARG_SPLITTER.splitToList(queryArg);
                if (result.put(parts.get(0), parts.get(1)) != null) {
                    throw new SQLException(String.format("Connection property '%s' is in URL multiple times", parts.get(0)));
                }
            }
        }

        return result;
    }

    /**
     * 将URL字符串解析成URI对象
     * 同时检查端口等合理性
     * @param url
     * @return
     * @throws SQLException
     */
    private static URI parseDriverUrl(String url)
            throws SQLException {
        URI uri;
        try {
            uri = new URI(url.substring(JDBC_URL_START.length()));
        } catch (URISyntaxException e) {
            throw new SQLException("Invalid JDBC URL: " + url, e);
        }
        if (isNullOrEmpty(uri.getHost())) {
            throw new SQLException("No host specified: " + url);
        }
        if (uri.getPort() == -1) {
            throw new SQLException("No port number specified: " + url);
        }
        if ((uri.getPort() < 1) || (uri.getPort() > 65535)) {
            throw new SQLException("Invalid port number: " + url);
        }
        return uri;
    }

    private URI buildHttpUri() {
        String scheme = "http";
        try {
            return new URI(scheme, null, address.getHost(), address.getPort(), null, null, null);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void initCatalogAndSchema()
            throws SQLException {
        String path = uri.getPath();
        if (isNullOrEmpty(uri.getPath()) || path.equals("/")) {
            return;
        }

        // remove first slash
        if (!path.startsWith("/")) {
            throw new SQLException("Path does not start with a slash: " + uri);
        }
        path = path.substring(1);

        List<String> parts = Splitter.on("/").splitToList(path);
        // remove last item due to a trailing slash
        if (parts.get(parts.size() - 1).isEmpty()) {
            parts = parts.subList(0, parts.size() - 1);
        }

        if (parts.size() > 2) {
            throw new SQLException("Invalid path segments in URL: " + uri);
        }

        if (parts.get(0).isEmpty()) {
            throw new SQLException("Catalog name is empty: " + uri);
        }
        catalog = parts.get(0);

        if (parts.size() > 1) {
            if (parts.get(1).isEmpty()) {
                throw new SQLException("Schema name is empty: " + uri);
            }
            schema = parts.get(1);
        }
    }

    /**
     * 将uri中的参数, driverProperties和默认的property进行合并, 合并成一个Properties
     * @param uri
     * @param driverProperties
     * @return
     * @throws SQLException
     */
    private static Properties mergeConnectionProperties(URI uri, Properties driverProperties)
            throws SQLException {
        Map<String, String> defaults = UltraConnectionProperties.getDefaults();
        Map<String, String> urlProperties = parseParameters(uri.getQuery());
        Map<String, String> suppliedProperties = Maps.fromProperties(driverProperties);

        for (String key : urlProperties.keySet()) {
            if (suppliedProperties.containsKey(key)) {
                throw new SQLException(String.format("Connection property '%s' is both in the URL and an argument", key));
            }
        }

        Properties result = new Properties();
        setProperties(result, defaults);
        setProperties(result, urlProperties);
        setProperties(result, suppliedProperties);
        return result;
    }


    /**
     * 将Map中的数据设置为properties中的配置项
     *
     * @param properties
     * @param values
     */
    private static void setProperties(Properties properties, Map<String, String> values) {
        for (Map.Entry<String, String> entry : values.entrySet()) {
            properties.setProperty(entry.getKey(), entry.getValue());
        }
    }


    /**
     * 检查属性是否是支持的属性, 如果是不支持的属性, 则报错
     * 同时验证准确性
     *
     * @param connectionProperties
     * @throws SQLException
     */
    private static void validateConnectionProperties(Properties connectionProperties)
            throws SQLException {
        for (String propertyName : connectionProperties.stringPropertyNames()) {
            if (UltraConnectionProperties.forKey(propertyName) == null) {
                throw new SQLException(String.format("Unrecognized connection property '%s'", propertyName));
            }
        }

        for (ConnectionProperty<?> property : UltraConnectionProperties.allProperties()) {
            property.validate(connectionProperties);
        }
    }

}
