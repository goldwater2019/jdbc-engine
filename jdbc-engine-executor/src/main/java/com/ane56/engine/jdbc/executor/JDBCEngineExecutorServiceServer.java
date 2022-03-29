package com.ane56.engine.jdbc.executor;

import com.ane56.engine.jdbc.driver.JDBCEngineDriverServiceClientManager;
import com.ane56.engine.jdbc.executor.impl.JDBCEngineExecutorServiceImpl;
import com.ane56.engine.jdbc.executor.pool.connection.PooledDataSourceManager;
import com.ane56.engine.jdbc.model.JDBCCatalog;
import com.ane56.engine.jdbc.model.thrift.JDBCEngineDriverServiceClientSuite;
import com.ane56.engine.jdbc.thrit.service.JDBCEngineDriverService;
import com.ane56.engine.jdbc.thrit.service.JDBCEngineExecutorService;
import com.ane56.engine.jdbc.thrit.struct.TJDBCCatalog;
import com.ane56.engine.jdbc.thrit.struct.TJDBCEngineExecutor;
import com.ane56.engine.jdbc.utils.NetUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.layered.TFramedTransport;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Slf4j
public class JDBCEngineExecutorServiceServer {
    String sql = "select send_date as sdate, * from tx_dev.bd_center_center_month";

    private String driverHost = "127.0.0.1";
    private int driverPort = 8888;
    private int servicePort = 8889;
    private JDBCEngineDriverServiceClientManager jdbcEngineDriverServiceClientManager = null;
    private PooledDataSourceManager pooledDataSourceManager = null;
    private ScheduledExecutorService heartBeatExecutorService = null;
    private ScheduledExecutorService refreshCatalogExecutorService = null;
    private UUID engineRefId =  UUID.randomUUID();

    private static volatile JDBCEngineExecutorServiceServer singleton;

    /**
     * 单例方法
     *
     * @param driverHost
     * @param driverPort
     * @param servicePort
     * @return
     */
    public static JDBCEngineExecutorServiceServer getInstance(String driverHost, int driverPort, int servicePort) {
        if (singleton == null) {
            synchronized (JDBCEngineExecutorServiceServer.class) {
                if (singleton == null) {
                    // TODO 关闭随机端口
                    singleton = new JDBCEngineExecutorServiceServer(driverHost, driverPort, Math.abs(new Random().nextInt()) % 10000 + 5000);
                }
            }
        }
        return singleton;
    }

    /**
     * 构造方法
     *
     * @param driverHost
     * @param driverPort
     * @param servicePort
     */
    private JDBCEngineExecutorServiceServer(String driverHost, int driverPort, int servicePort) {
        setDriverHost(driverHost);
        setDriverPort(driverPort);
        setServicePort(servicePort);
        checkInitialStatus();
    }


    /**
     * 检查初始化状态
     */
    private void checkInitialStatus() {
        if (jdbcEngineDriverServiceClientManager == null) {
            jdbcEngineDriverServiceClientManager = JDBCEngineDriverServiceClientManager.getInstance(driverHost, driverPort);
        }
        if (pooledDataSourceManager == null) {
            pooledDataSourceManager = PooledDataSourceManager.getInstance();
        }
        if (heartBeatExecutorService == null) {
            heartBeatExecutorService = Executors.newSingleThreadScheduledExecutor();
            heartBeatExecutorService.scheduleAtFixedRate(new HeartBeatRunnable(), 0L, 1000L, TimeUnit.MILLISECONDS);
        }
        if (refreshCatalogExecutorService == null) {
            refreshCatalogExecutorService = Executors.newSingleThreadScheduledExecutor();
            refreshCatalogExecutorService.scheduleAtFixedRate(new RefreshCatalogs(), 2000L, 10000L, TimeUnit.MILLISECONDS);
        }
    }

    public void invoke() throws TTransportException {
        // 非阻塞式的，配合TFramedTransport使用
        TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(servicePort);
        // 关联处理器与Service服务的实现
        TProcessor processor = new JDBCEngineExecutorService.Processor<JDBCEngineExecutorService.Iface>(new JDBCEngineExecutorServiceImpl(
                driverHost,
                driverPort
        ));
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
        log.info("Starting executor service server on port " + servicePort + "......");
        server.serve();
    }


    /**
     * 定时上报心跳
     */
    private class HeartBeatRunnable implements Runnable {
        @Override
        public void run() {
            JDBCEngineDriverServiceClientSuite availableClient = null;
            try {
                availableClient = jdbcEngineDriverServiceClientManager.getAvailableClient();
                JDBCEngineDriverService.Client client = availableClient.getClient();
                TTransport tTransport = availableClient.getTTransport();
                TJDBCEngineExecutor tJdbcEngineExecutor = new TJDBCEngineExecutor();
                tJdbcEngineExecutor.setHost(NetUtils.getInetHostAddress());
                tJdbcEngineExecutor.setPort(servicePort);
                tJdbcEngineExecutor.setExecutorRefId(engineRefId.toString());
                tJdbcEngineExecutor.setPrefix("");
                client.heartBeat(tJdbcEngineExecutor);
                jdbcEngineDriverServiceClientManager.close(tTransport);
            } catch (InterruptedException | TException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 定时更新catalogs
     */
    private class RefreshCatalogs implements Runnable {

        @Override
        public void run() {
            JDBCEngineDriverServiceClientSuite availableClient = null;
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
            } catch (InterruptedException | TException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws TTransportException {
        String driverHost = "127.0.0.1";
        int driverPort = 8888;
        int servicePort = 8889;
        JDBCEngineExecutorServiceServer jdbcEngineExecutorServiceServer = JDBCEngineExecutorServiceServer.getInstance(driverHost, driverPort, servicePort);
        jdbcEngineExecutorServiceServer.invoke();
    }
}
