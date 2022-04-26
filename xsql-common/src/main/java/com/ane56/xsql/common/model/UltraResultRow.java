package com.ane56.xsql.common.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/26 4:41 PM
 * @Desc: 每一行resultSet内对象的数据
 * @Version: v1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class UltraResultRow implements Serializable {
    private static final long serialVersionUID = -6439942002129746938L;
    private UltraResultSetMetaData ultraResultSetMetaData;
    private List<Object> ultraResultSetData;
}
