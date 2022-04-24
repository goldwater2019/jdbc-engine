package com.ane56.engine.jdbc.common;

import lombok.*;

import java.util.Optional;
import java.util.OptionalDouble;

import static java.lang.Math.min;
import static java.util.Objects.requireNonNull;

/**
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
public class QueryStats
{
    private String queryId;
    private String state;
    private boolean waitingForPrerequisites;
    private boolean queued;
    private boolean scheduled;
    private int nodes;

    static QueryStats create(String queryId, StatementStats stats)
    {
        return QueryStats.builder()
                .queryId(queryId)
                .state(stats.getState())
                .waitingForPrerequisites(stats.isWaitingForPrerequisites())
                .queued(stats.isQueued())
                .scheduled(stats.isScheduled())
                .nodes(stats.getNodes())
                .build();
    }
}

