package com.ane56.xsql.service.manager;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.ane56.xsql.common.enumeration.UltraColumnType;
import com.ane56.xsql.common.exception.XSQLException;
import com.ane56.xsql.common.model.UltraCatalog;
import com.ane56.xsql.common.model.UltraResultColumnMetaData;
import com.ane56.xsql.common.model.UltraResultRow;
import com.ane56.xsql.common.model.UltraResultSetMetaData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhangxinsen
 * @Date: 2022/3/29 11:20 PM
 * @Desc:
 * @Version: v1.0
 */

@Slf4j
@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PooledDataSourceManager {

    /**
     * 单例方法
     */
    private static volatile PooledDataSourceManager singleton;
    // name->catalog对象
    private Map<String, UltraCatalog> name2catalog = new HashMap<>();
    // name -> datasource对象
    private Map<String, DruidDataSource> name2source = new HashMap<>();

    ;

    private PooledDataSourceManager() {
    }

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

    /**
     * 当更新完catalogs后调用, 检查每个catalog的datasource状态
     */
    public void checkDataSources() {
        for (UltraCatalog catalog : name2catalog.values()) {
            checkDataSource(catalog);
        }
    }

    /**
     * 是否需要更新连接池
     *
     * @param druidDataSource
     * @return
     */
    private boolean isNeedUpdate(DruidDataSource druidDataSource) {
        // TODO add checkout condition
        if (druidDataSource == null) {
            return true;
        }
        return false;
    }

    public void checkDataSource(UltraCatalog catalog) {
        DruidDataSource druidDataSource = name2source.get(catalog.getName());
        boolean needUpdate = isNeedUpdate(druidDataSource);
        if (needUpdate) {
            // 数据源配置
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setUrl(catalog.getJdbcUrl());
            dataSource.setDriverClassName(catalog.getDriverClassName()); // 这个可以缺省的，会根据url自动识别
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
    public void addCatalog(UltraCatalog catalog) {
        name2catalog.put(catalog.getName(), catalog);
    }

    /**
     * 将查询结果封装成一个List<UltraResultRow>对象
     *
     * @param catalogName
     * @param sqlStatement
     * @return
     */
    public List<UltraResultRow> query(String catalogName, String sqlStatement) throws SQLException, XSQLException {
        long startTime = System.currentTimeMillis();
        DruidDataSource dataSource = getName2source().get(catalogName);
        if (dataSource == null) {
            log.error("catalog: " + catalogName + " not exists, please initialize it before use");
            throw new XSQLException("catalog: " + catalogName + " not exists, please initialize it before use");
        }
        log.info("get data source costs: " + (System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
        DruidPooledConnection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<UltraResultRow> result = new LinkedList<>();
        while (resultSet.next()) {
            List<Object> ultraResultSetData = new LinkedList<>();
            UltraResultSetMetaData ultraResultSetMetaData = UltraResultSetMetaData.builder().build();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            // 配置相应的columnCount
            ultraResultSetMetaData.setColumnCount(columnCount);
            List<UltraResultColumnMetaData> ultraResultColumnMetaDataList = new LinkedList<>();
            for (int i = 1; i <= columnCount; i++) {
                int nullable = metaData.isNullable(i);
                UltraResultColumnMetaData ultraResultColumnMetaData = UltraResultColumnMetaData.builder()
                        .autoIncrement(metaData.isAutoIncrement(i))
                        .caseSensitive(metaData.isCaseSensitive(i))
                        .searchable(metaData.isSearchable(i))
                        .currency(metaData.isCurrency(i))
                        .nullable(nullable)
                        .signed(metaData.isSigned(i))
                        .columnDisplaySize(metaData.getColumnDisplaySize(i))
                        .columnLabel(metaData.getColumnLabel(i))
                        .columnName(metaData.getColumnName(i))
                        .schemaName(metaData.getSchemaName(i))
                        .precision(metaData.getPrecision(i))
                        .scale(metaData.getScale(i))
                        .tableName(metaData.getTableName(i))
                        .catalogName(metaData.getCatalogName(i))
                        .columnType(metaData.getColumnType(i))
                        .columnTypeName(metaData.getColumnTypeName(i))
                        .readOnly(metaData.isReadOnly(i))
                        .writable(metaData.isWritable(i))
                        .definitelyWritable(metaData.isDefinitelyWritable(i))
                        .columnClassName(metaData.getColumnClassName(i))
                        .ultraColumnType(UltraColumnType.findByValue(metaData.getColumnType(i)))
                        .build();
                ultraResultColumnMetaDataList.add(ultraResultColumnMetaData);
                // TODO 新增数据
                ultraResultSetData.add(resultSet.getObject(i));
            }
            ultraResultSetMetaData.setColumnMetaDataList(ultraResultColumnMetaDataList);
            result.add(
                    UltraResultRow.builder()
                            .ultraResultSetData(ultraResultSetData)
                            .ultraResultSetMetaData(ultraResultSetMetaData)
                            .build()
            );
        }
        connection.close();
        log.info("query pool costs: " + (System.currentTimeMillis() - startTime));
        return result;
    }

    public void execute(String catalogName, String sqlStatement) throws XSQLException, SQLException {
        long startTime = System.currentTimeMillis();
        DruidDataSource dataSource = getName2source().get(catalogName);
        if (dataSource == null) {
            log.error("catalog: " + catalogName + " not exists, please initialize it before use");
            throw new XSQLException("catalog: " + catalogName + " not exists, please initialize it before use");
        }
        log.info("get data source costs: " + (System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
        DruidPooledConnection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        boolean executeResult = preparedStatement.execute();
        connection.close();
        log.info("query pool costs: " + (System.currentTimeMillis() - startTime));
        return;
    }
}
