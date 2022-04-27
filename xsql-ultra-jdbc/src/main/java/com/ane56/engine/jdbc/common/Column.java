package com.ane56.engine.jdbc.common;

import com.ane56.engine.jdbc.enumeration.JDBCColumnType;
import lombok.*;

/**
 * Column为通过数据库获得的对象
 *
 * @Author: zhangxinsen
 * @Date: 2022/4/24 5:59 PM
 * @Desc:
 * @Version: v1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class Column {
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
}
