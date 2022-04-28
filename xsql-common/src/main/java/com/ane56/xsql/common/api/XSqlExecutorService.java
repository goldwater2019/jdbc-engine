package com.ane56.xsql.common.api;

import com.ane56.xsql.common.exception.XSQLException;
import com.ane56.xsql.common.model.UltraCatalog;
import com.ane56.xsql.common.model.UltraDatabaseMetaData;
import com.ane56.xsql.common.model.UltraResultRow;

import java.sql.SQLException;
import java.util.List;

public interface XSqlExecutorService {
    public List<UltraCatalog> showCatalogs();

    /**
     * 将现存的catalog的数据进行心跳, 确保正常连接
     *
     * @return
     */
    public List<UltraCatalog> showAvailableCatalogs();

    public List<UltraCatalog> showUnForbiddenCatalogs();

    public List<UltraResultRow> query(String catalogName, String query) throws SQLException, XSQLException;

    /**
     * 添加catalog
     *
     * @param catalog
     */
    public void addCatalog(UltraCatalog catalog);

    /**
     * 更新catalog对应的链接
     *
     * @param catalog
     */
    public void checkDataSource(UltraCatalog catalog);

    /**
     * 执行相应的SQL
     * @param catalogName
     * @param query
     */
    public void execute(String catalogName, String query) throws SQLException, XSQLException;


    /**
     * 从配置中心获得最新的catalog信息
     * 初始化/更新 catalogs
     */
    public void updateOrInitCatalogs();

    public UltraDatabaseMetaData getDatabaseMetaData(String catalogName) throws SQLException, XSQLException;
}
