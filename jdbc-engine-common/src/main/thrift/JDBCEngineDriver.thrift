namespace java com.ane56.engine.jdbc.thrit.service

include "TJDBCEngineStruct.thrift"

service JDBCEngineDriverService {

    /**
    * 心跳
    **/
    bool heartBeat(1: TJDBCEngineStruct.TJDBCEngineExecutor jdbcEngineExecutor),

    /**
    * 获得相应的心跳
    **/
    list<TJDBCEngineStruct.TJDBCCatalog> getCatalogs(),
}