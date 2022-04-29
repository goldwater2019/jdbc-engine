package com.ane56.xsql.common.model;

import com.ane56.xsql.common.enumeration.UltraColumnType;
import lombok.*;

import java.io.Serializable;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/26 4:17 PM
 * @Desc:
 * @Version: v1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class UltraResultColumnMetaData implements Serializable {
    private static final long serialVersionUID = 1305486492153518536L;
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
    private UltraColumnType ultraColumnType;

    public static UltraResultColumnMetaData parseFromMetaData(ResultSetMetaData metaData, int columnIndex) throws SQLException {
        return UltraResultColumnMetaData.builder()
                .autoIncrement(metaData.isAutoIncrement(columnIndex))
                .caseSensitive(metaData.isCaseSensitive(columnIndex))
                .searchable(metaData.isSearchable(columnIndex))
                .currency(metaData.isCurrency(columnIndex))
                .nullable(metaData.isNullable(columnIndex))
                .signed(metaData.isSigned(columnIndex))
                .columnDisplaySize(metaData.getColumnDisplaySize(columnIndex))
                .columnLabel(metaData.getColumnLabel(columnIndex))
                .columnName(metaData.getColumnName(columnIndex))
                .schemaName(metaData.getSchemaName(columnIndex))
                .precision(metaData.getPrecision(columnIndex))
                .scale(metaData.getScale(columnIndex))
                .tableName(metaData.getTableName(columnIndex))
                .catalogName(metaData.getCatalogName(columnIndex))
                .columnType(metaData.getColumnType(columnIndex))
                .readOnly(metaData.isReadOnly(columnIndex))
                .writable(metaData.isWritable(columnIndex))
                .definitelyWritable(metaData.isDefinitelyWritable(columnIndex))
                .columnClassName(metaData.getColumnClassName(columnIndex))
                .columnTypeName(metaData.getColumnTypeName(columnIndex))
                .ultraColumnType(UltraColumnType.findByValue(metaData.getColumnType(columnIndex)))
                .build();
    }
}