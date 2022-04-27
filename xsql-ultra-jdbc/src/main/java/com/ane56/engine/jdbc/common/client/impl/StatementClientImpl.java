package com.ane56.engine.jdbc.common.client.impl;

import com.ane56.engine.jdbc.common.QueryData;
import com.ane56.engine.jdbc.common.QueryStatusInfo;
import com.ane56.engine.jdbc.common.StatementStats;
import com.ane56.engine.jdbc.common.client.ClientSession;
import com.ane56.engine.jdbc.common.client.StatementClient;
import lombok.*;
import okhttp3.OkHttpClient;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * StatementClient实现类
 * 真正执行query的地方
 *
 * @Author: zhangxinsen
 * @Date: 2022/4/24 4:03 PM
 * @Desc:
 * @Version: v1.0
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class StatementClientImpl implements StatementClient {
    private OkHttpClient httpClient;
    private ClientSession session;
    private String query;

    @Override
    public String getQuery() {
        return query;
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public boolean isClientAborted() {
        return false;
    }

    @Override
    public boolean isClientError() {
        return false;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public StatementStats getStats() {
        return null;
    }

    @Override
    public QueryStatusInfo currentStatusInfo() {
        return null;
    }

    @Override
    public QueryData currentData() {
        return null;
    }

    @Override
    public QueryStatusInfo finalStatusInfo() {
        return null;
    }

    @Override
    public Optional<String> getSetCatalog() {
        return Optional.empty();
    }

    @Override
    public Optional<String> getSetSchema() {
        return Optional.empty();
    }

    @Override
    public Map<String, String> getSetSessionProperties() {
        return null;
    }

    @Override
    public Set<String> getResetSessionProperties() {
        return null;
    }

    @Override
    public Map<String, String> getAddedPreparedStatements() {
        return null;
    }

    @Override
    public Set<String> getDeallocatedPreparedStatements() {
        return null;
    }

    @Nullable
    @Override
    public String getStartedTransactionId() {
        return null;
    }

    @Override
    public boolean isClearTransactionId() {
        return false;
    }

    @Override
    public Map<String, String> getAddedSessionFunctions() {
        return null;
    }

    @Override
    public Set<String> getRemovedSessionFunctions() {
        return null;
    }

    @Override
    public boolean advance() {
        return false;
    }

    @Override
    public void cancelLeafStage() {

    }

    @Override
    public void close() {

    }
}
