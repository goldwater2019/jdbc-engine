package com.ane56.engine.jdbc.client;


import com.alibaba.fastjson.JSONObject;
import com.ane56.engine.jdbc.model.JDBCQueryReq;
import com.ane56.engine.jdbc.model.JDBCResultRef;
import com.ane56.engine.jdbc.model.JsonResult;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Slf4j
public class JDBCEngineGatewayClientManager {

    private static volatile JDBCEngineGatewayClientManager singleton;

    public static JDBCEngineGatewayClientManager getInstance() {
        if (singleton == null) {
            synchronized (JDBCEngineGatewayClientManager.class) {
                if (singleton == null) {
                    singleton = new JDBCEngineGatewayClientManager();
                }
            }
        }
        return singleton;
    }

    public JsonResult<JDBCResultRef> querySql(String querySql, String baseUrl) throws IOException {
        String requestUrl = baseUrl + "/driver/query";
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        JDBCQueryReq jdbcQueryReq = JDBCQueryReq.builder()
                .querySql(querySql)
                .build();
        String json = JSONObject.toJSONString(jdbcQueryReq);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder().url(requestUrl).post(requestBody).build();
        Response response = okHttpClient.newCall(request).execute();
        String responseJsonStr = response.body().string();
        JSONObject jsonObject = JSONObject.parseObject(responseJsonStr);
        JDBCResultRef data = JSONObject.parseObject(
                jsonObject.getString("data"),
                JDBCResultRef.class
        );
        return new JsonResult<JDBCResultRef>(
                data,
                jsonObject.getString("msg"),
                jsonObject.getInteger("code")
        );
    }

    public static void main(String[] args) throws IOException {
        JDBCEngineGatewayClientManager jdbcEngineGatewayClientManager = JDBCEngineGatewayClientManager.getInstance();
        for (int i = 0; i < 100; i++) {
            JsonResult<JDBCResultRef> jdbcResultRefJsonResult = jdbcEngineGatewayClientManager.querySql(
                    "select * from aliyun.engine.t_click_logs limit 100,2",
                    "http://localhost:8080"
            );
            log.info(JSONObject.toJSONString(jdbcResultRefJsonResult));
        }
    }
}
