package com.ane56.engine.jdbc.executor;


import com.ane56.engine.jdbc.exetuor.JDBCEngineExecutorServiceClientManager;
import com.ane56.engine.jdbc.model.JDBCEngineExecutorRef;
import com.ane56.engine.jdbc.model.JDBCResultRef;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

@Slf4j
public class ExecutorClientTest {

    private JDBCEngineExecutorServiceClientManager jdbcEngineExecutorServiceClientManager;

    @Before
    public void beforeTest() {
        if (jdbcEngineExecutorServiceClientManager == null) {
            jdbcEngineExecutorServiceClientManager = JDBCEngineExecutorServiceClientManager.getInstance();
        }
    }

    @Test
    public void testQuery() throws InterruptedException, TException {
        long startTime = System.currentTimeMillis();
        int maxIterationNum = 1000;
        for (int i = 0; i < maxIterationNum; i++) {
            JDBCEngineExecutorRef engineExecutorRef = JDBCEngineExecutorRef.builder()
                    .executorRefId(UUID.randomUUID())
                    .host("192.168.66.117")
                    .port(8889)
                    .build();
            String querySQL = "select * from tx_dev.bd_center_center_month;";
//            String querySQL = "";
            JDBCResultRef jdbcResultRef = jdbcEngineExecutorServiceClientManager.query("starrocks", querySQL, engineExecutorRef);
            System.out.println(jdbcResultRef);
            log.info("index: " + i + ", duration: " + (jdbcResultRef.getJdbcOperationRef().getEndTime() - jdbcResultRef.getJdbcOperationRef().getStartTime()));
        }
        System.out.println(System.currentTimeMillis() - startTime);
    }
}
