namespace java com.ane56.engine.jdbc.thrit.service

include "TJDBCEngineStruct.thrift"

service JDBCEngineExecutorService {

    /**
    * 心跳
    **/
    bool heartBeat(1: TJDBCEngineStruct.TJDBCEngineExecutor jdbcEngineExecutor),

}