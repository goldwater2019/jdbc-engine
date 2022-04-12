package com.ane56.engine.jdbc.driver.impl;

import com.ane56.engine.jdbc.catalog.JDBCCatalogManager;
import com.ane56.engine.jdbc.client.JDBCEngineExecutorRefManager;
import com.ane56.engine.jdbc.client.JDBCEngineExecutorServiceClientManager;
import com.ane56.engine.jdbc.enumeration.JDBCQueryStatus;
import com.ane56.engine.jdbc.exception.JDBCEngineException;
import com.ane56.engine.jdbc.model.JDBCCatalog;
import com.ane56.engine.jdbc.model.JDBCOperationRef;
import com.ane56.engine.jdbc.model.JDBCResultRef;
import com.ane56.engine.jdbc.model.JDBCResultSet;
import com.ane56.engine.jdbc.thrit.service.JDBCEngineDriverService;
import com.ane56.engine.jdbc.thrit.struct.TJDBCCatalog;
import com.ane56.engine.jdbc.thrit.struct.TJDBCEngineExecutor;
import com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

import java.util.*;

@Data
@Slf4j
public class JDBCEngineDriverServiceImpl implements JDBCEngineDriverService.Iface {
    private JDBCCatalogManager jdbcCatalogManager;
    private JDBCEngineExecutorRefManager jdbcEngineExecutorRefManager;
    private JDBCEngineExecutorServiceClientManager jdbcEngineExecutorServiceClientManager;
    private String jDBCEngineDriverServiceConfigPath;

    public JDBCEngineDriverServiceImpl(String jDBCEngineDriverServiceConfigPath) {
        setJDBCEngineDriverServiceConfigPath(jDBCEngineDriverServiceConfigPath);
        try {
            checkInitialStatus();
        } catch (JDBCEngineException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean heartBeat(TJDBCEngineExecutor jdbcEngineExecutor) throws TException {
        long startTime = System.currentTimeMillis();
        try {
            checkInitialStatus();
        } catch (JDBCEngineException e) {
            e.printStackTrace();
        }
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
        try {
            checkInitialStatus();
        } catch (JDBCEngineException e) {
            e.printStackTrace();
        }
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
        try {
            checkInitialStatus();
        } catch (JDBCEngineException e) {
            e.printStackTrace();
        }

        JDBCResultRef jdbcResultRef = null;
        try {
            // TODO parse catalog and convert the original sql as real sql
            Map<String, String> stringStringMap = convertSQL(querySQL);
            if (stringStringMap.size() == 0) {
                // TODO return error msg directly

                JDBCResultSet jdbcResultSet = JDBCResultSet.builder()
                        .resultRowList(new LinkedList<>())
                        .build();

                JDBCOperationRef jdbcOperationRef = JDBCOperationRef.builder()
                        .queryStatus(JDBCQueryStatus.FAILED)
                        .message("no catalog specified,expected a catalog name before dbname, query sql: " + querySQL)
                        .sqlStatement(querySQL)
                        .startTime(System.currentTimeMillis())
                        .endTime(System.currentTimeMillis())
                        .operationRefId(UUID.randomUUID())
                        .build();

                jdbcResultRef = JDBCResultRef.builder()
                        .jdbcOperationRef(jdbcOperationRef)
                        .jdbcResultSet(jdbcResultSet)
                        .build();
            } else {
                String catalog = null;
                String sql = null;
                for (Map.Entry<String, String> stringStringEntry : stringStringMap.entrySet()) {
                    catalog = stringStringEntry.getKey();
                    sql = stringStringEntry.getValue();
                }
                jdbcResultRef = jdbcEngineExecutorServiceClientManager.query(catalog, sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        log.info(jdbcResultRef.toString());
        log.info("duration: " + (jdbcResultRef.getJdbcOperationRef().getEndTime() - jdbcResultRef.getJdbcOperationRef().getStartTime()));
        return jdbcResultRef.asTJDBCResultRef();
    }

    public void checkInitialStatus() throws JDBCEngineException {
        if (jdbcCatalogManager == null) {
            jdbcCatalogManager = JDBCCatalogManager.getInstance(jDBCEngineDriverServiceConfigPath);
            jdbcCatalogManager.loadOrDefaultCatalogs();
        }
        if (jdbcEngineExecutorRefManager == null) {
            jdbcEngineExecutorRefManager = JDBCEngineExecutorRefManager.getInstance();
        }
        if (jdbcEngineExecutorServiceClientManager == null) {
            jdbcEngineExecutorServiceClientManager = JDBCEngineExecutorServiceClientManager.getInstance(jDBCEngineDriverServiceConfigPath);
        }
    }

    /**
     * convert original sql into real sql, e.g.
     *      input:
     *          select * from aliyun.engine.t_click_logs limit 100, 2
     *      output:
     *          (aliyun, select * from engine.t_click_logs limit 100, 2)
     * @param originalSQL
     * @return
     */
    private Map<String, String> convertSQL(String originalSQL) {
        Map<String, String> result = new HashMap<>();
        boolean isKicked = false;
        Map<String, JDBCCatalog> name2jdbcCatalogs = jdbcCatalogManager.getName2jdbcCatalogs();
        for (String catalogName : name2jdbcCatalogs.keySet()) {
            int index = originalSQL.indexOf(catalogName);
            if (index > -1) {
                isKicked = true;
                result.put(catalogName, originalSQL.replace(catalogName +".", ""));
                break;
            }
        }
        return result;
    }

}
