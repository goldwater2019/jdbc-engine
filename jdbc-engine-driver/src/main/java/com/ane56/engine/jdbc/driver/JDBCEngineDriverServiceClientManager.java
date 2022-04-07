package com.ane56.engine.jdbc.driver;


import com.ane56.engine.jdbc.model.JDBCEngineExecutorRef;
import com.ane56.engine.jdbc.model.thrift.JDBCEngineDriverServiceClientSuite;
import com.ane56.engine.jdbc.thrit.service.JDBCEngineDriverService;
import com.ane56.engine.jdbc.thrit.struct.TJDBCCatalog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JDBCEngineDriverServiceClientManager {
    private static volatile JDBCEngineDriverServiceClientManager singleton;
    Map<JDBCEngineDriverService.Client, TTransport> client2tTransport;

    private int driverPort;
    private String driverHost;
    private int timeout;
    private int poolSize;

    public JDBCEngineDriverServiceClientManager(String driverHost, int driverPort, int timeout, int poolSize) {
        setPoolSize(poolSize);
        setDriverPort(driverPort);
        setDriverHost(driverHost);
        setTimeout(timeout);
        if (client2tTransport == null) {
            client2tTransport = new ConcurrentHashMap<>();
        }
    }

    public static JDBCEngineDriverServiceClientManager getInstance(String driverHost, int driverPort) {
        if (singleton == null) {
            synchronized (JDBCEngineDriverServiceClientManager.class) {
                if (singleton == null) {
                    singleton = new JDBCEngineDriverServiceClientManager(driverHost, driverPort, 10 * 1000, 8);
                }
            }
        }
        singleton.setDriverHost(driverHost);
        singleton.setDriverPort(driverPort);
        return singleton;
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


    public JDBCEngineDriverServiceClientSuite innerGetAvailableClient() throws InterruptedException {
        //使用非阻塞方式，按块的大小进行传输，类似于Java中的NIO。记得调用close释放资源
        try {
            TTransport transport = new TFramedTransport(new TSocket(driverHost, driverPort, timeout));
            //高效率的、密集的二进制编码格式进行数据传输协议
            TProtocol protocol = new TCompactProtocol(transport);
            JDBCEngineDriverService.Client client = new JDBCEngineDriverService.Client(protocol);
            open(transport);
            return JDBCEngineDriverServiceClientSuite.builder()
                    .client(client)
                    .tTransport(transport)
                    .build();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * TODO 使用连接池的方式提高性能
     * 重试三次
     * @return
     */
    public JDBCEngineDriverServiceClientSuite getAvailableClient(){
        boolean isError = true;
        int errorCnt = 0;
        JDBCEngineDriverServiceClientSuite jdbcEngineDriverServiceClientSuite = null;
        while (isError) {
            try {
                jdbcEngineDriverServiceClientSuite = innerGetAvailableClient();
                isError = false;
            } catch (InterruptedException e) {
                errorCnt += 1;
                if (errorCnt == 3) {
                    e.printStackTrace();
                    break;
                }
            }
        }
        return jdbcEngineDriverServiceClientSuite;
    }
}
