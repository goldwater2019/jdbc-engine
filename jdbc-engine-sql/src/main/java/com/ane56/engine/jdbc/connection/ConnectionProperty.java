package com.ane56.engine.jdbc.connection;

import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

import static java.lang.String.format;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/24 10:46 AM
 * @Desc:
 * @Version: v1.0
 */

public interface ConnectionProperty<T>
{
    String getKey();

    Optional<String> getDefault();

    DriverPropertyInfo getDriverPropertyInfo(Properties properties);

    boolean isRequired(Properties properties);

    boolean isAllowed(Properties properties);

    Optional<T> getValue(Properties properties)
            throws SQLException;

    default T getRequiredValue(Properties properties)
            throws SQLException
    {
        return getValue(properties).orElseThrow(() ->
                new SQLException(format("Connection property '%s' is required", getKey())));
    }

    void validate(Properties properties)
            throws SQLException;
}
