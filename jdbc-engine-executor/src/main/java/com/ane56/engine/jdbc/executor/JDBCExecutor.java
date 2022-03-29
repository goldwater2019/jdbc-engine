package com.ane56.engine.jdbc.executor;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.ane56.engine.jdbc.driver.JDBCEngineDriverServiceClientManager;
import com.ane56.engine.jdbc.model.JDBCCatalog;
import com.ane56.engine.jdbc.model.thrift.JDBCEngineDriverServiceClientSuite;
import com.ane56.engine.jdbc.thrit.service.JDBCEngineDriverService;
import com.ane56.engine.jdbc.thrit.struct.TJDBCCatalog;
import org.apache.thrift.TException;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.sql.Types;

public class JDBCExecutor {
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

    private void checkDataSources() {
        for (JDBCCatalog catalog : name2catalog.values()) {
            checkDataSource(catalog);
        }
    }

    private boolean isNeedUpdate(DruidDataSource druidDataSource) {
        // TODO add checkout condition
        return true;
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

    private void addCatalog(JDBCCatalog catalog) {
        name2catalog.put(catalog.getName(), catalog);
    }

    public static void main(String[] args) throws InterruptedException, TException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        JDBCExecutor jdbcExecutor = new JDBCExecutor();

        JDBCEngineDriverServiceClientManager clientManager = JDBCEngineDriverServiceClientManager.getInstance("localhost", 8888);
        JDBCEngineDriverServiceClientSuite availableClient = clientManager.getAvailableClient();
        JDBCEngineDriverService.Client client = availableClient.getClient();
        List<TJDBCCatalog> tCatalogs = client.getCatalogs();
        clientManager.close(availableClient.getTTransport());

        List<JDBCCatalog> catalogList = new LinkedList<>();
        for (TJDBCCatalog tCatalog : tCatalogs) {
            catalogList.add(JDBCCatalog.parseFromTJDBCCatalog(tCatalog));
        }
        for (JDBCCatalog jdbcCatalog : catalogList) {
            System.out.println(jdbcCatalog);
            jdbcExecutor.addCatalog(jdbcCatalog);
        }
        jdbcExecutor.checkDataSources();

        /**
         * for test
         */
        Map<String, DruidDataSource> name2source = jdbcExecutor.getName2source();
        for (Map.Entry<String, DruidDataSource> stringDruidDataSourceEntry : name2source.entrySet()) {
            DruidDataSource druidDataSource = stringDruidDataSourceEntry.getValue();
            DruidPooledConnection connection = druidDataSource.getConnection();
            String sql = "select send_date as sdate, * from tx_dev.bd_center_center_month";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 0 ; i < columnCount; i++) {
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    int columnType = metaData.getColumnType(i + 1);
                    String columnClassName = metaData.getColumnClassName(i + 1);
                    System.out.println(columnLabel + "\t" + columnType + "\t" + columnClassName);
                }
            }
        }

        clientManager.shutdown();
    }
}
