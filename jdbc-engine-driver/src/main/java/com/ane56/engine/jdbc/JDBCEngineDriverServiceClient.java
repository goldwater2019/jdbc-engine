package com.ane56.engine.jdbc;

import com.ane56.engine.jdbc.exetuor.JDBCEngineExecutorRef;
import com.ane56.engine.jdbc.thrift.service.JDBCEngineDriverService;
import com.ane56.engine.jdbc.thrift.struct.TJDBCCatalog;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.layered.TFramedTransport;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class JDBCEngineDriverServiceClient {
    String address = "localhost";
    int port = 7911;
    int timeout = 10 * 1000;
    int poolSize = 64;
    ExecutorService executorService = null;
    ExecutorService removeExecutorService = null;
    AtomicBoolean isRunning = new AtomicBoolean(true);
    Queue<JDBCEngineDriverService.Client> clientPool = new ConcurrentLinkedQueue<JDBCEngineDriverService.Client>();
    Map<JDBCEngineDriverService.Client, TTransport> client2tTransport = new ConcurrentHashMap<>();
    Map<JDBCEngineDriverService.Client, Boolean> client2flag = new ConcurrentHashMap<>();
    public JDBCEngineDriverServiceClient() {
        executorService = Executors.newFixedThreadPool(8);
        executorService.submit(new PushInClientPool());
        removeExecutorService = Executors.newFixedThreadPool(8);
        removeExecutorService.submit(new RemoveClientFromPool());
    }

    public static void main(String[] args) throws TTransportException {
        JDBCEngineDriverServiceClient jdbcEngineDriverServiceClient = new JDBCEngineDriverServiceClient();
        try {
            jdbcEngineDriverServiceClient.start();
        } catch (InterruptedException ignored) {

        } catch (Exception e) {
            System.out.println(e.getClass().getName());
            e.printStackTrace();
        }
    }

    public void start() throws Exception {
        try {
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < 10000; i++) {
                JDBCEngineDriverService.Client client = getClient();
                if (client == null) {
                    throw new Exception("client not initilized successfully");
                }
                testGetCatalogs(client);
                client2flag.put(client, false);
            }
            System.out.println(System.currentTimeMillis() - startTime);
            startTime = System.currentTimeMillis();
            for (int i = 0; i < 10000; i++) {
                JDBCEngineDriverService.Client client = getClient();
                if (client == null) {
                    throw new Exception("client not initilized successfully");
                }
                testHeartbeat(client);
                client2flag.put(client, false);
            }
            System.out.println(System.currentTimeMillis() - startTime);
        } catch (TException e) {
            e.printStackTrace();
        }
        isRunning.set(false);
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);
        executorService.shutdownNow();
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
                .host(address)
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

    private void close(TTransport transport) {
        if (transport != null && transport.isOpen()) {
            transport.close();
        }
    }


    public boolean hasReadyClient() {
        for (JDBCEngineDriverService.Client client : clientPool) {
            Boolean aBoolean = client2flag.get(client);
            if (aBoolean) {
                return true;
            }
        }
        return false;
    }

    public JDBCEngineDriverService.Client getClient() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        long duration = System.currentTimeMillis() - startTime;
        while (!hasReadyClient() || duration > timeout) {
            Thread.sleep(200L);
            duration = System.currentTimeMillis() - startTime;
        }
        for (Map.Entry<JDBCEngineDriverService.Client, Boolean> clientBooleanEntry : client2flag.entrySet()) {
            if (clientBooleanEntry.getValue()) {
                return clientBooleanEntry.getKey();
            }
        }
        return null;
    }

    public class RemoveClientFromPool implements Runnable {

        @Override
        public void run() {
            for (Map.Entry<JDBCEngineDriverService.Client, Boolean> clientBooleanEntry : client2flag.entrySet()) {
                Boolean isRunnable = clientBooleanEntry.getValue();
                if (!isRunnable) {
                    JDBCEngineDriverService.Client client = clientBooleanEntry.getKey();
                    TTransport tTransport = client2tTransport.get(client);
                    close(tTransport);
                    client2flag.remove(client);
                    client2tTransport.remove(client);
                    clientPool.remove(client);
                }
            }
        }
    }

    public class PushInClientPool implements Runnable {

        @Override
        public void run() {
            while (true) {
                if (!isRunning.get()) {
                    break;
                }
                if (clientPool.size() > poolSize) {
                    try {
                        Thread.sleep(200L);
                    } catch (InterruptedException e) {
                    }
                    continue;
                }
                try {
                    //使用非阻塞方式，按块的大小进行传输，类似于Java中的NIO。记得调用close释放资源
                    TTransport transport = new TFramedTransport(new TSocket(address, port, timeout));
                    //高效率的、密集的二进制编码格式进行数据传输协议
                    TProtocol protocol = new TCompactProtocol(transport);
                    JDBCEngineDriverService.Client client = new JDBCEngineDriverService.Client(protocol);
                    open(transport);
                    clientPool.add(client);
                    client2tTransport.put(client, transport);
                    client2flag.put(client, true);
                } catch (TTransportException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
