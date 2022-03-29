package com.ane56.engine.jdbc.driver.impl;

import com.ane56.engine.jdbc.catalog.JDBCCatalogManager;
import com.ane56.engine.jdbc.exetuor.JDBCEngineExecutorManager;
import com.ane56.engine.jdbc.model.JDBCCatalog;
import com.ane56.engine.jdbc.model.JDBCEngineExecutorRef;
import com.ane56.engine.jdbc.thrit.service.JDBCEngineDriverService;
import com.ane56.engine.jdbc.thrit.struct.TJDBCCatalog;
import com.ane56.engine.jdbc.thrit.struct.TJDBCEngineExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

import java.util.List;
import java.util.UUID;

@Slf4j
public class JDBCEngineDriverServiceImpl implements JDBCEngineDriverService.Iface {
    private JDBCCatalogManager jdbcCatalogManager;
    private JDBCEngineExecutorManager jdbcEngineExecutorManager;

    @Override
    public boolean heartBeat(TJDBCEngineExecutor jdbcEngineExecutor) throws TException {
        long startTime = System.currentTimeMillis();
        checkInitialStatus();
        boolean result = jdbcEngineExecutorManager.heartbeat(jdbcEngineExecutor);
        log.info("heartBeat : " + (System.currentTimeMillis() - startTime) + " ms, body: " + jdbcEngineExecutorManager.getUuid2jdbcEngineExecutorRefs().get(
                UUID.fromString(jdbcEngineExecutor.getExecutorRefId())
        ));
        return result;
    }

    /**
     * 获得相应的catalogs
     * @return
     * @throws TException
     */
    @Override
    public List<TJDBCCatalog> getCatalogs() throws TException {
        long startTime = System.currentTimeMillis();
        checkInitialStatus();
        List<TJDBCCatalog> result = jdbcCatalogManager.getCatalogs();
        log.info("getCatalogs : " + (System.currentTimeMillis() - startTime) + " ms");
        return result;
    }

    @Override
    public boolean addCatalog(TJDBCCatalog jdbcCatalog) throws TException {
        return jdbcCatalogManager.upsertJDBCCatalog(JDBCCatalog.parseFromTJDBCCatalog(jdbcCatalog));
    }

    public JDBCEngineDriverServiceImpl() {
        checkInitialStatus();
    }

    public void checkInitialStatus() {
        if (jdbcCatalogManager == null) {
            jdbcCatalogManager = JDBCCatalogManager.getInstance();
            jdbcCatalogManager.loadOrDefaultCatalogs();
        }
        if (jdbcEngineExecutorManager == null) {
            jdbcEngineExecutorManager = JDBCEngineExecutorManager.getInstance();
        }
    }
}
