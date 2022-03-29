package com.ane56.engine.jdbc.driver;


import com.ane56.engine.jdbc.driver.impl.JDBCEngineDriverServiceImpl;
import com.ane56.engine.jdbc.thrit.service.JDBCEngineDriverService;
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
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.layered.TFramedTransport;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class JDBCEngineDriverServiceServer {
    private int servicePort = 8888;

    private static volatile JDBCEngineDriverServiceServer singleton;

    public static JDBCEngineDriverServiceServer getInstance() {
        if (singleton == null) {
            synchronized (JDBCEngineDriverServiceServer.class) {
                if (singleton == null) {
                    singleton = new JDBCEngineDriverServiceServer();
                }
            }
        }
        return singleton;
    }

    public void invoke() throws TTransportException {
        // 非阻塞式的，配合TFramedTransport使用
        TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(servicePort);
        // 关联处理器与Service服务的实现
        TProcessor processor = new JDBCEngineDriverService.Processor<JDBCEngineDriverService.Iface>(new JDBCEngineDriverServiceImpl());
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
        args.selectorThreads(8);
        // 工作线程池
        ExecutorService pool = Executors.newFixedThreadPool(64);
        args.executorService(pool);
        TThreadedSelectorServer server = new TThreadedSelectorServer(args);
        log.info("Starting server on port " + servicePort + "......");
        server.serve();
    }

    public static void main(String[] args) throws TTransportException {
        JDBCEngineDriverServiceServer jdbcEngineDriverServiceServer = JDBCEngineDriverServiceServer.getInstance();
        jdbcEngineDriverServiceServer.invoke();
    }
}
