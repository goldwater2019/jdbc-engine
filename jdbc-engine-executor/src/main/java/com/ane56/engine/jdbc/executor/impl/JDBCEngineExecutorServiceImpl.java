package com.ane56.engine.jdbc.executor.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.ane56.engine.jdbc.driver.JDBCEngineDriverServiceClientManager;
import com.ane56.engine.jdbc.executor.pool.connection.PooledDataSourceManager;
import com.ane56.engine.jdbc.thrit.enumeration.TJDBCColumnType;
import com.ane56.engine.jdbc.thrit.enumeration.TJDBCQueryStatus;
import com.ane56.engine.jdbc.thrit.service.JDBCEngineExecutorService;
import com.ane56.engine.jdbc.thrit.struct.TJDBCOperationRef;
import com.ane56.engine.jdbc.thrit.struct.TJDBCResultColumn;
import com.ane56.engine.jdbc.thrit.struct.TJDBCResultSet;
import com.ane56.engine.jdbc.thrit.struct.TJDBCRsultRow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: zhangxinsen
 * @Date: 2022/3/29 11:19 PM
 * @Desc:
 * @Version: v1.0
 */

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class JDBCEngineExecutorServiceImpl implements JDBCEngineExecutorService.Iface {

    private String driverHost;
    private int driverPort;

    private JDBCEngineDriverServiceClientManager jdbcEngineDriverServiceClientManager;
    private PooledDataSourceManager pooledDataSourceManager;
    // TODO 将心跳移动至server侧
    // TODO 通过定时任务更新连接池

    public JDBCEngineExecutorServiceImpl(String driverHost, int driverPort) {
        setDriverHost(driverHost);
        setDriverPort(driverPort);
        checkInitialStatus(driverHost, driverPort);
    }

    @Override
    public TJDBCOperationRef query(TJDBCOperationRef jdbcOperationRef) throws TException {
        checkInitialStatus(getDriverHost(), getDriverPort());
        TJDBCOperationRef tjdbcOperationRef = new TJDBCOperationRef();
        tjdbcOperationRef.setStartTime(System.currentTimeMillis());
        tjdbcOperationRef.setOperationRefId(jdbcOperationRef.getOperationRefId());
        tjdbcOperationRef.setCatalogName(jdbcOperationRef.getCatalogName());
        tjdbcOperationRef.setSqlStatement(jdbcOperationRef.getSqlStatement());
        DruidDataSource druidDataSource = pooledDataSourceManager.getName2source().get(jdbcOperationRef.getCatalogName());
        while (druidDataSource == null) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            druidDataSource = pooledDataSourceManager.getName2source().get(jdbcOperationRef.getCatalogName());
        }
        queryWithDataSource(tjdbcOperationRef, druidDataSource);
        tjdbcOperationRef.setEndTime(System.currentTimeMillis());
        return tjdbcOperationRef;
    }

    /**
     * @param tjdbcOperationRef
     * @param druidDataSource
     */
    private void queryWithDataSource(TJDBCOperationRef tjdbcOperationRef, DruidDataSource druidDataSource) {
        try {
            DruidPooledConnection connection = druidDataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(tjdbcOperationRef.getSqlStatement());
            ResultSet resultSet = preparedStatement.executeQuery();
            tjdbcOperationRef.setMessage("query seccuess");
            tjdbcOperationRef.setQueryStatus(TJDBCQueryStatus.OK);
            TJDBCResultSet tjdbcResultSet = new TJDBCResultSet();
            List<TJDBCRsultRow> resultRowList = new LinkedList<>();
            while (resultSet.next()) {
                TJDBCRsultRow tjdbcRsultRow = new TJDBCRsultRow();
                ResultSetMetaData metaData = resultSet.getMetaData();
                // 获得总共有多少列
                int columnCount = metaData.getColumnCount();
                List<TJDBCResultColumn> columnList = new LinkedList<>();
                for (int i = 0; i < columnCount; i++) {
                    i = i + 1;
                    TJDBCResultColumn tjdbcResultColumn = new TJDBCResultColumn();
                    tjdbcResultColumn.setColumnName(metaData.getColumnName(i));
                    tjdbcResultColumn.setColumnClassName(metaData.getColumnClassName(i));
                    tjdbcResultColumn.setColumnValue(resultSet.getString(i));
                    TJDBCColumnType tjdbcColumnType = TJDBCColumnType.findByValue(metaData.getColumnType(i));
                    if (tjdbcColumnType == null) {
                        tjdbcColumnType = TJDBCColumnType.OTHER;
                    }
                    tjdbcResultColumn.setColumnType(tjdbcColumnType);
                    columnList.add(tjdbcResultColumn);
                }
                tjdbcRsultRow.setColumnList(columnList);
                resultRowList.add(tjdbcRsultRow);
            }
            tjdbcResultSet.setResultSet(resultRowList);
            tjdbcOperationRef.setTJDBCResultSet(tjdbcResultSet);
        } catch (SQLException e) {
            tjdbcOperationRef.setQueryStatus(TJDBCQueryStatus.FAILED);
            tjdbcOperationRef.setMessage(e.getMessage());
        }
        tjdbcOperationRef.setEndTime(System.currentTimeMillis());
    }

    private void checkInitialStatus(String driverHost, int driverPort) {
        if (jdbcEngineDriverServiceClientManager == null) {
            jdbcEngineDriverServiceClientManager = JDBCEngineDriverServiceClientManager.getInstance(driverHost, driverPort);
        }
        if (pooledDataSourceManager == null) {
            pooledDataSourceManager = PooledDataSourceManager.getInstance();
        }
    }
}
