package com.ane56.xsql.common.model;

import com.ane56.xsql.common.enumeration.UltraColumnType;
import lombok.*;

import java.io.Serializable;

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
    private UltraColumnType ultraColumnType;
}