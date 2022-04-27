package com.ane56.engine.jdbc.common;

import lombok.*;

/**
 * query运行时
 * 数据记录的地方
 *
 * @Author: zhangxinsen
 * @Date: 2022/4/24 5:34 PM
 * @Desc:
 * @Version: v1.0
 */

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class QueryStats {
    private String queryId;
    private String query;
    private long queryStartTime;
    private long queryEndTime;

    public static QueryStats create(String queryId, StatementStats stats) {
        return QueryStats.builder()
                .queryId(queryId)
                .query(stats.getQuery())
                .queryStartTime(stats.getQueryStartTime())
                .queryEndTime(stats.getQueryEndTime())
                .build();
    }
}

