package com.ane56.engine.jdbc;

import org.junit.Test;

import java.sql.*;
import java.util.Properties;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/27 7:06 PM
 * @Desc:
 * @Version: v1.0
 */

public class UltraDriverTest {

    private static final String USERNAME = "adhoc";
    private static final String PASSWORD = "adhoc";
    private static final String DRIVER_NAME = "com.ane56.engine.jdbc.UltraDriver";
    private static final String JDBC_URL = "jdbc:ultra://127.0.0.1:6666/starrocks";


    @Test
    public void testHelloWorld() {
        System.out.println("hello world");
    }

    @Test
    public void testGetConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER_NAME);
        Properties connectionProperties = new Properties();
        connectionProperties.setProperty("user", USERNAME);
        connectionProperties.setProperty("password", PASSWORD);
        connectionProperties.setProperty("applicationName", "ultra-test");
        Connection conn = DriverManager.getConnection(JDBC_URL, connectionProperties);
        conn.close();
    }

    @Test
    public void testExecute() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER_NAME);
        Properties connectionProperties = new Properties();
        connectionProperties.setProperty("user", USERNAME);
        connectionProperties.setProperty("password", PASSWORD);
        connectionProperties.setProperty("applicationName", "ultra-test");
        Connection conn = DriverManager.getConnection(JDBC_URL, connectionProperties);
        Statement statement = conn.createStatement();
        statement.execute("drop table if exists xsql_test");
        statement.execute("use tx_dev");
        conn.close();
    }

    @Test
    public void testQuery() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER_NAME);
        Properties connectionProperties = new Properties();
        connectionProperties.setProperty("user", USERNAME);
        connectionProperties.setProperty("password", PASSWORD);
        connectionProperties.setProperty("applicationName", "ultra-test");
        Connection conn = DriverManager.getConnection(JDBC_URL, connectionProperties);
        Statement statement = conn.createStatement();
        String sql = "select 1";
        ResultSet resultSet = statement.executeQuery(sql);
//        printSchemaAndData(sql, resultSet);

        sql = "select max(update_time) as max_update_time " +
                "from tx_dev.bd_manager_area_station";
        resultSet = statement.executeQuery(sql);
//        printSchemaAndData(sql, resultSet);


        sql = "select * " +
                "from tx_dev.bd_manager_area_station order by update_time " +
                "limit 100 offset 2";
        resultSet = statement.executeQuery(sql);
//        printSchemaAndData(sql, resultSet);


        sql = "show create table tx_dev.bd_manager_area_station";
        resultSet = statement.executeQuery(sql);
//        printSchemaAndData(sql, resultSet);

        sql = "show tables";
        resultSet = statement.executeQuery(sql);
//        printSchemaAndData(sql, resultSet);

        sql = "select update_time from tx_dev.bd_manager_area_station limit 10";
        resultSet = statement.executeQuery(sql);
        resultSet.next();
        System.out.println(resultSet.getObject(1));
        conn.close();
    }

    private void printSchemaAndData(String sql, ResultSet resultSet) throws SQLException {
        System.out.println("execute query: " + sql);
        boolean flag = true;
        while (resultSet.next()) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            if (flag) {
                System.out.print("|");
                for (int i = 0; i < columnCount; i++) {
                    System.out.print("--------------------|");
                }
                System.out.println("");

                System.out.print("|");
                for (int i = 0; i < columnCount; i++) {
                    System.out.print(center(metaData.getColumnLabel(i + 1), 20));
                    System.out.print("|");
                }
                System.out.println("");

                System.out.print("|");
                for (int i = 0; i < columnCount; i++) {
                    System.out.print("--------------------|");
                }
                System.out.println("");
                flag = false;
            }
            System.out.print("|");
            for (int i = 0; i < columnCount; i++) {
                System.out.print(center(resultSet.getString(metaData.getColumnLabel(i + 1)), 20));
                System.out.print("|");
            }
            System.out.println("");

            System.out.print("|");
            for (int i = 0; i < columnCount; i++) {
                System.out.print("--------------------|");
            }
            System.out.println("");
        }
        System.out.println("\n");
    }

    private String center(String str, int size) {
        int length = str.length();
        int rest = size - length;
        int left = (int) ((size - length) / 2);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < left; i++) {
            sb.append(" ");
        }
        sb.append(str);
        for (int i = 0; i < rest - left; i++) {
            sb.append(" ");
        }
        return sb.toString();
    }

    //    @Test
    public void testMySQL() throws SQLException, ClassNotFoundException {
//        .name("aliyun")
//                            .driverClassName("com.mysql.cj.jdbc.Driver")
//                            .isAvailable(true)
//                            .isForbidden(false)
//                            .jdbcUrl("jdbc:mysql://rm-uf67xpwhzp9xvuciv2o.mysql.rds.aliyuncs.com:3306")
//                            .username("root")
//                            .password("Luxin@19980516")
//                            .build()
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://rm-uf67xpwhzp9xvuciv2o.mysql.rds.aliyuncs.com:3306", "root", "Luxin@19980516");
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from engine.t_click_logs limit 100 offset 10");
        while (resultSet.next()) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                System.out.println(metaData.getColumnLabel(i + 1));
            }
            for (int i = 0; i < columnCount; i++) {
                System.out.printf(" %10s |", resultSet.getString(i + 1));
            }
        }
    }
}
