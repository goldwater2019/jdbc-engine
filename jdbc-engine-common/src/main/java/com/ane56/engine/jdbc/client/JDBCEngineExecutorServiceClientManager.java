package com.ane56.engine.jdbc.client;

import com.ane56.engine.jdbc.enumeration.JDBCQueryStatus;
import com.ane56.engine.jdbc.model.JDBCEngineExecutorRef;
import com.ane56.engine.jdbc.model.JDBCOperationRef;
import com.ane56.engine.jdbc.model.JDBCResultRef;
import com.ane56.engine.jdbc.model.thrift.JDBCEngineExecutorServiceClientSuite;
import com.ane56.engine.jdbc.thrit.service.JDBCEngineExecutorService;
import com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.layered.TFramedTransport;

import java.util.UUID;


@Data
@AllArgsConstructor
@Builder
@Slf4j
public class JDBCEngineExecutorServiceClientManager {

    private static final int TIMEOUT = 10 * 1000;
    private static volatile JDBCEngineExecutorServiceClientManager singleton;
    private JDBCEngineExecutorRefManager jdbcEngineExecutorRefManager;

    public JDBCEngineExecutorServiceClientManager() {
        if (jdbcEngineExecutorRefManager == null) {
            jdbcEngineExecutorRefManager = JDBCEngineExecutorRefManager.getInstance();
        }
    }

    public static JDBCEngineExecutorServiceClientManager getInstance() {
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


    /**
     * @param catalogName           : 待查询的catalogName
     * @param querySQL              : 执行的SQL
     * @param jdbcEngineExecutorRef : 对应到那个jdbcExecutor进行执行
     * @return
     * @throws InterruptedException
     * @throws TException
     */
    public JDBCResultRef query(String catalogName, String querySQL, JDBCEngineExecutorRef jdbcEngineExecutorRef) throws InterruptedException, TException {
        long startTime = System.currentTimeMillis();
        JDBCEngineExecutorServiceClientSuite availableClient = getAvailableClient(jdbcEngineExecutorRef.getHost(), jdbcEngineExecutorRef.getPort());
        log.info("get client costs: " + (System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
        JDBCEngineExecutorService.Client client = availableClient.getClient();
        TTransport tTransport = availableClient.getTTransport();
        jdbcEngineExecutorRefManager.accessJDBCEngineRef(jdbcEngineExecutorRef);  // 更新最新的接入时间

        // 先创建一个oepration的对象
        JDBCOperationRef jdbcOperationRef = JDBCOperationRef.builder()
                .startTime(System.currentTimeMillis())
                .queryStatus(JDBCQueryStatus.OK)
                .endTime(0L)
                .catalogName(catalogName)
                .operationRefId(UUID.randomUUID())
                .sqlStatement(querySQL)
                .message("")
                .build();
        log.info("create JDBCOperationRef costs: " + (System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
        TJDBCResultRef tjdbcResultRef = client.query(jdbcOperationRef.asTJDBCOperationRef());
        log.info("client query costs: " + (System.currentTimeMillis() - startTime));
        JDBCResultRef jdbcResultRef = JDBCResultRef.parseFromTJDBCResultRef(tjdbcResultRef);
        close(tTransport);
        jdbcEngineExecutorRefManager.accessJDBCEngineRef(jdbcEngineExecutorRef);  // 更新最新的接入时间
        return jdbcResultRef;
    }
}