package com.ane56.engine.jdbc;

import com.ane56.xsql.client.XSQLGatewayClientManager;
import com.ane56.xsql.common.exception.XSQLException;
import com.ane56.xsql.common.model.UltraCatalog;
import com.ane56.xsql.common.model.UltraDatabaseMetaData;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.util.List;

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

    public StatementClient startQuery(UltraConnection connection, ClientSession session, String query) {
        return newStatementClient(connection, httpClient, session, query, xSqlGatewayClientManager);
    }

    public List<UltraCatalog> getCatalogs(ClientSession session) throws XSQLException, IOException {
        return xSqlGatewayClientManager.getCatalogs(httpClient, session.getServer());
    }

    public UltraDatabaseMetaData getDatabaseMetaData(ClientSession session, String catalogName) throws XSQLException, IOException {
        return xSqlGatewayClientManager.getDatabaseMetaData(catalogName, httpClient, session.getServer());
    }
}
