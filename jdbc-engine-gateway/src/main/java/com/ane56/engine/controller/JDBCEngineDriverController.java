package com.ane56.engine.controller;


import com.ane56.engine.dto.JDBCQueryReq;
import com.ane56.engine.dto.JsonResult;
import com.ane56.engine.jdbc.model.JDBCCatalog;
import com.ane56.engine.jdbc.model.JDBCResultRef;
import com.ane56.engine.service.JDBCEngineDriverService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("driver")
public class JDBCEngineDriverController {

    @Resource
    private JDBCEngineDriverService jdbcEngineDriverService;

    @GetMapping("config/ha/zk/get")
    public JsonResult<String> getHaZookeeperQuorum() {
        return jdbcEngineDriverService.getHaZookeeperQuorum();
    }

    @GetMapping("catalog/list")
    public JsonResult<List<JDBCCatalog>> listCatalogs() {
        return jdbcEngineDriverService.listCatalogs();
    }

    @PostMapping(value = "query", produces = "application/json")
    public JsonResult<JDBCResultRef> query(@RequestBody JDBCQueryReq jdbcQueryReq) {
        return jdbcEngineDriverService.query(jdbcQueryReq.getQuerySql());
    }
}