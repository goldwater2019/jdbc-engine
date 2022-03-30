package com.ane56.engine.jdbc.model;

import com.ane56.engine.jdbc.thrit.struct.TJDBCCatalog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JDBCCatalog {
    private String name;
    private String driverClass;
    private String uri;
    private String username;
    private String password;

    public static JDBCCatalog parseFromTJDBCCatalog(TJDBCCatalog tJdbcCatalog) {
        return JDBCCatalog.builder()
                .name(tJdbcCatalog.getName())
                .driverClass(tJdbcCatalog.getDriver())
                .uri(tJdbcCatalog.getUrl())
                .username(tJdbcCatalog.getUsername())
                .password(tJdbcCatalog.getPassword())
                .build();
    }

    public TJDBCCatalog asTJDBCCatalog() {
        TJDBCCatalog tjdbcCatalog = new TJDBCCatalog();
        tjdbcCatalog.setDriver(driverClass);
        tjdbcCatalog.setName(name);
        tjdbcCatalog.setUrl(uri);
        tjdbcCatalog.setUsername(username);
        tjdbcCatalog.setPassword(password);
        return tjdbcCatalog;
    }
}
