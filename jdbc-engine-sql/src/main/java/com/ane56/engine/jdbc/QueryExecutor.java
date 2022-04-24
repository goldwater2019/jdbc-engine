package com.ane56.engine.jdbc;

import com.ane56.engine.jdbc.common.client.ClientSession;
import com.ane56.engine.jdbc.common.client.StatementClient;
import okhttp3.OkHttpClient;

import static com.ane56.engine.jdbc.common.client.StatementClientFactory.newStatementClient;
import static java.util.Objects.requireNonNull;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/24 11:39 AM
 * @Desc:
 * @Version: v1.0
 */

public class QueryExecutor {
    private final OkHttpClient httpClient;

    public QueryExecutor(OkHttpClient httpClient) {
        this.httpClient = requireNonNull(httpClient, "httpClient is null");
    }

    public StatementClient startQuery(ClientSession session, String query) {
        return newStatementClient(httpClient, session, query);
    }
}
