package com.ane56.xsql.service.controller;

import com.ane56.xsql.common.exception.XSQLException;
import com.ane56.xsql.common.model.*;
import com.ane56.xsql.service.consumer.XSqlDriverConsumer;
import com.ane56.xsql.service.consumer.XSqlExecutorConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/26 1:20 AM
 * @Desc:
 * @Version: v1.0
 */

@RestController()
@RequestMapping("catalog")
public class CatalogController {
    @Autowired
    private XSqlDriverConsumer xSqlDriverConsumer;
    @Autowired
    private XSqlExecutorConsumer xSqlExecutorConsumer;

    @GetMapping("echo")
    public String echo() {
        return xSqlDriverConsumer.echo();
    }

    @GetMapping("show")
    public JsonResult<List<UltraCatalog>> showCatalogs() {
        xSqlExecutorConsumer.init();
        return xSqlExecutorConsumer.getCatalogs();
    }

    @PostMapping("query")
    public JsonResult<List<UltraResultRow>> query(@RequestBody UltraBaseStatement ultraBaseStatement) {
        return xSqlExecutorConsumer.query(ultraBaseStatement);
    }

    @PostMapping("execute")
    public JsonResult<Boolean> execute(@RequestBody UltraBaseStatement ultraBaseStatement) {
        return xSqlExecutorConsumer.execute(ultraBaseStatement);
    }

    @PostMapping("metadata")
    public JsonResult<UltraDatabaseMetaData> getMetaData(@RequestBody UltraBaseStatement ultraBaseStatement) throws SQLException, XSQLException {
        return xSqlExecutorConsumer.getMetaData(ultraBaseStatement);
    }
}
