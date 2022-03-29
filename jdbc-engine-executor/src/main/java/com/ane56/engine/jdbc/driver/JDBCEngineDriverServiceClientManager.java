package com.ane56.engine.jdbc.driver;


import com.ane56.engine.jdbc.model.JDBCEngineExecutorRef;
import com.ane56.engine.jdbc.model.thrift.JDBCEngineDriverServiceClientSuite;
import com.ane56.engine.jdbc.thrit.service.JDBCEngineDriverService;
import com.ane56.engine.jdbc.thrit.struct.TJDBCCatalog;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.layered.TFramedTransport;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class JDBCEngineDriverServiceClientManager {
    private int driverPort;
    private String driverHost;
    private int timeout;
    private int poolSize;
    ExecutorService executorService = null;
    AtomicBoolean isRunning = new AtomicBoolean(false);
    Map<JDBCEngineDriverService.Client, TTransport> client2tTransport = new ConcurrentHashMap<>();

    private static volatile JDBCEngineDriverServiceClientManager singleton;

    public static JDBCEngineDriverServiceClientManager getInstance(String driverHost, int driverPort) {
        if (singleton == null) {
            synchronized (JDBCEngineDriverServiceClientManager.class) {
                if (singleton == null) {
                    singleton = new JDBCEngineDriverServiceClientManager(driverHost, driverPort, 10 * 1000, 8);
                }
            }
        }
        return singleton;
    }

    public AtomicBoolean getIsRunning() {
        return isRunning;
    }

    public void setIsRunning(AtomicBoolean isRunning) {
        this.isRunning = isRunning;
    }

    public int getDriverPort() {
        return driverPort;
    }

    public void setDriverPort(int driverPort) {
        this.driverPort = driverPort;
    }

    public String getDriverHost() {
        return driverHost;
    }

    public void setDriverHost(String driverHost) {
        this.driverHost = driverHost;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public JDBCEngineDriverServiceClientManager(String driverHost, int driverPort, int timeout, int poolSize) {
        setPoolSize(poolSize);
        setDriverPort(driverPort);
        setDriverHost(driverHost);
        setTimeout(timeout);
        // focus on generate client
        executorService = Executors.newFixedThreadPool(1);
        executorService.submit(new PushInClientPool());
        isRunning.set(true);
    }


    public static void main(String[] args) throws TTransportException {
        JDBCEngineDriverServiceClientManager jdbcEngineDriverServiceClientManager = new JDBCEngineDriverServiceClientManager(
                "localhost",
                8888,
                10 * 1000,
                8
        );
    }

    private void testGetCatalogs(JDBCEngineDriverService.Client client) throws TException {
        List<TJDBCCatalog> catalogs = client.getCatalogs();
        for (TJDBCCatalog catalog : catalogs) {
//            System.out.println(catalog);
        }
    }

    private void testHeartbeat(JDBCEngineDriverService.Client client) throws TException {
        Random random = new Random();
        JDBCEngineExecutorRef jdbcEngineExecutorRef = JDBCEngineExecutorRef.builder()
                .host(driverHost)
                .executorRefId(UUID.randomUUID())
                .port(random.nextInt())
                .prefixPath("/dev")
                .latestHeartbeatTime(System.currentTimeMillis())
                .build();
        client.heartBeat(jdbcEngineExecutorRef.asTJDBCEngineExecutor());
    }

    private void open(TTransport transport) {
        if (transport != null && !transport.isOpen()) {
            try {
                transport.open();
            } catch (TTransportException e) {
                e.printStackTrace();
            }
        }
    }

    public void close(TTransport transport) {
        if (transport != null && transport.isOpen()) {
            transport.close();
        }
    }


    public JDBCEngineDriverServiceClientSuite getAvailableClient() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        long duration = System.currentTimeMillis() - startTime;
        while (client2tTransport.size() == 0 || duration > timeout) {
            Thread.sleep(200L);
            duration = System.currentTimeMillis() - startTime;
        }
        if (client2tTransport.size() == 0) {
            return null;
        }
        JDBCEngineDriverService.Client client = null;
        for (Map.Entry<JDBCEngineDriverService.Client, TTransport> clientTTransportEntry : client2tTransport.entrySet()) {
            client = clientTTransportEntry.getKey();
            break;
        }
        TTransport tTransport = client2tTransport.remove(client);
        return JDBCEngineDriverServiceClientSuite.builder()
                .client(client)
                .tTransport(tTransport)
                .build();
    }

    public class PushInClientPool implements Runnable {

        @Override
        public void run() {
            while (true) {
                if (!isRunning.get()) {
                    break;
                }
                if (client2tTransport.size() > poolSize) {
                    try {
                        Thread.sleep(200L);
                    } catch (InterruptedException e) {
                    }
                    continue;
                }
                try {
                    //使用非阻塞方式，按块的大小进行传输，类似于Java中的NIO。记得调用close释放资源
                    TTransport transport = new TFramedTransport(new TSocket(driverHost, driverPort, timeout));
                    //高效率的、密集的二进制编码格式进行数据传输协议
                    TProtocol protocol = new TCompactProtocol(transport);
                    JDBCEngineDriverService.Client client = new JDBCEngineDriverService.Client(protocol);
                    open(transport);
                    client2tTransport.put(client, transport);
                } catch (TTransportException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void shutdown() throws InterruptedException {
        getIsRunning().set(false);
        Thread.sleep(1000L);
        executorService.shutdownNow();
    }
}
