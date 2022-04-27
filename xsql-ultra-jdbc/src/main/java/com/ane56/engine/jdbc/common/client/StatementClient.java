package com.ane56.engine.jdbc.common.client;

import com.ane56.engine.jdbc.common.QueryData;
import com.ane56.engine.jdbc.common.QueryStatusInfo;
import com.ane56.engine.jdbc.common.StatementStats;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/24 3:12 PM
 * @Desc:
 * @Version: v1.0
 */

public interface StatementClient
        extends Closeable
{
    String getQuery();

    boolean isRunning();

    boolean isClientAborted();

    boolean isClientError();

    boolean isFinished();

    StatementStats getStats();

    QueryStatusInfo currentStatusInfo();

    QueryData currentData();

    QueryStatusInfo finalStatusInfo();

    Optional<String> getSetCatalog();

    Optional<String> getSetSchema();

    Map<String, String> getSetSessionProperties();

    Set<String> getResetSessionProperties();

    Map<String, String> getAddedPreparedStatements();

    Set<String> getDeallocatedPreparedStatements();

    @Nullable
    String getStartedTransactionId();

    boolean isClearTransactionId();

    Map<String, String> getAddedSessionFunctions();

    Set<String> getRemovedSessionFunctions();

    boolean advance();

    void cancelLeafStage();

    @Override
    void close();
}
