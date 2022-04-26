package com.ane56.xsql.service.consumer;

import com.ane56.xsql.common.api.XSqlExecutorService;
import com.ane56.xsql.common.exception.XSQLException;
import com.ane56.xsql.common.model.JsonResult;
import com.ane56.xsql.common.model.UltraBaseStatement;
import com.ane56.xsql.common.model.UltraCatalog;
import com.ane56.xsql.common.model.UltraResultRow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

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

    public JsonResult<List<UltraCatalog>> getCatalogs() {
        return (JsonResult<List<UltraCatalog>>) new JsonResult(xSqlExecutorService.showCatalogs());
    }

    public void init() {
        long startTime = System.currentTimeMillis();
        List<UltraCatalog> catalogs = xSqlExecutorService.showCatalogs();
        if (catalogs.size() == 0) {
            xSqlExecutorService.addCatalog(
                    UltraCatalog.builder()
                            .name("starrocks")
                            .driverClassName("com.mysql.cj.jdbc.Driver")
                            .isAvailable(true)
                            .isForbidden(false)
                            .jdbcUrl("jdbc:mysql://10.10.230.2:9030/tx_dev")
                            .username("anetx")
                            .password("anetx")
                            .build()
            );

            xSqlExecutorService.addCatalog(
                    UltraCatalog.builder()
                            .name("aliyun")
                            .driverClassName("com.mysql.cj.jdbc.Driver")
                            .isAvailable(true)
                            .isForbidden(false)
                            .jdbcUrl("jdbc:mysql://rm-uf67xpwhzp9xvuciv2o.mysql.rds.aliyuncs.com:3306")
                            .username("root")
                            .password("Luxin@19980516")
                            .build()
            );
        }

        List<UltraCatalog> ultraCatalogs = xSqlExecutorService.showCatalogs();
        for (UltraCatalog ultraCatalog : ultraCatalogs) {
            xSqlExecutorService.checkDataSource(ultraCatalog);
        }
        log.info("initial finished, elapsed: " + (System.currentTimeMillis() - startTime) + " ms");
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
}
