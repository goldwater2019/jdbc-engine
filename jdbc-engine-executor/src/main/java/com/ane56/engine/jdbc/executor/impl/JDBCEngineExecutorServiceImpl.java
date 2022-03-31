package com.ane56.engine.jdbc.executor.impl;

import com.ane56.engine.jdbc.driver.JDBCEngineDriverServiceClientManager;
import com.ane56.engine.jdbc.enumeration.JDBCQueryStatus;
import com.ane56.engine.jdbc.executor.pool.connection.PooledDataSourceManager;
import com.ane56.engine.jdbc.model.JDBCOperationRef;
import com.ane56.engine.jdbc.model.JDBCResultRef;
import com.ane56.engine.jdbc.model.JDBCResultSet;
import com.ane56.engine.jdbc.thrit.service.JDBCEngineExecutorService;
import com.ane56.engine.jdbc.thrit.struct.TJDBCOperationRef;
import com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

import java.sql.SQLException;
import java.util.UUID;

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

    public JDBCEngineExecutorServiceImpl(String driverHost, int driverPort) {
        setDriverHost(driverHost);
        setDriverPort(driverPort);
        checkInitialStatus(driverHost, driverPort);
    }

    @Override
    public TJDBCResultRef query(TJDBCOperationRef jdbcOperationRef) throws TException {
        checkInitialStatus(getDriverHost(), getDriverPort());
        JDBCOperationRef operationRef = JDBCOperationRef.builder()
                .startTime(jdbcOperationRef.getStartTime())
                .endTime(jdbcOperationRef.getEndTime())
                .queryStatus(JDBCQueryStatus.OK)
                .message("")
                .operationRefId(UUID.fromString(jdbcOperationRef.getOperationRefId()))
                .catalogName(jdbcOperationRef.getCatalogName())
                .sqlStatement(jdbcOperationRef.getSqlStatement())
                .build();


        JDBCResultSet jdbcResultSet = null;
        try {
            jdbcResultSet = pooledDataSourceManager.query(jdbcOperationRef.getCatalogName(), jdbcOperationRef.getSqlStatement());
        } catch (SQLException e) {
            operationRef.setQueryStatus(JDBCQueryStatus.FAILED);
            operationRef.setMessage(e.getMessage());  // TODO 使用更完善的错误捕获
        }
        operationRef.setEndTime(System.currentTimeMillis());
        JDBCResultRef jdbcResultRef = JDBCResultRef.builder()
                .jdbcOperationRef(operationRef)
                .jdbcResultSet(jdbcResultSet)
                .build();
        return jdbcResultRef.asTJDBCResultRef();
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
