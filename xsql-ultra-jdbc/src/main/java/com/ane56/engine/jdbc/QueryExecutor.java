package com.ane56.engine.jdbc;

import com.ane56.xsql.client.XSQLGatewayClientManager;
import okhttp3.OkHttpClient;

import static com.ane56.engine.jdbc.StatementClientFactory.newStatementClient;
import static java.util.Objects.requireNonNull;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/24 11:39 AM
 * @Desc:
 * @Version: v1.0
 */

public class QueryExecutor {
    private final OkHttpClient httpClient;

    private XSQLGatewayClientManager xSqlGatewayClientManager;

    public QueryExecutor(OkHttpClient httpClient) {
        this.httpClient = requireNonNull(httpClient, "httpClient is null");
        xSqlGatewayClientManager = XSQLGatewayClientManager.getInstance();
    }

    public StatementClient startQuery(ClientSession session, String query) {
        return newStatementClient(httpClient, session, query, xSqlGatewayClientManager);
    }
}
