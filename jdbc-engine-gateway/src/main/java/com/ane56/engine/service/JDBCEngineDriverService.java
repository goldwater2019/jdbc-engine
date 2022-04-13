package com.ane56.engine.service;


import com.ane56.engine.jdbc.model.JsonResult;
import com.ane56.engine.jdbc.model.JDBCCatalog;
import com.ane56.engine.jdbc.model.JDBCResultRef;

import java.util.List;

public interface JDBCEngineDriverService {

    public JsonResult<String> getHaZookeeperQuorum();

    JsonResult<List<JDBCCatalog>> listCatalogs();

    JsonResult<JDBCResultRef> query(String querySql);
}
