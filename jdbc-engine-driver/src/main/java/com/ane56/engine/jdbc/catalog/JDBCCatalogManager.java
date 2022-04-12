package com.ane56.engine.jdbc.catalog;

import com.ane56.engine.jdbc.exception.JDBCEngineException;
import com.ane56.engine.jdbc.model.JDBCCatalog;
import com.ane56.engine.jdbc.thrit.struct.TJDBCCatalog;
import com.ane56.engine.jdbc.utils.PropertiesUtils;
import com.ane56.engine.jdbc.utils.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Slf4j
public class JDBCCatalogManager {

    private static volatile JDBCCatalogManager singleton;
    private Map<String, JDBCCatalog> name2jdbcCatalogs = new ConcurrentHashMap<>();
    private String jDBCEngineDriverServiceConfigPath;


    private JDBCCatalogManager() {
    }

    private JDBCCatalogManager(String jDBCEngineDriverServiceConfigPath) {
        setJDBCEngineDriverServiceConfigPath(jDBCEngineDriverServiceConfigPath);
    }

    public static JDBCCatalogManager getInstance(String jDBCEngineDriverServiceConfigPath) {
        if (singleton == null) {
            synchronized (JDBCCatalogManager.class) {
                if (singleton == null) {
                    singleton = new JDBCCatalogManager(jDBCEngineDriverServiceConfigPath);
                }
            }
        }
        return singleton;
    }

    /**
     * 添加相应的catalog
     *
     * @param jdbcCatalog
     * @return
     */
    public boolean upsertJDBCCatalog(JDBCCatalog jdbcCatalog) {
        JDBCCatalog catalog = name2jdbcCatalogs.getOrDefault(jdbcCatalog.getName(), null);
        name2jdbcCatalogs.put(jdbcCatalog.getName(), jdbcCatalog);
        if (catalog == null) {
            return true;
        }
        return false;
    }

    /**
     * 将数据直接转换成thrift格式
     *
     * @return
     */
    public List<TJDBCCatalog> getCatalogs() {
        List<TJDBCCatalog> tJdbcCatalogList = new LinkedList<>();
        for (Map.Entry<String, JDBCCatalog> uuidJdbcCatalogEntry : name2jdbcCatalogs.entrySet()) {
            tJdbcCatalogList.add(uuidJdbcCatalogEntry.getValue().asTJDBCCatalog());
        }
        return tJdbcCatalogList;
    }

    /**
     * 加载默认的配置
     */
    public void loadOrDefaultCatalogs() throws JDBCEngineException {
        if (jDBCEngineDriverServiceConfigPath == null) {
            // no-ops
            log.error("There is no specific config path, no ops for load or default catalogs");
            throw new JDBCEngineException("There is no specific config path, no ops for load or default catalogs");
        }

        File file = new File(jDBCEngineDriverServiceConfigPath);
        if (!file.isDirectory()) {
            throw new JDBCEngineException(String.format("%s not a directory, expect --conf-dir is a directory", jDBCEngineDriverServiceConfigPath));
        }
        if (!file.exists()) {
            throw new JDBCEngineException(String.format("%s not exists, expect --conf-dir is a directory", jDBCEngineDriverServiceConfigPath));
        }
        File[] files = file.listFiles();
        for (File configFile : files) {
            String absolutePath = configFile.getAbsolutePath();
            String name = configFile.getName();
            if (!name.endsWith("catalog.properties")) {
                continue;
            }
            Map<String, String> stringStringMap = null;
            try {
                stringStringMap = PropertiesUtils.loadAsMap(absolutePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            JDBCCatalog jdbcCatalog = JDBCCatalog.builder()
                    .driverClass(stringStringMap.getOrDefault("catalog.jdbc.driver.classname", ""))
                    .name(stringStringMap.getOrDefault("catalog.name", ""))
                    .password(stringStringMap.getOrDefault("catalog.jdbc.password", ""))
                    .uri(stringStringMap.getOrDefault("catalog.jdbc.url", ""))
                    .username(stringStringMap.getOrDefault("catalog.jdbc.username", ""))
                    .build();

            if (StringUtils.isBlank(jdbcCatalog.getDriverClass())) {
                continue;
            }
            if (StringUtils.isBlank(jdbcCatalog.getName())) {
                continue;
            }
            if (StringUtils.isBlank(jdbcCatalog.getPassword())) {
                continue;
            }
            if (StringUtils.isBlank(jdbcCatalog.getUri())) {
                continue;
            }
            if (StringUtils.isBlank(jdbcCatalog.getUsername())) {
                continue;
            }
            upsertJDBCCatalog(jdbcCatalog);
        }
        log.info("loadOrDefaultCatalogs successfully");
    }
}
