package com.ane56.xsql.service.controller;

import com.ane56.xsql.common.exception.XSQLException;
import com.ane56.xsql.common.model.*;
import com.ane56.xsql.service.consumer.XSqlDriverConsumer;
import com.ane56.xsql.service.consumer.XSqlExecutorConsumer;
import com.ane56.xsql.service.entity.TDriverCatalog;
import com.ane56.xsql.service.service.TDriverCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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

    @Resource
    private TDriverCatalogService tDriverCatalogService;

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

    /**
     * 分页查询
     *
     * @param tDriverCatalog 筛选条件
     * @param pageRequest    分页对象
     * @return 查询结果
     */
    @GetMapping
    public ResponseEntity<Page<TDriverCatalog>> queryByPage(TDriverCatalog tDriverCatalog, PageRequest pageRequest) {
        return ResponseEntity.ok(this.tDriverCatalogService.queryByPage(tDriverCatalog, pageRequest));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public ResponseEntity<TDriverCatalog> queryById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(this.tDriverCatalogService.queryById(id));
    }

    /**
     * 新增数据
     *
     * @param tDriverCatalog 实体
     * @return 新增结果
     */
    @PostMapping
    public ResponseEntity<TDriverCatalog> add(TDriverCatalog tDriverCatalog) {
        return ResponseEntity.ok(this.tDriverCatalogService.insert(tDriverCatalog));
    }

    /**
     * 编辑数据
     *
     * @param tDriverCatalog 实体
     * @return 编辑结果
     */
    @PutMapping
    public ResponseEntity<TDriverCatalog> edit(TDriverCatalog tDriverCatalog) {
        return ResponseEntity.ok(this.tDriverCatalogService.update(tDriverCatalog));
    }

    /**
     * 删除数据
     *
     * @param id 主键
     * @return 删除是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Integer id) {
        return ResponseEntity.ok(this.tDriverCatalogService.deleteById(id));
    }
}
