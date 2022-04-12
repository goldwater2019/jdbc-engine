package com.ane56.engine.jdbc.executor.impl;

import com.ane56.engine.jdbc.client.JDBCEngineDriverServiceClientManager;
import com.ane56.engine.jdbc.enumeration.JDBCQueryStatus;
import com.ane56.engine.jdbc.exception.JDBCEngineException;
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
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;
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
@Data
@Builder
public class JDBCEngineExecutorServiceImpl implements JDBCEngineExecutorService.Iface {

    private JDBCEngineDriverServiceClientManager jdbcEngineDriverServiceClientManager;
    private PooledDataSourceManager pooledDataSourceManager;
    private String jdbcConfDir;

    public JDBCEngineExecutorServiceImpl(String jdbcConfDir) throws JDBCEngineException {
        setJdbcConfDir(jdbcConfDir);
        checkInitialStatus();
    }

    @Override
    public TJDBCResultRef query(TJDBCOperationRef jdbcOperationRef) {
        long startTime = System.currentTimeMillis();
        try {
            checkInitialStatus();
        } catch (JDBCEngineException e) {
            e.printStackTrace();
        }
        JDBCOperationRef operationRef = JDBCOperationRef.builder()
                .startTime(jdbcOperationRef.getStartTime())
                .endTime(jdbcOperationRef.getEndTime())
                .queryStatus(JDBCQueryStatus.OK)
                .message("")
                .operationRefId(UUID.fromString(jdbcOperationRef.getOperationRefId()))
                .catalogName(jdbcOperationRef.getCatalogName())
                .sqlStatement(jdbcOperationRef.getSqlStatement())
                .build();

        log.info("create JDBCOperationRef costs: " + (System.currentTimeMillis() - startTime));
        JDBCResultSet jdbcResultSet = null;
        try {
            jdbcResultSet = pooledDataSourceManager.query(jdbcOperationRef.getCatalogName(), jdbcOperationRef.getSqlStatement());
        } catch (SQLException e) {
            operationRef.setQueryStatus(JDBCQueryStatus.FAILED);
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw, true));
            operationRef.setMessage(sw.toString());
        }
        operationRef.setEndTime(System.currentTimeMillis());
        JDBCResultRef jdbcResultRef = JDBCResultRef.builder()
                .jdbcOperationRef(operationRef)
                .jdbcResultSet(jdbcResultSet)
                .build();
        return jdbcResultRef.asTJDBCResultRef();
    }

    private void checkInitialStatus() throws JDBCEngineException {
        if (jdbcEngineDriverServiceClientManager == null) {
            jdbcEngineDriverServiceClientManager = JDBCEngineDriverServiceClientManager.getInstance(getJdbcConfDir());
        }
        if (pooledDataSourceManager == null) {
            pooledDataSourceManager = PooledDataSourceManager.getInstance();
        }
    }
}
