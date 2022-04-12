package com.ane56.engine.jdbc.driver;


import com.ane56.engine.jdbc.client.JDBCEngineDriverServiceClientManager;
import com.ane56.engine.jdbc.exception.JDBCEngineException;
import com.ane56.engine.jdbc.model.JDBCResultRef;
import com.ane56.engine.jdbc.model.thrift.JDBCEngineDriverServiceClientSuite;
import com.ane56.engine.jdbc.thrit.service.JDBCEngineDriverService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class DriverClientTest {

    private JDBCEngineDriverServiceClientManager jdbcEngineDriverServiceClientManager;
    private String jdbcEngineConfDir = "C:/workspace/jdbc-engine/conf";

    @Before
    public void beforeTest() throws JDBCEngineException {
        if (jdbcEngineDriverServiceClientManager == null) {
            jdbcEngineDriverServiceClientManager = JDBCEngineDriverServiceClientManager.getInstance(jdbcEngineConfDir);
        }
    }

    @Test
    public void testDriverQuery() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        int maxIterationNum = 10000;
        for (int i = 0; i < maxIterationNum; i++) {
            executorService.submit(new SyncQuery());
        }
        Thread.sleep(100000L);
        System.out.println(System.currentTimeMillis() - startTime);
    }

    public class AsyncQuery implements Callable<JDBCResultRef> {

        @Override
        public JDBCResultRef call() {
            String querySQL = "select * from tx_dev.bd_center_center_month;";
//            String querySQL = "";
            JDBCResultRef jdbcResultRef = null;
            try {
                JDBCEngineDriverServiceClientSuite availableClient = jdbcEngineDriverServiceClientManager.getAvailableClient();
                if (availableClient == null) {
                    throw new InterruptedException("get available client suite failed 3 times");
                }
                JDBCEngineDriverService.Client client = availableClient.getClient();
                jdbcResultRef = JDBCResultRef.parseFromTJDBCResultRef(client.query(querySQL));
                log.info(jdbcResultRef.toString());
                log.info("duration: " + (jdbcResultRef.getJdbcOperationRef().getEndTime() - jdbcResultRef.getJdbcOperationRef().getStartTime()));
                jdbcEngineDriverServiceClientManager.close(availableClient.getTTransport());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jdbcResultRef;
        }
    }

    public class SyncQuery implements Runnable {

        @Override
        public void run() {
            String querySQL = "select * from tx_dev.bd_center_center_month;";
//            String querySQL = "";
            JDBCResultRef jdbcResultRef;
            try {
                JDBCEngineDriverServiceClientSuite availableClient = jdbcEngineDriverServiceClientManager.getAvailableClient();
                if (availableClient == null) {
                    throw new InterruptedException("get available client suite failed 3 times");
                }
                JDBCEngineDriverService.Client client = availableClient.getClient();
                jdbcResultRef = JDBCResultRef.parseFromTJDBCResultRef(client.query(querySQL));
                log.info(jdbcResultRef.toString());
                log.info("duration: " + (jdbcResultRef.getJdbcOperationRef().getEndTime() - jdbcResultRef.getJdbcOperationRef().getStartTime()));
                jdbcEngineDriverServiceClientManager.close(availableClient.getTTransport());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
