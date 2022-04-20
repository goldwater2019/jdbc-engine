package com.ane56.engine.jdbc.model;

import com.ane56.engine.jdbc.enumeration.JDBCColumnType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JDBCColumn {
    private JDBCColumnType jdbcColumnType;
    private String jdbcColumnName;
}
