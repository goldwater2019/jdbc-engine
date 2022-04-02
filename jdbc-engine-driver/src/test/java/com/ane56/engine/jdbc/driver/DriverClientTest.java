package com.ane56.engine.jdbc.driver;


import com.ane56.engine.jdbc.model.JDBCResultRef;
import com.ane56.engine.jdbc.model.thrift.JDBCEngineDriverServiceClientSuite;
import com.ane56.engine.jdbc.thrit.service.JDBCEngineDriverService;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class DriverClientTest {

    private JDBCEngineDriverServiceClientManager jdbcEngineDriverServiceClientManager;

    @Before
    public void beforeTest() {
        if (jdbcEngineDriverServiceClientManager == null) {
            jdbcEngineDriverServiceClientManager = JDBCEngineDriverServiceClientManager.getInstance("127.0.0.1", 8888);
        }
    }

    @Test
    public void testDriverQuery() throws InterruptedException, TException, ExecutionException {
        long startTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        int maxIterationNum = 1000;
        List<Future<JDBCResultRef>> jdbcResultRefFeatureList = new LinkedList<>();
        for (int i = 0; i < maxIterationNum; i++) {
            Future<JDBCResultRef> jdbcResultRefFuture = executorService.submit(new AsyncQuery());
            jdbcResultRefFeatureList.add(jdbcResultRefFuture);
        }
        for (Future<JDBCResultRef> jdbcResultRefFuture : jdbcResultRefFeatureList) {
            JDBCResultRef jdbcResultRef = jdbcResultRefFuture.get();
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
                JDBCEngineDriverServiceClientSuite availableClient = jdbcEngineDriverServiceClientManager.getAvailableClient();
                JDBCEngineDriverService.Client client = availableClient.getClient();
                jdbcResultRef = JDBCResultRef.parseFromTJDBCResultRef(client.query(querySQL));
                log.info(jdbcResultRef.toString());
                log.info("duration: " + (jdbcResultRef.getJdbcOperationRef().getEndTime() - jdbcResultRef.getJdbcOperationRef().getStartTime()));
                jdbcEngineDriverServiceClientManager.close(availableClient.getTTransport());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TException e) {
                e.printStackTrace();
            }
            return jdbcResultRef;
        }
    }
}
