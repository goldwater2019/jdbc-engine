package com.ane56.xsql.service.provider;

import com.ane56.xsql.common.api.XSqlExecutorService;
import com.ane56.xsql.common.exception.XSQLException;
import com.ane56.xsql.common.model.UltraCatalog;
import com.ane56.xsql.common.model.UltraResultRow;
import com.ane56.xsql.service.manager.PooledDataSourceManager;
import com.google.common.collect.ImmutableList;
import org.apache.dubbo.config.annotation.DubboService;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/26 1:36 AM
 * @Desc:
 * @Version: v1.0
 */

@DubboService
public class XSqlExecutorServiceImpl implements XSqlExecutorService {

    // TODO 单例, 获得连接池管理对象
    private PooledDataSourceManager pooledDataSourceManager;

    @Override
    public List<UltraCatalog> showCatalogs() {
        checkInitialStatus();
        return ImmutableList.copyOf(pooledDataSourceManager.getName2catalog().values());
    }

    @Override
    public List<UltraCatalog> showAvailableCatalogs() {
        return null;
    }

    @Override
    public List<UltraCatalog> showUnForbiddenCatalogs() {
        checkInitialStatus();
        List<UltraCatalog> result = new LinkedList<>();
        for (UltraCatalog ultraCatalog : showCatalogs()) {
            if (!ultraCatalog.getIsForbidden()) {
                result.add(ultraCatalog);
            }
        }
        return result;
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

    /**
     * 检查状态
     */
    private void checkInitialStatus() {
        if (pooledDataSourceManager == null) {
            pooledDataSourceManager = PooledDataSourceManager.getInstance();
        }
    }
}
