package com.ane56.engine.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ane56.engine.jdbc.model.JsonResult;
import com.ane56.engine.jdbc.client.JDBCEngineDriverServiceClientManager;
import com.ane56.engine.jdbc.config.JDBCEngineConfig;
import com.ane56.engine.jdbc.exception.JDBCEngineException;
import com.ane56.engine.jdbc.model.JDBCCatalog;
import com.ane56.engine.jdbc.model.JDBCResultRef;
import com.ane56.engine.jdbc.model.thrift.JDBCEngineDriverServiceClientSuite;
import com.ane56.engine.jdbc.thrit.struct.TJDBCCatalog;
import com.ane56.engine.jdbc.thrit.struct.TJDBCResultRef;
import com.ane56.engine.jdbc.utils.PathUtils;
import com.ane56.engine.service.JDBCEngineDriverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class JDBCEngineDriverServiceImpl implements JDBCEngineDriverService {

    @Value("${engine.jdbc.config-dir}")
    private String configDir;

    private JDBCEngineConfig jdbcEngineConfig;

    private JDBCEngineDriverServiceClientManager jdbcEngineDriverServiceClientManager;

    @Override
    public JsonResult<String> getHaZookeeperQuorum() {
        try {
            checkInitialStatus();
        } catch (JDBCEngineException e) {
            e.printStackTrace();
            return new JsonResult<String>(-1, e.getMessage());
        }
        String haZookeeperQuorum = jdbcEngineConfig.getHaZookeeperQuorum();
        log.info("ha zookeeper quorum: " + haZookeeperQuorum);
        return new JsonResult<String>(haZookeeperQuorum);
    }

    @Override
    public JsonResult<List<JDBCCatalog>> listCatalogs() {
        try {
            checkInitialStatus();
        } catch (JDBCEngineException e) {
            e.printStackTrace();
            return new JsonResult<List<JDBCCatalog>>(-1, e.getMessage());
        }
        try {
            JDBCEngineDriverServiceClientSuite availableClient = jdbcEngineDriverServiceClientManager.getAvailableClient();
            com.ane56.engine.jdbc.thrit.service.JDBCEngineDriverService.Client client = availableClient.getClient();
            List<TJDBCCatalog> catalogs = client.getCatalogs();
            List<JDBCCatalog> jdbcCatalogs = new LinkedList<>();
            for (TJDBCCatalog catalog : catalogs) {
                jdbcCatalogs.add(JDBCCatalog.parseFromTJDBCCatalog(catalog));
            }
            jdbcEngineDriverServiceClientManager.close(availableClient.getTTransport());
            return new JsonResult<>(jdbcCatalogs);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult<>(-1, e.getMessage());
        }
    }

    @Override
    public JsonResult<JDBCResultRef> query(String querySql) {
        try {
            checkInitialStatus();
        } catch (JDBCEngineException e) {
            e.printStackTrace();
            return new JsonResult<>(-1, e.getMessage());
        }
        try {
            JDBCEngineDriverServiceClientSuite availableClient = jdbcEngineDriverServiceClientManager.getAvailableClient();
            com.ane56.engine.jdbc.thrit.service.JDBCEngineDriverService.Client client = availableClient.getClient();

            TJDBCResultRef tjdbcResultRef = client.query(querySql);
            jdbcEngineDriverServiceClientManager.close(availableClient.getTTransport());
            log.info(JSONObject.toJSONString(JDBCResultRef.parseFromTJDBCResultRef(tjdbcResultRef)));
            return new JsonResult<>(JDBCResultRef.parseFromTJDBCResultRef(tjdbcResultRef));
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult<>(-1, e.getMessage());
        }
    }


    private void checkInitialStatus() throws JDBCEngineException {
        checkConfigDir();
        if (jdbcEngineConfig == null) {
            jdbcEngineConfig = JDBCEngineConfig.getInstance(configDir);
        }

        if (jdbcEngineDriverServiceClientManager == null) {
            jdbcEngineDriverServiceClientManager = JDBCEngineDriverServiceClientManager.getInstance(configDir);
        }
    }

    private void checkConfigDir() {
        if (configDir == null || configDir.length() == 0) {
            String jdbcEngineHome = System.getenv("JDBC_ENGINE_HOME");
            if (jdbcEngineHome != null) {
                configDir = PathUtils.checkAndCombinePath(jdbcEngineHome, "/conf");
            }
        }
    }
}
