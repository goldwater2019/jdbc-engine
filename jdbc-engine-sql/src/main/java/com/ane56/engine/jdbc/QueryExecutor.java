package com.ane56.engine.jdbc;

import okhttp3.OkHttpClient;

import static java.util.Objects.requireNonNull;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/24 11:39 AM
 * @Desc:
 * @Version: v1.0
 */

public class QueryExecutor {
    private final OkHttpClient httpClient;

    public QueryExecutor(OkHttpClient httpClient)
    {
        this.httpClient = requireNonNull(httpClient, "httpClient is null");
    }
}
