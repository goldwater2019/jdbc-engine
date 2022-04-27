package com.ane56.engine.jdbc.connection;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.Map;
import java.util.Set;

import static com.ane56.engine.jdbc.connection.AbstractUltraConnectionProperty.StringMapConverter.STRING_MAP_CONVERTER;
import static java.util.Collections.unmodifiableMap;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/24 10:46 AM
 * @Desc:
 * @Version: v1.0
 */

public class UltraConnectionProperties {
    public static final ConnectionProperty<String> USER = new User();
    public static final ConnectionProperty<String> PASSWORD = new Password();
    public static final ConnectionProperty<String> APPLICATION_NAME = new ApplicationName();

    public static final ConnectionProperty<Integer> TIMEOUT = new Timeout();

    private static final Set<ConnectionProperty<?>> ALL_PROPERTIES = ImmutableSet.<ConnectionProperty<?>>builder()
            .add(USER)
            .add(PASSWORD)
            .add(APPLICATION_NAME)
            .add(TIMEOUT)
            .build();

    private static final Map<String, ConnectionProperty<?>> KEY_LOOKUP = unmodifiableMap(ALL_PROPERTIES.stream()
            .collect(toMap(ConnectionProperty::getKey, identity())));
    private static final Map<String, String> DEFAULTS;

    static {
        ImmutableMap.Builder<String, String> defaults = ImmutableMap.builder();
        for (ConnectionProperty<?> property : ALL_PROPERTIES) {
            property.getDefault().ifPresent(value -> defaults.put(property.getKey(), value));
        }
        DEFAULTS = defaults.build();
    }

    private UltraConnectionProperties() {
    }

    public static ConnectionProperty<?> forKey(String propertiesKey) {
        return KEY_LOOKUP.get(propertiesKey);
    }

    public static Set<ConnectionProperty<?>> allProperties() {
        return ALL_PROPERTIES;
    }

    public static Map<String, String> getDefaults() {
        return DEFAULTS;
    }

    private static class User
            extends AbstractUltraConnectionProperty<String> {
        public User() {
            super("user", REQUIRED, ALLOWED, NON_EMPTY_STRING_CONVERTER);
        }
    }

    private static class Password
            extends AbstractUltraConnectionProperty<String> {
        public Password() {
            super("password", NOT_REQUIRED, ALLOWED, STRING_CONVERTER);
        }
    }

    private static class ApplicationName
            extends AbstractUltraConnectionProperty<String> {
        public ApplicationName() {
            super("applicationName", NOT_REQUIRED, ALLOWED, STRING_CONVERTER);
        }
    }

    private static class Timeout
            extends AbstractUltraConnectionProperty<Integer> {
        public Timeout() {
            super("timeout", NOT_REQUIRED, ALLOWED, INTEGER_CONVERTER);
        }
    }

    private static class SessionProperties
            extends AbstractUltraConnectionProperty<Map<String, String>> {
        public SessionProperties() {
            super("sessionProperties", NOT_REQUIRED, ALLOWED, STRING_MAP_CONVERTER);
        }
    }

}
