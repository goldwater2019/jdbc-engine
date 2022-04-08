package com.ane56.engine.jdbc.executor;


import com.ane56.engine.jdbc.client.JDBCEngineExecutorServiceClientManager;
import com.ane56.engine.jdbc.model.JDBCEngineExecutorRef;
import com.ane56.engine.jdbc.model.JDBCResultRef;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

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
    public void testExecutorQuery() throws InterruptedException, TException, ExecutionException {
        long startTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        int maxIterationNum = 1000;
        List<Future<JDBCResultRef>> jdbcResultRefFeatureList = new LinkedList<>();
        for (int i = 0; i < maxIterationNum; i++) {
            Future<JDBCResultRef> jdbcResultRefFuture = executorService.submit(new AsyncQuery());
            jdbcResultRefFeatureList.add(jdbcResultRefFuture);
        }
        for (Future<JDBCResultRef> jdbcResultRefFuture : jdbcResultRefFeatureList) {
            jdbcResultRefFuture.get();
        }
        System.out.println(System.currentTimeMillis() - startTime);
    }

    public class AsyncQuery implements Callable<JDBCResultRef> {

        @Override
        public JDBCResultRef call() {
            String querySQL = "select * from tx_dev.bd_center_center_month;";
//            String querySQL = "";
            JDBCResultRef jdbcResultRef = null;
            try {
                jdbcResultRef = jdbcEngineExecutorServiceClientManager.query("starrocks", querySQL);
                log.info(jdbcResultRef.toString());
                log.info("duration: " + (jdbcResultRef.getJdbcOperationRef().getEndTime() - jdbcResultRef.getJdbcOperationRef().getStartTime()));

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return jdbcResultRef;
        }
    }
}
