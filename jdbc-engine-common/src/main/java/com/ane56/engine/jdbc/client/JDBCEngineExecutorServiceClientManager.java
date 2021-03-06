package com.ane56.engine.jdbc.client;

import com.ane56.engine.jdbc.config.JDBCEngineConfig;
import com.ane56.engine.jdbc.enumeration.JDBCQueryStatus;
import com.ane56.engine.jdbc.exception.JDBCEngineException;
import com.ane56.engine.jdbc.model.JDBCOperationRef;
import com.ane56.engine.jdbc.model.JDBCResultRef;
import com.ane56.engine.jdbc.model.thrift.JDBCEngineExecutorServiceClientSuite;
import com.ane56.engine.jdbc.thrit.service.JDBCEngineExecutorService;
import com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef;
import com.ane56.engine.jdbc.utils.ZkUtils;
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

import java.util.List;
import java.util.Random;
import java.util.UUID;


@Data
@AllArgsConstructor
@Builder
@Slf4j
public class JDBCEngineExecutorServiceClientManager {

    private static volatile JDBCEngineExecutorServiceClientManager singleton;
    private int timeout;
    private JDBCEngineExecutorRefManager jdbcEngineExecutorRefManager;
    private String configDir;

    public JDBCEngineExecutorServiceClientManager(String configDir) throws JDBCEngineException {
        setConfigDir(configDir);
        if (jdbcEngineExecutorRefManager == null) {
            jdbcEngineExecutorRefManager = JDBCEngineExecutorRefManager.getInstance();
        }
        setTimeout(JDBCEngineConfig.getInstance(
                        getConfigDir()
                ).getJdbcEngineExecutorTimeout()
        );
    }

    public static JDBCEngineExecutorServiceClientManager getInstance(String configDir) throws JDBCEngineException {
        if (singleton == null) {
            synchronized (JDBCEngineExecutorServiceClientManager.class) {
                if (singleton == null) {
                    singleton = new JDBCEngineExecutorServiceClientManager(configDir);
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
     * ?????????????????????uri?????????????????????executorUri
     *
     * @param availableExecutorUris
     * @return
     * @throws JDBCEngineException
     */
    public String pickupOneUri(List<String> availableExecutorUris) throws JDBCEngineException {
        if (availableExecutorUris == null || availableExecutorUris.isEmpty()) {
            throw new JDBCEngineException("no executor uri available");
        }
        Random random = new Random();
        int index = Math.abs(random.nextInt()) % availableExecutorUris.size();
        return availableExecutorUris.get(index);
    }

    public JDBCEngineExecutorServiceClientSuite innerGetAvailableClient(String executorHost, int executorPort) {
        //???????????????????????????????????????????????????????????????Java??????NIO???????????????close????????????
        try {
            TTransport transport = new TFramedTransport(new TSocket(executorHost, executorPort, getTimeout()));
            //?????????????????????????????????????????????????????????????????????
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
     * ??????ZkUtils??????????????????executorUri
     * ?????????????????????client?????????
     *
     * @return
     * @throws InterruptedException
     */
    public JDBCEngineExecutorServiceClientSuite getAvailableClient() throws Exception {

        // 1. ???????????????executor?????????
        List<String> availableExecutorUris = ZkUtils.getInstance(getConfigDir()).getAvailableExecutorUris();
        // 2. ???????????????driver uri, e.g. 127.0.0.1:8080
        String executorUri = pickupOneUri(availableExecutorUris);
        String[] split = executorUri.split(":");
        String executorHost = split[0];
        int executorPort = Integer.parseInt(split[1]);
        return innerGetAvailableClient(executorHost, executorPort);
    }


    /**
     * @param catalogName : ????????????catalogName
     * @param querySQL    : ?????????SQL
     * @return
     * @throws InterruptedException
     * @throws TException
     */
    public JDBCResultRef query(String catalogName, String querySQL) throws Exception {
        long startTime = System.currentTimeMillis();
        JDBCEngineExecutorServiceClientSuite availableClient = getAvailableClient();
        log.info("get client costs: " + (System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
        JDBCEngineExecutorService.Client client = availableClient.getClient();
        TTransport tTransport = availableClient.getTTransport();
        // jdbcEngineExecutorRefManager.accessJDBCEngineRef(jdbcEngineExecutorRef);  // ???????????????????????????

        // ???????????????oepration?????????
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
        // jdbcEngineExecutorRefManager.accessJDBCEngineRef(jdbcEngineExecutorRef);  // ???????????????????????????
        return jdbcResultRef;
    }
}
