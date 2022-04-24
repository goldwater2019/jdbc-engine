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
    private Integer columnCount;
    private Boolean autoIncrement;
    private Boolean caseSensitive;
    private Boolean searchable;
    private Boolean currency;
    private Integer nullable;
    private Boolean signed;
    private Integer columnDisplaySize;
    private String columnLabel;
    private String columnName;
    private String schemaName;
    private Integer precision;
    private Integer scale;
    private String tableName;
    private String catalogName;
    private Integer columnType;
    private Boolean readOnly;
    private Boolean writable;
    private Boolean definitelyWritable;
    private String columnClassName;
    private String columnTypeName;
    private JDBCColumnType jdbcColumnType;
    private String columnValue;

    /**
     * 从TJDBCResultColumn中解析出JDBCResultColumn对象
     * <p>
     * TODO 从thrift端中获得新的内容
     *
     * @param tjdbcResultColumn
     * @return
     */
    public static JDBCResultColumn parseFromTJDBCResultColumn(TJDBCResultColumn tjdbcResultColumn) {
        return JDBCResultColumn.builder()
                .columnName(tjdbcResultColumn.getColumnName())
                .jdbcColumnType(JDBCColumnType.parseFromTJDBCColumnType(tjdbcResultColumn.getColumnType()))
                .columnClassName(tjdbcResultColumn.getColumnClassName())
                .columnValue(tjdbcResultColumn.getColumnValue())
                .build();
    }

    /**
     * 将JDBCResultColumn转化成TJDBCResultColumn
     *
     * @return
     */
    public TJDBCResultColumn asTJDBCResultColumn() {
        TJDBCResultColumn tjdbcResultColumn = new TJDBCResultColumn();
        tjdbcResultColumn.setColumnName(getColumnName());
        tjdbcResultColumn.setColumnType(jdbcColumnType.asTJDBCColumnType());
        tjdbcResultColumn.setColumnClassName(getColumnClassName());
        tjdbcResultColumn.setColumnValue(getColumnValue());
        return tjdbcResultColumn;
    }
}
