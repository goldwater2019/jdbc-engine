package com.ane56.engine.jdbc.config;

import com.ane56.engine.jdbc.exception.JDBCEngineException;
import com.ane56.engine.jdbc.utils.PropertiesUtils;
import lombok.Data;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class JDBCEngineConfig {

    // kyuubi.ha.zookeeper.quorum
    private String haZookeeperQuorum;
    private String haZookeeperDriverUriPath;
    private String haZookeeperExecutorUriPath;
    private int jdbcEngineDriverTimeout;

    private Map<String, String> configMap;
    private String configPath;

    private static volatile JDBCEngineConfig singleton;

    public JDBCEngineConfig(String configPath) throws JDBCEngineException {
        setConfigPath(configPath);
        setConfigMap(new LinkedHashMap<>());
        loadGetOrDefaultAsMap();
    }

    public static JDBCEngineConfig getInstance(String configPath) throws JDBCEngineException {
        if (singleton == null) {
            synchronized (JDBCEngineConfig.class) {
                if (singleton == null) {
                    singleton = new JDBCEngineConfig(configPath);
                }
            }
        }
        return singleton;
    }

    private void loadGetOrDefaultAsMap() throws JDBCEngineException {
        File configDir = new File(configPath);
        if (!configDir.exists()) {
            throw new JDBCEngineException(String.format("%s not exists, expect --conf-dir is a directory", configPath));
        }
        if (!configDir.isDirectory()) {
            throw new JDBCEngineException(String.format("%s not a directory, expect --conf-dir is a directory", configPath));
        }

        File[] files = configDir.listFiles();
        for (File file : files) {
            String absolutePath = file.getAbsolutePath();
            if (!absolutePath.endsWith("jdbc-engine-default.conf")) {
                continue;
            }
            Map<String, String> stringStringMap = PropertiesUtils.loadConfAsMap(absolutePath);
            for (Map.Entry<String, String> stringStringEntry : stringStringMap.entrySet()) {
                getConfigMap().put(stringStringEntry.getKey(), stringStringEntry.getValue());
            }
        }

        // haZookeeperQuorum
        setHaZookeeperQuorum(
                configMap.getOrDefault("jdbc.engine.ha.zookeeper.quorum", "10.10.106.102:2181")
        );
        // haZookeeperDriverUriPath
        setHaZookeeperDriverUriPath(
                configMap.getOrDefault("jdbc.engine.ha.zookeeper.driver.uri.path", "/engine/jdbc/driver/uri")
        );
        // haZookeeperExecutorUriPath
        setHaZookeeperExecutorUriPath(
                configMap.getOrDefault("jdbc.engine.ha.zookeeper.executor.uri.path", "")
        );
        // jdbcEngineDriverTimeout
        setJdbcEngineDriverTimeout(
                Integer.valueOf(configMap.getOrDefault("", "10000"))
        );
    }
}
