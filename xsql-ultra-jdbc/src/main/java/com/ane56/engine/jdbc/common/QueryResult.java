package com.ane56.engine.jdbc.common;

import lombok.*;

import java.net.URI;
import java.util.List;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/24 9:37 PM
 * @Desc:
 * @Version: v1.0
 */

// @Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class QueryResult implements QueryStatusInfo, QueryData {
    private String id;
    private URI infoUri;
    private URI partialCancelUri;
    private URI nextUri;
    private List<Column> columns;
    private Iterable<List<Object>> data;
    private StatementStats stats;
    private QueryError error;
    private String updateType;
    private Long updateCount;

    @Override
    public Iterable<List<Object>> getData() {
        return data;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public URI getInfoUri() {
        return infoUri;
    }

    @Override
    public URI getPartialCancelUri() {
        return partialCancelUri;
    }

    @Override
    public URI getNextUri() {
        return nextUri;
    }

    @Override
    public List<Column> getColumns() {
        return columns;
    }

    @Override
    public StatementStats getStats() {
        return stats;
    }

    @Override
    public QueryError getError() {
        return error;
    }

    @Override
    public String getUpdateType() {
        return updateType;
    }

    @Override
    public Long getUpdateCount() {
        return updateCount;
    }
}
