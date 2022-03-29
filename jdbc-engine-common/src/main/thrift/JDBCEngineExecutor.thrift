namespace java com.ane56.engine.jdbc.thrit.service

include "TJDBCEngineStruct.thrift"

service JDBCEngineExecutorService {

    /**
    * 心跳
    **/
    bool heartBeat(1: TJDBCEngineStruct.TJDBCEngineExecutor jdbcEngineExecutor),

    /**
    * 获得相应的catalogs
    **/
    list<TJDBCEngineStruct.TJDBCCatalog> getCatalogs(),

    /**
     *  add catalog
     **/
     bool addCatalog(1: TJDBCEngineStruct.TJDBCCatalog jdbcCatalog)
}