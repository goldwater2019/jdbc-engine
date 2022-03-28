package com.ane56.engine.jdbc.catalog;

import com.ane56.engine.jdbc.thrift.struct.TJDBCCatalog;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JDBCCatalogManager {

    private static volatile JDBCCatalogManager singleton;
    private JDBCCatalogManager() {};
    public static JDBCCatalogManager getInstance() {
        if (singleton == null) {
            synchronized (JDBCCatalogManager.class) {
                if (singleton == null) {
                    singleton = new JDBCCatalogManager();
                }
            }
        }
        return singleton;
    }

    private Map<String, JDBCCatalog> name2jdbcCatalogs = new ConcurrentHashMap<>();

    /**
     * 添加相应的catalog
     * @param jdbcCatalog
     * @return
     */
    public boolean addJDBCCatalog(JDBCCatalog jdbcCatalog) {
        JDBCCatalog catalog = name2jdbcCatalogs.getOrDefault(jdbcCatalog.getName(), null);
        if (catalog == null) {
            name2jdbcCatalogs.put(jdbcCatalog.getName(), jdbcCatalog);
        } else {
            return false;
        }
        return true;
    }

    /**
     * 将数据直接转换成thrift格式
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
    public void loadOrDefaultCatalogs() {
        JDBCCatalog jdbcCatalog = JDBCCatalog.builder()
                // TODO driver的修改
                .driverClass("com")
                .name("starrocks")
                .password("anetx")
                .uri("jdbc:mysql://10.10.230.2:9030/tx_dev")
                .username("anetx")
                .build();

        addJDBCCatalog(jdbcCatalog);
    }
}
