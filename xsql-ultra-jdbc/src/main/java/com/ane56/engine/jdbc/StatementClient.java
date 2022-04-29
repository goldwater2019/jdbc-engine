package com.ane56.engine.jdbc;

import com.ane56.xsql.client.XSQLGatewayClientManager;
import com.ane56.xsql.common.exception.XSQLException;
import com.ane56.xsql.common.model.UltraCatalog;
import com.ane56.xsql.common.model.UltraResultRow;
import com.ane56.xsql.common.model.UltraResultSetMetaData;
import com.ane56.xsql.common.utils.ArrayUtil;
import lombok.*;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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

    private UltraConnection connection;

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
    public static boolean isQuery(String query) {
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
        // TODO 检测是否是切换backend
        boolean isChangeBackendCatalog = isChangeBackendCatalog();
        if (isChangeBackendCatalog) {
            changeBackendCatalog();
        }
        isShowCatalogs();
        if (isQuery(query)) {
            ultraResultSetMetaDataV2 = null;
            currentData = null;
            QueryContainer queryContainer = parseQueryContainer(clientSession, query);
            List<UltraResultRow> ultraResultRows = xsqlGatewayClientManager.executeQuery(
                    queryContainer.catalog,
                    queryContainer.sql,
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
            QueryContainer queryContainer = parseQueryContainer(clientSession, query);
            xsqlGatewayClientManager.execute(
                    queryContainer.catalog,
                    queryContainer.sql,
                    httpClient,
                    clientSession.getServer());
            return false;
        }
        return true;
    }

    private void changeBackendCatalog() {
        try {
            this.connection.setCatalog(clientSession.getCatalog());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断query是不是在修改catalog
     *
     * @return
     */
    private boolean isChangeBackendCatalog() {
        query = query.trim().toLowerCase(Locale.ROOT);
        if (!query.startsWith("set") || !query.contains("backend.catalog")) {
            return false;
        }
        String[] temp = query.split("=");
        if (temp.length != 2) {
            return false;
        }
        String newCatalogName = temp[1].trim();
        List<UltraCatalog> availableCatalogs = clientSession.getAvailableCatalogs();
        List<String> catalogNameList = availableCatalogs.stream().map(UltraCatalog::getName)
                .collect(Collectors.toList());
        if (catalogNameList.stream()
                .filter(x -> x.equals(newCatalogName))
                .collect(Collectors.toList()).size() == 0) {
            return false;
        }
        this.query = "select 'change backend.catalog successfully' as result";
        clientSession.setCatalog(newCatalogName);
        return true;
    }

    private void isShowCatalogs() {
        query = query.trim().toLowerCase(Locale.ROOT);
        String[] split = query.split("\\s+");
        if (split.length != 2) {
            return;
        }
        if (split[0].equals("show") && split[1].equals("catalogs")) {
            List<UltraCatalog> availableCatalogs = clientSession.getAvailableCatalogs();
            String catalogListStr = String.join(",", availableCatalogs.stream().map(UltraCatalog::getName).collect(Collectors.toList()));
            this.query = "select '" + catalogListStr + "' as catalogs";
            return;
        }
    }


    /**
     * 内部类, 用于解析数据并且临时保存query对象
     */
    private static class QueryContainer {
        private final String sql;
        private final String catalog;

        public QueryContainer(String sql, String catalog) {
            this.sql = sql;
            this.catalog = catalog;
        }
    }

    public QueryContainer parseQueryContainer(ClientSession clientSession, String query) {
        List<UltraCatalog> availableCatalogs = clientSession.getAvailableCatalogs();
        List<String> catalogNameList = new LinkedList<>();
        for (UltraCatalog availableCatalog : availableCatalogs) {
            catalogNameList.add(availableCatalog.getName());
        }
        ArrayUtil<String> stringArrayUtil = new ArrayUtil<>();
        if (isQuery(query)) {
            // from xxx.xxx
            boolean isKicked = false;
            String kickedCatalogName = null;
            String kickedSql = null;
            for (String catalogName : catalogNameList) {
                if (query.contains("from " + catalogName + ".")) {
                    kickedCatalogName = catalogName;
                    isKicked = true;
                    kickedSql = query.replace("from " + catalogName + ".", "from ");
                    break;
                }
            }
            if (isKicked) {
                return new QueryContainer(kickedSql, kickedCatalogName);
            } else {
                // from xxx.
                isKicked = false;
                kickedCatalogName = null;
                kickedSql = null;
                for (String catalogName : catalogNameList) {
                    if (query.contains("from " + catalogName)) {
                        kickedCatalogName = catalogName;
                        isKicked = true;
                        kickedSql = query.replace("from " + catalogName, "");
                        break;
                    }
                }
                if (isKicked) {
                    return new QueryContainer(kickedSql, kickedCatalogName);
                } else {
                    if (stringArrayUtil.isArrayContains(
                            catalogNameList,
                            clientSession.getCatalog(),
                            false
                    )) {
                        return new QueryContainer(query, clientSession.getCatalog());
                    } else {
                        return new QueryContainer(query, "UNKNOWN");
                    }
                }
            }
        } else {
            // from xxx;
            boolean isKicked = false;
            String kickedCatalogName = null;
            String kickedSql = null;
            for (String catalogName : catalogNameList) {
                if (query.contains(catalogName + ".")) {
                    kickedCatalogName = catalogName;
                    isKicked = true;
                    kickedSql = query.replace(catalogName + ".", "");
                    break;
                }
            }
            if (isKicked) {
                return new QueryContainer(kickedSql, kickedCatalogName);
            } else {
                if (stringArrayUtil.isArrayContains(
                        catalogNameList,
                        clientSession.getCatalog(),
                        false
                )) {
                    return new QueryContainer(query, clientSession.getCatalog());
                }
                return new QueryContainer(query, "UNKNOWN");
            }
        }
    }

}
