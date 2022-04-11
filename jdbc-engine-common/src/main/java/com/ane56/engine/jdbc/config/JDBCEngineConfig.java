package com.ane56.engine.jdbc.config;

import lombok.Data;

import java.io.File;
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

    public JDBCEngineConfig(String configPath) {
        setConfigPath(configPath);
        loadGetOrDefaultAsMap();
    }

    public static JDBCEngineConfig getInstance(String configPath) {
        if (singleton == null) {
            synchronized (JDBCEngineConfig.class) {
                if (singleton == null) {
                    singleton = new JDBCEngineConfig(configPath);
                }
            }
        }
        return singleton;
    }

    private void loadGetOrDefaultAsMap() {
        // TODO 根据configPath读取相应的jdbc-engine-default.conf中的内容
        File configDir = new File(configPath);
        if (!configDir.exists()) {
            // TODO 抛出异常
        }
        if (!configDir.isDirectory()) {
            // TODO 抛出异常
        }

        File[] files = configDir.listFiles();
        for (File file : files) {
            String absolutePath = file.getAbsolutePath();
            if (!absolutePath.endsWith("jdbc-engine-default.conf")) {
                continue;
            }
            // TODO 读取文件中的数据
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
