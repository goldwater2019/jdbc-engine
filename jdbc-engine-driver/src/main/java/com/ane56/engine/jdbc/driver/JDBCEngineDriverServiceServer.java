package com.ane56.engine.jdbc.driver;


import com.ane56.engine.jdbc.config.JDBCEngineConfig;
import com.ane56.engine.jdbc.driver.impl.JDBCEngineDriverServiceImpl;
import com.ane56.engine.jdbc.exception.JDBCEngineException;
import com.ane56.engine.jdbc.thrit.service.JDBCEngineDriverService;
import com.ane56.engine.jdbc.utils.LogUtils;
import com.ane56.engine.jdbc.utils.NetUtils;
import com.ane56.engine.jdbc.utils.PathUtils;
import com.ane56.engine.jdbc.utils.ZkUtils;
import org.apache.log4j.PropertyConfigurator;
import org.apache.thrift.TProcessor;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.layered.TFramedTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class JDBCEngineDriverServiceServer {
    private static volatile JDBCEngineDriverServiceServer singleton;
    private static int servicePort = 8888;
    private static Map<String, String> configMap = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(JDBCEngineDriverServiceServer.class);

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

        // ??????configDir/log4j.properties
        LogUtils.getInstance().loadLog4jProperties(configDir);

        String driverHost = NetUtils.getInetHostAddress();
        // TODO ??????????????????????????????, ????????????+1????????????????????????
        int nextInt = new Random().nextInt();
        servicePort = Math.abs(nextInt % 5000 + 5000);
        String uri = String.join(":", driverHost, String.valueOf(servicePort));
        logger.info("registry driver uri: " + uri);
        ZkUtils zkUtils = ZkUtils.getInstance(configDir);
        zkUtils.createEphemeralNode(
                PathUtils.checkAndCombinePath(JDBCEngineConfig.getInstance(configDir).getHaZookeeperDriverUriPath(), driverHost + ":" +
                        servicePort)
        );
        JDBCEngineDriverServiceServer jdbcEngineDriverServiceServer = JDBCEngineDriverServiceServer.getInstance();
        jdbcEngineDriverServiceServer.invoke();
        zkUtils.changeRunningStatus(false);
    }

    public void invoke() throws TTransportException {
        String configPath = configMap.get("jdbc.engine.driver.config.path"); // , "C:/workspace/jdbc-engine/conf");
        // ????????????????????????TFramedTransport??????
        TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(servicePort);
        // ??????????????????Service???????????????
        TProcessor processor = new JDBCEngineDriverService.Processor<JDBCEngineDriverService.Iface>(new JDBCEngineDriverServiceImpl(configPath));
        // ??????Thrift????????????????????????????????????????????????????????????
        TThreadedSelectorServer.Args args = new TThreadedSelectorServer.Args(serverTransport);
        args.processor(processor);
        // ??????????????????????????????????????????????????????????????????????????????????????????
        args.protocolFactory(new TCompactProtocol.Factory());
        // ????????????????????????????????????????????????????????????????????????????????????Java??????NIO
        args.transportFactory(new TFramedTransport.Factory());
        // ?????????????????????,???????????????????????????
        args.processorFactory(new TProcessorFactory(processor));
        // ???????????????????????????????????????IO??????
        args.selectorThreads(8);
        // ???????????????
        ExecutorService pool = Executors.newFixedThreadPool(64);
        args.executorService(pool);
        TThreadedSelectorServer server = new TThreadedSelectorServer(args);
        logger.info("Starting server on port " + servicePort + "......");
        server.serve();
    }
}
