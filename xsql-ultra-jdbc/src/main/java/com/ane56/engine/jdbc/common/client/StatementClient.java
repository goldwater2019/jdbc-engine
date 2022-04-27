package com.ane56.engine.jdbc.common.client;

import com.ane56.engine.jdbc.common.QueryData;
import com.ane56.engine.jdbc.resultset.UltraResultSet;
import lombok.*;
import okhttp3.OkHttpClient;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/24 3:12 PM
 * @Desc:
 * @Version: v1.0
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class StatementClient {

    private OkHttpClient httpClient;
    private ClientSession clientSession;
    private String query;
    private QueryData currentData;
    private UltraResultSet resultSet;


    /**
     * TODO 新增此处的数据处理
     *
     * @return
     */
    boolean advance() {
        return true;
    }

}
