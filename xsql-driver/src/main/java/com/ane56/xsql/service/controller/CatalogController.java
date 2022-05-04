package com.ane56.xsql.service.controller;

import com.ane56.xsql.common.exception.XSQLException;
import com.ane56.xsql.common.model.*;
import com.ane56.xsql.service.consumer.XSqlDriverConsumer;
import com.ane56.xsql.service.consumer.XSqlExecutorConsumer;
import com.ane56.xsql.service.provider.XSqlDriverServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    // private TDriverCatalogService tDriverCatalogService;
    private XSqlDriverServiceImpl xSqlExecutorDriverService;

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

    // TODO 分页查询

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<UltraCatalog> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.xSqlExecutorDriverService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param ultraCatalog 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<UltraCatalog> add(UltraCatalog ultraCatalog) {
        return ResponseEntity.ok(this.xSqlExecutorDriverService.insert(ultraCatalog));
    }

    /**
     * 编辑数据
     *
     * @param ultraCatalog 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<UltraCatalog> edit(UltraCatalog ultraCatalog) {
        return ResponseEntity.ok(this.xSqlExecutorDriverService.update(ultraCatalog));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Integer id) {
        return ResponseEntity.ok(this.xSqlExecutorDriverService.deleteById(id));
    }
}
