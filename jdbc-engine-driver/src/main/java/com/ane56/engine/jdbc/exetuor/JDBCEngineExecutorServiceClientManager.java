package com.ane56.engine.jdbc.exetuor;


import com.ane56.engine.jdbc.model.JDBCEngineExecutorRef;
import com.ane56.engine.jdbc.model.thrift.JDBCEngineExecutorServiceClientSuite;
import com.ane56.engine.jdbc.thrit.service.JDBCEngineDriverService;
import com.ane56.engine.jdbc.thrit.service.JDBCEngineExecutorService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.layered.TFramedTransport;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JDBCEngineExecutorServiceClientManager {

    private static final int TIMEOUT = 10 * 1000;

    private static volatile JDBCEngineExecutorServiceClientManager singleton;

    public JDBCEngineExecutorServiceClientManager(String driverHost, int driverPort, int timeout, int poolSize) {
    }

    public static JDBCEngineExecutorServiceClientManager getInstance(String driverHost, int driverPort) {
        if (singleton == null) {
            synchronized (JDBCEngineExecutorServiceClientManager.class) {
                if (singleton == null) {
                    singleton = new JDBCEngineExecutorServiceClientManager();
                }
            }
        }
        return singleton;
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


    /**
     * 获得一个可用的client大礼包
     *
     * @param executorHost
     * @param executorPort
     * @return
     * @throws InterruptedException
     */
    public JDBCEngineExecutorServiceClientSuite getAvailableClient(String executorHost, int executorPort) throws InterruptedException {
        //使用非阻塞方式，按块的大小进行传输，类似于Java中的NIO。记得调用close释放资源
        try {
            TTransport transport = new TFramedTransport(new TSocket(executorHost, executorPort, TIMEOUT));
            //高效率的、密集的二进制编码格式进行数据传输协议
            TProtocol protocol = new TCompactProtocol(transport);
            JDBCEngineExecutorService.Client client = new JDBCEngineExecutorService.Client(protocol);
            open(transport);
            return JDBCEngineExecutorServiceClientSuite.builder()
                    .client(client)
                    .tTransport(transport)
                    .build();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void query(String querySQL, JDBCEngineExecutorRef jdbcEngineExecutorRef) throws InterruptedException {
        JDBCEngineExecutorServiceClientSuite availableClient = getAvailableClient(jdbcEngineExecutorRef.getHost(), jdbcEngineExecutorRef.getPort());
        JDBCEngineExecutorService.Client client = availableClient.getClient();
        TTransport tTransport = availableClient.getTTransport();

        client.query();

        close(tTransport);
    }
}
