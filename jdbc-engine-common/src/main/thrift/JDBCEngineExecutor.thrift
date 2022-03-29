namespace java com.ane56.engine.jdbc.thrit.service

include "TJDBCEngineStruct.thrift"

service JDBCEngineExecutorService {

    /**
     * SQL查询
     * 请求时绑定starttime
     * 结束时绑定endtime
     **/
    TJDBCEngineStruct.TJDBCOperationRef query(1: TJDBCEngineStruct.TJDBCOperationRef jdbcOperationRef)

}