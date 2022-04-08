package com.ane56.engine.jdbc.pressure;

import com.ane56.engine.jdbc.client.JDBCEngineDriverServiceClientManager;
import com.ane56.engine.jdbc.config.JDBCEngineConfig;
import com.ane56.engine.jdbc.model.JDBCCatalog;
import com.ane56.engine.jdbc.model.JDBCResultRef;
import com.ane56.engine.jdbc.model.thrift.JDBCEngineDriverServiceClientSuite;
import com.ane56.engine.jdbc.thrit.service.JDBCEngineDriverService;
import com.ane56.engine.jdbc.thrit.struct.TJDBCCatalog;
import com.ane56.engine.jdbc.utils.PathUtils;
import com.ane56.engine.jdbc.utils.ZkUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;


@Slf4j
public class DriverServerSideQuery {
    private static JDBCEngineDriverServiceClientManager jdbcEngineDriverServiceClientManager;

    public static void main02(String[] args) throws Exception {
        ZkUtils zkUtils = ZkUtils.getInstance(JDBCEngineConfig.haZookeeperQuorum);
        for (int i = 0; i < 0; i++) {
            Random random = new Random();
            int port = Math.abs(random.nextInt() % 5000 + 5000);
            zkUtils.createEphemeralNode(
                    PathUtils.checkAndCombinePath(JDBCEngineConfig.haZookeeperDriverUriPath, "localhost:" + port)
            );
        }
        Thread.sleep(1000L);
        List<String> childrenUnderZNodePath = zkUtils.getChildrenUnderZNodePath(JDBCEngineConfig.haZookeeperDriverUriPath);
        for (String childPath : childrenUnderZNodePath) {
            System.out.println(childPath);
        }

        if (jdbcEngineDriverServiceClientManager == null) {
            jdbcEngineDriverServiceClientManager = JDBCEngineDriverServiceClientManager.getInstance();
        }

        JDBCEngineDriverServiceClientSuite availableClientV2 = jdbcEngineDriverServiceClientManager.getAvailableClient();
        JDBCEngineDriverService.Client v2Client = availableClientV2.getClient();
        List<TJDBCCatalog> catalogs = v2Client.getCatalogs();
        for (TJDBCCatalog catalog : catalogs) {
            log.info("catalog: " + JDBCCatalog.parseFromTJDBCCatalog(catalog));
        }
        jdbcEngineDriverServiceClientManager.close(availableClientV2.getTTransport());

        log.info("close zk client");
        zkUtils.changeRunningStatus(false);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String driverHost = null;
        Integer driverPort = null;
        Integer maxIterationNum = null;
        Integer threadNum = null;
        for (String arg : args) {
            if (arg.startsWith("--driverHost")) {
                driverHost = arg.trim().split("=")[1];
            }
            if (arg.startsWith("--driverPort")) {
                driverPort = Integer.parseInt(arg.trim().split("=")[1]);
            }
            if (arg.startsWith("--maxIterationNum")) {
                maxIterationNum = Integer.parseInt(arg.trim().split("=")[1]);
            }
            if (arg.startsWith("--threadNum")) {
                threadNum = Integer.parseInt(arg.trim().split("=")[1]);
            }
        }
        if (driverHost == null) {
            driverHost = "127.0.0.1";
        }
        if (driverPort == null) {
            driverPort = 8888;
        }
        if (maxIterationNum == null) {
            maxIterationNum = 1000;
        }
        if (threadNum == null) {
            threadNum = 32;
        }

        log.info("driver host: " + driverHost);
        log.info("driver port: " + driverPort);
        log.info("max iteration num: " + maxIterationNum);
        log.info("thread num: " + threadNum);

        if (jdbcEngineDriverServiceClientManager == null) {
            jdbcEngineDriverServiceClientManager = JDBCEngineDriverServiceClientManager.getInstance();
        }

        long startTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        List<Future<?>> futureList = new LinkedList<>();
        for (int i = 0; i < maxIterationNum; i++) {
            Future<JDBCResultRef> submit = executorService.submit(new AsyncQuery());
            futureList.add(submit);
        }
        for (Future<?> future : futureList) {
            future.get();
        }
        long duration = System.currentTimeMillis() - startTime;
        log.info("query: " + maxIterationNum + " times with " + threadNum + " threads, costs " + duration + " ms");
        executorService.shutdown();
        if (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
            executorService.shutdownNow();
        }
    }


    static class AsyncQuery implements Callable<JDBCResultRef> {

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
}
