package com.ane56.engine.jdbc;

import com.ane56.xsql.client.XSQLGatewayClientManager;
import com.ane56.xsql.common.exception.XSQLException;
import com.ane56.xsql.common.model.UltraResultRow;
import com.ane56.xsql.common.model.UltraResultSetMetaData;
import lombok.*;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

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
@Data
public class StatementClient {

    private OkHttpClient httpClient;
    private ClientSession clientSession;
    private String query;
    private QueryData currentData;
    private UltraResultSet resultSet;
    private UltraResultSetMetaDataV2 ultraResultSetMetaDataV2;
    private XSQLGatewayClientManager xsqlGatewayClientManager;

    public static List<String> EXECUTE_DICT = new LinkedList<>();

    static {
        // TODO 补全此处的字典
        EXECUTE_DICT.add("CREATE");
        EXECUTE_DICT.add("DROP");
        EXECUTE_DICT.add("DELETE");
        EXECUTE_DICT.add("UPDATE");
        EXECUTE_DICT.add("USE");
        EXECUTE_DICT.add("SET");
        EXECUTE_DICT.add("TRUNCATE");
    }

    /**
     * 判断SQL是否为查询
     *
     * @return
     */
    public boolean isQuery() {
        String queryStr = query.toUpperCase(Locale.ROOT).trim();
        boolean isQuery = true;
        for (String s : EXECUTE_DICT) {
            if (queryStr.startsWith(s)) {
                isQuery = false;
                break;
            }
        }
        return isQuery;
    }

    /**
     * 将currentData和metaData换成相应的resultSet
     */
    public void refreshResultSet(UltraStatement statement) throws SQLException {
        this.resultSet = new UltraResultSet(statement, this, 500);
    }

    /**
     * @return
     */
    public boolean advance() throws XSQLException, IOException {
        if (isQuery()) {
            ultraResultSetMetaDataV2 = null;
            currentData = null;
            List<UltraResultRow> ultraResultRows = xsqlGatewayClientManager.executeQuery(clientSession.getCatalog(),
                    query,
                    httpClient,
                    clientSession.getServer());
            List<List<Object>> data = new LinkedList<>();

            for (UltraResultRow ultraResultRow : ultraResultRows) {
                List<Object> ultraResultSetData = ultraResultRow.getUltraResultSetData();
                data.add(ultraResultSetData);
                UltraResultSetMetaData ultraResultSetMetaData = ultraResultRow.getUltraResultSetMetaData();
                if (ultraResultSetMetaDataV2 == null) {
                    ultraResultSetMetaDataV2 = new UltraResultSetMetaDataV2(ultraResultSetMetaData.getColumnMetaDataList());
                }
            }
            currentData = QueryData.builder()
                    .data(data)
                    .build();
        } else {
            xsqlGatewayClientManager.execute(clientSession.getCatalog(),
                    query,
                    httpClient,
                    clientSession.getServer());
            return false;
        }
        return true;
    }

}
