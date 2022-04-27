package com.ane56.xsql.common.model;

import lombok.*;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/27 2:57 PM
 * @Desc:
 * @Version: v1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class XSqlExecuteReq {
    private String catalogName;
    private String sql;
}
