package com.ane56.engine.jdbc.executor;

import com.ane56.engine.jdbc.client.JDBCEngineDriverServiceClientManager;
import com.ane56.engine.jdbc.config.JDBCEngineConfig;
import com.ane56.engine.jdbc.exception.JDBCEngineException;
import com.ane56.engine.jdbc.executor.impl.JDBCEngineExecutorServiceImpl;
import com.ane56.engine.jdbc.executor.pool.connection.PooledDataSourceManager;
import com.ane56.engine.jdbc.model.JDBCCatalog;
import com.ane56.engine.jdbc.model.thrift.JDBCEngineDriverServiceClientSuite;
import com.ane56.engine.jdbc.thrit.service.JDBCEngineDriverService;
import com.ane56.engine.jdbc.thrit.service.JDBCEngineExecutorService;
import com.ane56.engine.jdbc.thrit.struct.TJDBCCatalog;
import com.ane56.engine.jdbc.utils.LogUtils;
import com.ane56.engine.jdbc.utils.NetUtils;
import com.ane56.engine.jdbc.utils.PathUtils;
import com.ane56.engine.jdbc.utils.ZkUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TProcessor;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.layered.TFramedTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class JDBCEngineExecutorServiceServer {
    private static final int retryTimes = 3;
    private static final UUID ENGINE_REF_ID = UUID.randomUUID();
    private static volatile JDBCEngineExecutorServiceServer singleton;
    private static Map<String, String> configMap = new HashMap<>();
    private int servicePort;
    private JDBCEngineDriverServiceClientManager jdbcEngineDriverServiceClientManager;
    private PooledDataSourceManager pooledDataSourceManager;
    private ScheduledExecutorService heartBeatExecutorService;
    private ScheduledExecutorService refreshCatalogExecutorService;
    private static final Logger logger = LoggerFactory.getLogger(JDBCEngineExecutorServiceServer.class);

    /**
     * 构造方法
     */
    private JDBCEngineExecutorServiceServer(int servicePort) throws JDBCEngineException {
        setServicePort(servicePort);
        checkInitialStatus();
    }

    /**
     * 单例方法
     * TODO 自增端口
     */
    public static JDBCEngineExecutorServiceServer getInstance() throws JDBCEngineException {
        if (singleton == null) {
            synchronized (JDBCEngineExecutorServiceServer.class) {
                if (singleton == null) {
                    // TODO 关闭随机端口
                    singleton = new JDBCEngineExecutorServiceServer(Math.abs(new Random().nextInt()) % 10000 + 5000);
                }
            }
        }
        return singleton;
    }

    public static void main(String[] args) throws Exception {
        for (String arg : args) {
            if (arg.startsWith("--config-dir")) {
                String configDir = arg.split("=")[1];
                configMap.put("jdbc.engine.driver.config.path", configDir);
            }
        }

        String givenConfigDir = configMap.get("jdbc.engine.driver.config.path");
        if (givenConfigDir == null) {
            String jdbcEngineHome = System.getenv("JDBC_ENGINE_HOME");
            if (jdbcEngineHome != null) {
                configMap.put(
                        "jdbc.engine.driver.config.path",
                        PathUtils.checkAndCombinePath(jdbcEngineHome, "/conf"));
            }
        }
        givenConfigDir = configMap.get("jdbc.engine.driver.config.path");
        if (givenConfigDir == null) {
            throw new JDBCEngineException("at least, env variable JDBC_ENGINE_HOME or args --conf-dir should be specified");
        }

        String configDir = configMap.get("jdbc.engine.driver.config.path");

        LogUtils.getInstance().loadLog4jProperties(configDir);

        JDBCEngineExecutorServiceServer jdbcEngineExecutorServiceServer = JDBCEngineExecutorServiceServer.getInstance();
        jdbcEngineExecutorServiceServer.heartBeat();
        jdbcEngineExecutorServiceServer.invoke();
        ZkUtils zkUtils = ZkUtils.getInstance(configDir);
        zkUtils.changeRunningStatus(false);
    }

    /**
     * 检查初始化状态
     */
    private void checkInitialStatus() throws JDBCEngineException {
        if (jdbcEngineDriverServiceClientManager == null) {
            String configDir = configMap.getOrDefault("jdbc.engine.driver.config.path", "C:/workspace/jdbc-engine/conf");
            jdbcEngineDriverServiceClientManager = JDBCEngineDriverServiceClientManager.getInstance(configDir);
        }
        if (pooledDataSourceManager == null) {
            pooledDataSourceManager = PooledDataSourceManager.getInstance();
        }
        if (refreshCatalogExecutorService == null) {
            refreshCatalogExecutorService = Executors.newSingleThreadScheduledExecutor();
            refreshCatalogExecutorService.scheduleAtFixedRate(new RefreshCatalogs(), 2000L, 10000L, TimeUnit.MILLISECONDS);
        }
    }

    public void invoke() throws TTransportException, JDBCEngineException {
        String configPath = configMap.get("jdbc.engine.driver.config.path"); // , "C:/workspace/jdbc-engine/conf");
        // 非阻塞式的，配合TFramedTransport使用
        TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(servicePort);
        // 关联处理器与Service服务的实现
        TProcessor processor = new JDBCEngineExecutorService.Processor<JDBCEngineExecutorService.Iface>(new JDBCEngineExecutorServiceImpl(configPath));
        // 目前Thrift提供的最高级的模式，可并发处理客户端请求
        TThreadedSelectorServer.Args args = new TThreadedSelectorServer.Args(serverTransport);
        args.processor(processor);
        // 设置协议工厂，高效率的、密集的二进制编码格式进行数据传输协议
        args.protocolFactory(new TCompactProtocol.Factory());
        // 设置传输工厂，使用非阻塞方式，按块的大小进行传输，类似于Java中的NIO
        args.transportFactory(new TFramedTransport.Factory());
        // 设置处理器工厂,只返回一个单例实例
        args.processorFactory(new TProcessorFactory(processor));
        // 多个线程，主要负责客户端的IO处理
        args.selectorThreads(2);
        // 工作线程池
        ExecutorService pool = Executors.newFixedThreadPool(3);
        args.executorService(pool);
        TThreadedSelectorServer server = new TThreadedSelectorServer(args);
        logger.info("Starting executor service server on port " + servicePort + "......");
        server.serve();
    }

    /**
     * 定时上报心跳
     */
    private void heartBeat() throws Exception {
        String configDir = configMap.getOrDefault("jdbc.engine.driver.config.path", "C:/workspace/jdbc-engine/conf");
        ZkUtils zkUtils = ZkUtils.getInstance(configDir);
        zkUtils.createEphemeralNode(
                PathUtils.checkAndCombinePath(
                        JDBCEngineConfig.getInstance(configDir).getHaZookeeperExecutorUriPath(),
                        String.join(":",
                                NetUtils.getInetHostAddress(),
                                String.valueOf(servicePort))
                )
        );
    }

    /**
     * 定时更新catalogs
     */
    private class RefreshCatalogs implements Runnable {

        @Override
        public void run() {
            JDBCEngineDriverServiceClientSuite availableClient;
            try {
                availableClient = jdbcEngineDriverServiceClientManager.getAvailableClient();
                JDBCEngineDriverService.Client client = availableClient.getClient();
                TTransport tTransport = availableClient.getTTransport();
                List<TJDBCCatalog> catalogs = client.getCatalogs();
                for (TJDBCCatalog tjdbcCatalog : catalogs) {
                    pooledDataSourceManager.addCatalog(JDBCCatalog.builder()
                            .name(tjdbcCatalog.getName())
                            .driverClass(tjdbcCatalog.getDriver())
                            .uri(tjdbcCatalog.getUrl())
                            .username(tjdbcCatalog.getUsername())
                            .password(tjdbcCatalog.getPassword())
                            .build());
                }
                pooledDataSourceManager.checkDataSources();
                jdbcEngineDriverServiceClientManager.close(tTransport);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
