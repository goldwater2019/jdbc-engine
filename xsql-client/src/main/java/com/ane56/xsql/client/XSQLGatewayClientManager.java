package com.ane56.xsql.client;

import com.alibaba.fastjson.JSONObject;
import com.ane56.xsql.common.exception.XSQLException;
import com.ane56.xsql.common.model.UltraResultRow;
import com.ane56.xsql.common.model.XSqlExecuteReq;
import lombok.*;
import okhttp3.*;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/27 2:47 PM
 * @Desc:
 * @Version: v1.0
 */

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class XSQLGatewayClientManager {
    private OkHttpClient httpClient;
    private URI baseUri;

    private static volatile XSQLGatewayClientManager singleton;

    /**
     * 单例模式, 获得相应的数据
     * //     * @param baseUri: 网关访问uri, e.g. http://localhost:6666
     * //     * @param okHttpClient: manager调用的httpClient
     *
     * @return
     */
    public static XSQLGatewayClientManager getInstance() {
        if (singleton == null) {
            synchronized (XSQLGatewayClientManager.class) {
                if (singleton == null) {
                    singleton = new XSQLGatewayClientManager();
                }
            }
        }
        return singleton;
    }

    public XSQLGatewayClientManager() {};

    /**
     * 查询语句调用
     *
     * @param catalog
     * @param sql
     * @param httpClient
     * @param baseUri
     * @return
     */
    public List<UltraResultRow> executeQuery(String catalog, String sql, OkHttpClient httpClient, URI baseUri) throws IOException, XSQLException {
        URI uri = URI.create(String.format("%s/%s", baseUri.toString(), "api/v1/driver/catalog/query"));
        XSqlExecuteReq xSqlExecuteReq = XSqlExecuteReq.builder()
                .catalogName(catalog)
                .sql(sql)
                .build();
        String reqJsonString = JSONObject.toJSONString(xSqlExecuteReq);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), reqJsonString);
        Request request = new Request.Builder().url(uri.toString()).post(requestBody).build();
        Response response = httpClient.newCall(request).execute();
        // TODO 路由错误, 400, 504, 503 ,etc 的处理
        if (response.body() == null) {
            // TODO 抛出异常
        }
        String responseJsonStr = response.body().string();
        JSONObject jsonObject = JSONObject.parseObject(responseJsonStr);
        Integer msgCode = jsonObject.getInteger("code");
        if (msgCode == 0) {
            return JSONObject.parseArray(jsonObject.getString("data"), UltraResultRow.class);
        }
        throw new XSQLException(jsonObject.getString("msg"));
    }

    /**
     * update / create / ... execute类型语句调用
     *
     * @param catalog
     * @param sql
     * @param httpClient
     * @param baseUri
     * @return
     */
    public boolean execute(String catalog, String sql, OkHttpClient httpClient, URI baseUri) throws IOException, XSQLException {
        URI uri = URI.create(String.format("%s/%s", baseUri.toString(), "api/v1/driver/catalog/execute"));
        XSqlExecuteReq xSqlExecuteReq = XSqlExecuteReq.builder()
                .catalogName(catalog)
                .sql(sql)
                .build();
        String reqJsonString = JSONObject.toJSONString(xSqlExecuteReq);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), reqJsonString);
        Request request = new Request.Builder().url(uri.toString()).post(requestBody).build();
        Response response = httpClient.newCall(request).execute();
        // TODO 路由错误, 400, 504, 503 ,etc 的处理
        if (response.body() == null) {
            // TODO 抛出异常
        }
        String responseJsonStr = response.body().string();
        JSONObject jsonObject = JSONObject.parseObject(responseJsonStr);
        Integer msgCode = jsonObject.getInteger("code");
        if (msgCode == 0) {
            return jsonObject.getBoolean("data");
        }
        throw new XSQLException(jsonObject.getString("msg"));
    }
}
