package com.ane56.xsql.service.provider;

import com.ane56.xsql.common.api.XSqlExecutorService;
import com.ane56.xsql.common.exception.XSQLException;
import com.ane56.xsql.common.model.UltraCatalog;
import com.ane56.xsql.common.model.UltraDatabaseMetaData;
import com.ane56.xsql.common.model.UltraResultRow;
import com.ane56.xsql.service.consumer.XSqlDriverConsumer;
import com.ane56.xsql.service.manager.PooledDataSourceManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/26 1:36 AM
 * @Desc:
 * @Version: v1.0
 */

@DubboService
@Slf4j
@RefreshScope
public class XSqlExecutorServiceImpl implements XSqlExecutorService {

    private PooledDataSourceManager pooledDataSourceManager;

    @Autowired
    private XSqlDriverConsumer xSqlDriverConsumer;

    @Override
    public List<UltraCatalog> showCatalogs() {
        return showAvailableCatalogs();
    }

    @Override
    public List<UltraCatalog> showAvailableCatalogs() {
        List<UltraCatalog> catalogs = xSqlDriverConsumer.getAvailableAndUnForbiddenCatalogs();
        if (catalogs == null) {
            catalogs = new LinkedList<>();
        }
        for (UltraCatalog catalog : catalogs) {
            addCatalog(catalog);
            checkDataSource(catalog);
        }
        return catalogs;
    }

    @Override
    public List<UltraCatalog> showUnForbiddenCatalogs() {
        return showAvailableCatalogs();
    }

    @Override
    public List<UltraResultRow> query(String catalogName, String query) throws SQLException, XSQLException {
        checkInitialStatus();
        List<UltraResultRow> result = pooledDataSourceManager.query(catalogName, query);
        return result;
    }

    @Override
    public void addCatalog(UltraCatalog catalog) {
        checkInitialStatus();
        pooledDataSourceManager.addCatalog(catalog);
    }

    @Override
    public void checkDataSource(UltraCatalog catalog) {
        checkInitialStatus();
        pooledDataSourceManager.checkDataSource(catalog);
    }

    @Override
    public void execute(String catalogName, String query) throws SQLException, XSQLException {
        checkInitialStatus();
        pooledDataSourceManager.execute(catalogName, query);
    }

    @Override
    public void updateOrInitCatalogs() {
        checkInitialStatus();
        Map<String, UltraCatalog> name2catalog = pooledDataSourceManager.getName2catalog();
        // TODO 获得配置的catalog, 然后对比是否相同
        // 如果不同, 则关闭当前连接, 并且创建新的连接
    }

    @Override
    public UltraDatabaseMetaData getDatabaseMetaData(String catalogName) throws SQLException, XSQLException {
        checkInitialStatus();
        return pooledDataSourceManager.getDatabaseMetaData(catalogName);
    }

    /**
     * 检查状态
     */
    private void checkInitialStatus() {
        if (pooledDataSourceManager == null) {
            pooledDataSourceManager = PooledDataSourceManager.getInstance();
        }
    }
}
