package com.ane56.engine.jdbc;

import com.ane56.xsql.client.XSQLGatewayClientManager;
import okhttp3.OkHttpClient;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/24 4:02 PM
 * @Desc:
 * @Version: v1.0
 */

public final class StatementClientFactory {
    private StatementClientFactory() {
    }

    public static StatementClient newStatementClient(OkHttpClient httpClient,
                                                     ClientSession session,
                                                     String query,
                                                     XSQLGatewayClientManager xSqlGatewayClientManager) {
        return StatementClient
                .builder()
                .httpClient(httpClient)
                .clientSession(session)
                .query(query)
                .xsqlGatewayClientManager(xSqlGatewayClientManager)
                .build();
    }
}