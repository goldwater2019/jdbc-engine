package com.ane56.engine.jdbc.model;

import com.ane56.engine.jdbc.enumeration.JDBCColumnType;
import com.ane56.engine.jdbc.thrit.struct.TJDBCResultColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JDBCResultColumn {
    private String columnName;
    private JDBCColumnType columnType;
    private String columnClassName;
    private String columnValue;

    /**
     * 将JDBCResultColumn转化成TJDBCResultColumn
     * @return
     */
    public TJDBCResultColumn asTJDBCResultColumn() {
        TJDBCResultColumn tjdbcResultColumn = new TJDBCResultColumn();
        tjdbcResultColumn.setColumnName(getColumnName());
        tjdbcResultColumn.setColumnType(columnType.asTJDBCColumnType());
        tjdbcResultColumn.setColumnClassName(getColumnClassName());
        tjdbcResultColumn.setColumnValue(getColumnValue());
        return tjdbcResultColumn;
    }

    /**
     * 从TJDBCResultColumn中解析出JDBCResultColumn对象
     * @param tjdbcResultColumn
     * @return
     */
    public static JDBCResultColumn parseFromTJDBCResultColumn(TJDBCResultColumn tjdbcResultColumn) {
        return JDBCResultColumn.builder()
                .columnName(tjdbcResultColumn.getColumnName())
                .columnType(JDBCColumnType.parseFromTJDBCColumnType(tjdbcResultColumn.getColumnType()))
                .columnClassName(tjdbcResultColumn.getColumnClassName())
                .columnValue(tjdbcResultColumn.getColumnValue())
                .build();
    }
}
