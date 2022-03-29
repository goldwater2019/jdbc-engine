package com.ane56.engine.jdbc.executor.pool.connection;

import com.alibaba.druid.pool.DruidDataSource;
import com.ane56.engine.jdbc.model.JDBCCatalog;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhangxinsen
 * @Date: 2022/3/29 11:20 PM
 * @Desc:
 * @Version: v1.0
 */

public class PooledDataSourceManager {

    /**
     * 单例方法
     */
    private static volatile PooledDataSourceManager singleton;

    public static PooledDataSourceManager getInstance() {
        if (singleton == null) {
            synchronized (PooledDataSourceManager.class) {
                if (singleton == null) {
                    singleton = new PooledDataSourceManager();
                }
            }
        }
        return singleton;
    }

    private PooledDataSourceManager() {
    }

    ;

    private Map<String, JDBCCatalog> name2catalog = new HashMap<>();
    private Map<String, DruidDataSource> name2source = new HashMap<>();

    public Map<String, JDBCCatalog> getName2catalog() {
        return name2catalog;
    }

    public void setName2catalog(Map<String, JDBCCatalog> name2catalog) {
        this.name2catalog = name2catalog;
    }

    public Map<String, DruidDataSource> getName2source() {
        return name2source;
    }

    public void setName2source(Map<String, DruidDataSource> name2source) {
        this.name2source = name2source;
    }

    /**
     * 当更新完catalogs后调用, 检查每个catalog的datasource状态
     */
    public void checkDataSources() {
        for (JDBCCatalog catalog : name2catalog.values()) {
            checkDataSource(catalog);
        }
    }

    private boolean isNeedUpdate(DruidDataSource druidDataSource) {
        // TODO add checkout condition
        if (druidDataSource == null) {
            return true;
        }
        return false;
    }

    private void checkDataSource(JDBCCatalog catalog) {
        DruidDataSource druidDataSource = name2source.get(catalog.getName());
        boolean needUpdate = isNeedUpdate(druidDataSource);
        if (needUpdate) {
            // 数据源配置
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setUrl(catalog.getUri());
            dataSource.setDriverClassName(catalog.getDriverClass()); // 这个可以缺省的，会根据url自动识别
            dataSource.setUsername(catalog.getUsername());
            dataSource.setPassword(catalog.getPassword());

            // 下面都是可选的配置
            dataSource.setInitialSize(10);  // 初始连接数，默认0
            dataSource.setMaxActive(30);  // 最大连接数，默认8
            dataSource.setMinIdle(10);  // 最小闲置数
            dataSource.setMaxWait(2000);  // 获取连接的最大等待时间，单位毫秒
            dataSource.setPoolPreparedStatements(true); // 缓存PreparedStatement，默认false
            dataSource.setMaxOpenPreparedStatements(20); // 缓存PreparedStatement的最大数量，默认-1（不缓存）。大于0时会自动开启缓存PreparedStatement，所以可以省略上一句代码
            name2source.put(catalog.getName(), dataSource);
        }
    }

    /**
     * 将一个jdbcCatalog对象更新至内存中
     *
     * @param catalog
     */
    public void addCatalog(JDBCCatalog catalog) {
        name2catalog.put(catalog.getName(), catalog);
    }

}
