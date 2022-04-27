package com.ane56.engine.jdbc.common.client;

import com.ane56.engine.jdbc.common.client.impl.StatementClientImpl;
import okhttp3.OkHttpClient;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/24 4:02 PM
 * @Desc:
 * @Version: v1.0
 */

public final class StatementClientFactory
{
    private StatementClientFactory() {}

    public static StatementClient newStatementClient(OkHttpClient httpClient, ClientSession session, String query)
    {
        return StatementClient.builder()
                .httpClient(httpClient)
                .session(session)
                .query(query)
                .build();
    }
}