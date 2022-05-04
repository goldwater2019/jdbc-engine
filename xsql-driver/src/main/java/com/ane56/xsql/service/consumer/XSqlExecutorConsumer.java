package com.ane56.xsql.service.consumer;

import com.ane56.xsql.common.api.XSqlExecutorService;
import com.ane56.xsql.common.exception.XSQLException;
import com.ane56.xsql.common.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/26 1:44 AM
 * @Desc:
 * @Version: v1.0
 */

@Slf4j
@Component("xSqlExecutorConsumer")
public class XSqlExecutorConsumer {
    @Resource
    private XSqlExecutorService xSqlExecutorService;

    private static final Map<String, Long> name2time = new ConcurrentHashMap<>();
    private static final Map<String, UltraDatabaseMetaData> name2metaData = new ConcurrentHashMap<>();


    public JsonResult<List<UltraCatalog>> getCatalogs() {
        return new JsonResult(xSqlExecutorService.showCatalogs());
    }

    public void init() {
        List<UltraCatalog> catalogs = xSqlExecutorService.showCatalogs();
        if (catalogs == null || catalogs.size() == 0) {
            log.info("no available catalog exists");
        }

        List<UltraCatalog> ultraCatalogs = xSqlExecutorService.showCatalogs();
        for (UltraCatalog ultraCatalog : ultraCatalogs) {
            xSqlExecutorService.checkDataSource(ultraCatalog);
        }
    }

    public JsonResult<List<UltraResultRow>> query(UltraBaseStatement ultraBaseStatement) {
        JsonResult<List<UltraResultRow>> jsonResult = null;
        try {
            List<UltraResultRow> resultRowList = xSqlExecutorService.query(
                    ultraBaseStatement.getCatalogName(),
                    ultraBaseStatement.getSql()
            );
            jsonResult = new JsonResult<>(resultRowList);
        } catch (SQLException | XSQLException e) {
            jsonResult = new JsonResult<>(-1, e.getMessage());
        }
        return jsonResult;
    }

    public JsonResult<Boolean> execute(UltraBaseStatement ultraBaseStatement) {
        JsonResult<Boolean> jsonResult = null;
        try {
            xSqlExecutorService.execute(
                    ultraBaseStatement.getCatalogName(),
                    ultraBaseStatement.getSql()
            );
            jsonResult = new JsonResult<>(true);
        } catch (SQLException | XSQLException e) {
            jsonResult = new JsonResult<>(-1, e.getMessage());
        }
        return jsonResult;
    }

    public void ping() throws SQLException, XSQLException {
        List<UltraCatalog> ultraCatalogs = xSqlExecutorService.showUnForbiddenCatalogs();
        for (UltraCatalog ultraCatalog : ultraCatalogs) {
            List<UltraResultRow> ultraResultRows = xSqlExecutorService.query(ultraCatalog.getCatalogName(), "select 1");
            log.info(String.valueOf(ultraResultRows.get(0)));
        }
    }

    public JsonResult<UltraDatabaseMetaData> getMetaData(UltraBaseStatement ultraBaseStatement) throws SQLException, XSQLException {
        String catalogName = ultraBaseStatement.getCatalogName();
        Long lastUpdateTime = name2time.getOrDefault(catalogName, 0L);
        if (System.currentTimeMillis() - lastUpdateTime <= 5000L) {
            return new JsonResult<>(name2metaData.get(catalogName));
        } else {
            UltraDatabaseMetaData databaseMetaData = xSqlExecutorService.getDatabaseMetaData(catalogName);
            name2metaData.put(catalogName, databaseMetaData);
            name2time.put(catalogName, System.currentTimeMillis());
            return new JsonResult<>(databaseMetaData);
        }
    }
}
