package com.ane56.engine.jdbc.driver.impl;

import com.ane56.engine.jdbc.catalog.JDBCCatalogManager;
import com.ane56.engine.jdbc.client.JDBCEngineExecutorRefManager;
import com.ane56.engine.jdbc.client.JDBCEngineExecutorServiceClientManager;
import com.ane56.engine.jdbc.model.JDBCCatalog;
import com.ane56.engine.jdbc.model.JDBCEngineExecutorRef;
import com.ane56.engine.jdbc.model.JDBCResultRef;
import com.ane56.engine.jdbc.thrit.service.JDBCEngineDriverService;
import com.ane56.engine.jdbc.thrit.struct.TJDBCCatalog;
import com.ane56.engine.jdbc.thrit.struct.TJDBCEngineExecutor;
import com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

import java.util.List;

@Slf4j
public class JDBCEngineDriverServiceImpl implements JDBCEngineDriverService.Iface {
    private JDBCCatalogManager jdbcCatalogManager;
    private JDBCEngineExecutorRefManager jdbcEngineExecutorRefManager;
    private JDBCEngineExecutorServiceClientManager jdbcEngineExecutorServiceClientManager;

    public JDBCEngineDriverServiceImpl() {
        checkInitialStatus();
    }

    @Override
    public boolean heartBeat(TJDBCEngineExecutor jdbcEngineExecutor) throws TException {
        long startTime = System.currentTimeMillis();
        checkInitialStatus();
        boolean result = jdbcEngineExecutorRefManager.heartbeat(jdbcEngineExecutor);
//        log.info("heartBeat : " + (System.currentTimeMillis() - startTime) + " ms, body: " + jdbcEngineExecutorRefManager.getUuid2jdbcEngineExecutorRefs().get(
//                UUID.fromString(jdbcEngineExecutor.getExecutorRefId())
//        ));
        return result;
    }

    /**
     * 获得相应的catalogs
     *
     * @return
     * @throws TException
     */
    @Override
    public List<TJDBCCatalog> getCatalogs() throws TException {
        long startTime = System.currentTimeMillis();
        checkInitialStatus();
        List<TJDBCCatalog> result = jdbcCatalogManager.getCatalogs();
//        log.info("getCatalogs : " + (System.currentTimeMillis() - startTime) + " ms");
        return result;
    }

    @Override
    public boolean addCatalog(TJDBCCatalog jdbcCatalog) throws TException {
        return jdbcCatalogManager.upsertJDBCCatalog(JDBCCatalog.parseFromTJDBCCatalog(jdbcCatalog));
    }

    /**
     * @param querySQL
     * @return
     * @throws TException
     */
    @Override
    public TJDBCResultRef query(String querySQL) throws TException {
        checkInitialStatus();

        JDBCResultRef jdbcResultRef = null;
        try {
            jdbcResultRef = jdbcEngineExecutorServiceClientManager.query("starrocks", querySQL);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        log.info(jdbcResultRef.toString());
        log.info("duration: " + (jdbcResultRef.getJdbcOperationRef().getEndTime() - jdbcResultRef.getJdbcOperationRef().getStartTime()));
        return jdbcResultRef.asTJDBCResultRef();
    }

    public void checkInitialStatus() {
        if (jdbcCatalogManager == null) {
            jdbcCatalogManager = JDBCCatalogManager.getInstance();
            jdbcCatalogManager.loadOrDefaultCatalogs();
        }
        if (jdbcEngineExecutorRefManager == null) {
            jdbcEngineExecutorRefManager = JDBCEngineExecutorRefManager.getInstance();
        }
        if (jdbcEngineExecutorServiceClientManager == null) {
            jdbcEngineExecutorServiceClientManager = JDBCEngineExecutorServiceClientManager.getInstance();
        }
    }
}
