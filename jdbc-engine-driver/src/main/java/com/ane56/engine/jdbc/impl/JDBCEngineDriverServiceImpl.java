package com.ane56.engine.jdbc.impl;

import com.ane56.engine.jdbc.catalog.JDBCCatalogManager;
import com.ane56.engine.jdbc.exetuor.JDBCEngineExecutorManager;
import com.ane56.engine.jdbc.thrift.service.JDBCEngineDriverService;
import com.ane56.engine.jdbc.thrift.struct.TJDBCCatalog;
import com.ane56.engine.jdbc.thrift.struct.TJDBCEngineExecutor;
import org.apache.thrift.TException;

import java.util.List;

public class JDBCEngineDriverServiceImpl implements JDBCEngineDriverService.Iface {
    private JDBCCatalogManager jdbcCatalogManager;
    private JDBCEngineExecutorManager jdbcEngineExecutorManager;

    @Override
    public boolean heartBeat(TJDBCEngineExecutor jdbcEngineExecutor) throws TException {
        checkInitialStatus();
        return jdbcEngineExecutorManager.heartbeat(jdbcEngineExecutor);
    }

    /**
     * 获得相应的catalogs
     * @return
     * @throws TException
     */
    @Override
    public List<TJDBCCatalog> getCatalogs() throws TException {
        checkInitialStatus();
        return jdbcCatalogManager.getCatalogs();
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
