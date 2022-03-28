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

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class JDBCEngineDriverServiceClient {
    String address = "localhost";
    int port = 7911;
    int timeout = 100*1000;

    public void start() throws TTransportException {
        //使用非阻塞方式，按块的大小进行传输，类似于Java中的NIO。记得调用close释放资源
        TTransport transport =
                new TFramedTransport(new TSocket(address, port, timeout));
        //高效率的、密集的二进制编码格式进行数据传输协议
        TProtocol protocol = new TCompactProtocol(transport);
        JDBCEngineDriverService.Client client = new JDBCEngineDriverService.Client(protocol);
        try {
            open(transport);
            for (int i = 0; i < 1000; i++) {
                testGetCatalogs(client);
            }
            for (int i= 0; i < 1000; i++) {
                testHeartbeat(client);
            }
            close(transport);
        } catch (TException e) {
            e.printStackTrace();
        }
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

    private void open(TTransport transport)
    {
        if(transport != null && !transport.isOpen())
        {
            try {
                transport.open();
            } catch (TTransportException e) {
                e.printStackTrace();
            }
        }
    }

    private void close(TTransport transport)
    {
        if(transport != null && transport.isOpen())
        {
            transport.close();
        }
    }

    public static void main(String[] args) throws TTransportException {
        JDBCEngineDriverServiceClient jdbcEngineDriverServiceClient = new JDBCEngineDriverServiceClient();
        jdbcEngineDriverServiceClient.start();
    }
}
