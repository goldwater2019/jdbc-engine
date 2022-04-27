package com.ane56.xsql.common.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/26 4:26 PM
 * @Desc:
 * @Version: v1.0
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UltraResultSetMetaData implements Serializable {
    private static final long serialVersionUID = 2006997583902867045L;
    private int columnCount;
    private List<UltraResultColumnMetaData> columnMetaDataList;

}
