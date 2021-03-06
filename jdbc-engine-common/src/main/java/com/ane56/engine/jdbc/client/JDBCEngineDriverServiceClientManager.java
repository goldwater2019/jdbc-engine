package com.ane56.engine.jdbc.client;

import com.ane56.engine.jdbc.config.JDBCEngineConfig;
import com.ane56.engine.jdbc.exception.JDBCEngineException;
import com.ane56.engine.jdbc.model.JDBCEngineExecutorRef;
import com.ane56.engine.jdbc.model.thrift.JDBCEngineDriverServiceClientSuite;
import com.ane56.engine.jdbc.thrit.service.JDBCEngineDriverService;
import com.ane56.engine.jdbc.thrit.struct.TJDBCCatalog;
import com.ane56.engine.jdbc.utils.ZkUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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


@Data
@AllArgsConstructor
@Builder
public class    JDBCEngineDriverServiceClientManager {
    private static volatile JDBCEngineDriverServiceClientManager singleton;

    private int timeout;

    private String configDir;

    public JDBCEngineDriverServiceClientManager(String configDir) throws JDBCEngineException {
        setConfigDir(configDir);
        setTimeout(JDBCEngineConfig.getInstance(
                        getConfigDir()
                ).getJdbcEngineDriverTimeout()
        );
    }

    public static JDBCEngineDriverServiceClientManager getInstance(String configDir) throws JDBCEngineException {
        if (singleton == null) {
            synchronized (JDBCEngineDriverServiceClientManager.class) {
                if (singleton == null) {
                    singleton = new JDBCEngineDriverServiceClientManager(configDir);
                }
            }
        }
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
                .host("127.0.0.1")
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


    /**
     * 1. ??????ZkUtils ???????????????driver??????host???port
     * 2. ????????????driverUri
     * 3. ????????????client
     *
     * @return
     */
    public JDBCEngineDriverServiceClientSuite getAvailableClient() throws Exception {
        // 1. ???????????????driver?????????
        List<String> availableDriverUris = ZkUtils.getInstance(getConfigDir()).getAvailableDriverUris();
        // 2. ???????????????driver uri, e.g. 127.0.0.1:8080
        String driverUri = pickupOneUri(availableDriverUris);
        String[] split = driverUri.split(":");
        String driverHost = split[0];
        int driverPort = Integer.parseInt(split[1]);
        return innerGetAvailableClient(driverHost, driverPort);
    }

    /**
     * ?????????driveUri?????????????????????provider
     *
     * @param availableDriverUris
     * @return
     * @throws JDBCEngineException
     */
    private String pickupOneUri(List<String> availableDriverUris) throws JDBCEngineException {
        if (availableDriverUris == null || availableDriverUris.isEmpty()) {
            throw new JDBCEngineException("no driver uri avaiable");
        }
        Random random = new Random();
        int index = Math.abs(random.nextInt()) % availableDriverUris.size();
        return availableDriverUris.get(index);
    }


    /**
     * @param driverHost
     * @param driverPort
     * @return
     * @throws InterruptedException
     */
    public JDBCEngineDriverServiceClientSuite innerGetAvailableClient(String driverHost, int driverPort) throws InterruptedException {
        //???????????????????????????????????????????????????????????????Java??????NIO???????????????close????????????
        try {
            TTransport transport = new TFramedTransport(new TSocket(driverHost, driverPort, timeout));
            //?????????????????????????????????????????????????????????????????????
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
}