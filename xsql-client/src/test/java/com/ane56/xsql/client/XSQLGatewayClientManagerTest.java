package com.ane56.xsql.client;

import com.ane56.xsql.common.exception.XSQLException;
import com.ane56.xsql.common.model.UltraResultRow;
import okhttp3.OkHttpClient;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.List;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/27 3:06 PM
 * @Desc:
 * @Version: v1.0
 */


public class XSQLGatewayClientManagerTest {
    private OkHttpClient httpClient;
    private XSQLGatewayClientManager xsqlGatewayClientManager;

    @Before
    public void beforeTest() {
        httpClient = new OkHttpClient.Builder().build();
        if (xsqlGatewayClientManager == null) {
            xsqlGatewayClientManager = XSQLGatewayClientManager.getInstance();
        }
    }

    @After
    public void afterTest() {

    }

    @Test()
    public void testQuerySelect1() throws IOException, XSQLException {
        URI uri = URI.create("http://127.0.0.1:6666");
        List<UltraResultRow> resultRows = xsqlGatewayClientManager.executeQuery("aliyun", "select 1", httpClient, uri);
        for (int i = 0; i < resultRows.size(); i++) {
            System.out.println("row index: " + i);
            UltraResultRow ultraResultRow = resultRows.get(i);
            System.out.println(ultraResultRow.getUltraResultSetData());
            System.out.println(ultraResultRow.getUltraResultSetMetaData());
        }
    }

    @Test()
    public void testQueryShowTables() throws IOException, XSQLException {
        URI uri = URI.create("http://127.0.0.1:6666");
        List<UltraResultRow> resultRows = xsqlGatewayClientManager.executeQuery("starrocks", "show tables", httpClient, uri);
        for (int i = 0; i < resultRows.size(); i++) {
            System.out.println("row index: " + i);
            UltraResultRow ultraResultRow = resultRows.get(i);
            System.out.println(ultraResultRow.getUltraResultSetData());
            System.out.println(ultraResultRow.getUltraResultSetMetaData());
        }
    }

    @Test()
    public void testQuerySelectStar() throws IOException, XSQLException {
        URI uri = URI.create("http://127.0.0.1:6666");
        List<UltraResultRow> resultRows = xsqlGatewayClientManager.executeQuery("aliyun", "select * from engine.t_click_logs limit 100", httpClient, uri);
        for (int i = 0; i < resultRows.size(); i++) {
            System.out.println("row index: " + i);
            UltraResultRow ultraResultRow = resultRows.get(i);
            System.out.println(ultraResultRow.getUltraResultSetData());
            System.out.println(ultraResultRow.getUltraResultSetMetaData());
        }
    }

    @Test()
    public void testExecute() throws IOException, XSQLException {
        URI uri = URI.create("http://127.0.0.1:6666");
        boolean execute = xsqlGatewayClientManager.execute("aliyun", "drop table if exists engine.xsql_test", httpClient, uri);
    }
}
