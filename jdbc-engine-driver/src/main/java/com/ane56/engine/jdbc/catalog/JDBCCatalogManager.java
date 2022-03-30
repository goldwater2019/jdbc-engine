package com.ane56.engine.jdbc.catalog;

import com.ane56.engine.jdbc.model.JDBCCatalog;
import com.ane56.engine.jdbc.thrit.struct.TJDBCCatalog;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JDBCCatalogManager {

    private static volatile JDBCCatalogManager singleton;
    private Map<String, JDBCCatalog> name2jdbcCatalogs = new ConcurrentHashMap<>();

    ;

    private JDBCCatalogManager() {
    }

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
    public void loadOrDefaultCatalogs() {
        JDBCCatalog jdbcCatalog = JDBCCatalog.builder()
                .driverClass("com.mysql.cj.jdbc.Driver")
                .name("starrocks")
                .password("anetx")
                .uri("jdbc:mysql://10.10.230.2:9030/tx_dev")
                .username("anetx")
                .build();

        upsertJDBCCatalog(jdbcCatalog);
    }
}
