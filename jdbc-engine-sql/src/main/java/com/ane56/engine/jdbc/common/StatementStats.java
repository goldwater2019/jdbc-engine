package com.ane56.engine.jdbc.common;

import lombok.*;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/24 4:06 PM
 * @Desc:
 * @Version: v1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class StatementStats {
    private String queryId;
    private String query;
    private long queryStartTime;
    private long queryEndTime;
}
