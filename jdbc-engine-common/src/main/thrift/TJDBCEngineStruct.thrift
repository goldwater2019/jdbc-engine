namespace java com.ane56.engine.jdbc.thrit.struct

include "TJDBCEngineEnum.thrift"

struct TJDBCEngineExecutor {
    1: string host,
    2: i32 port,
    3: string prefix,
    4: string executorRefId
}

struct TJDBCCatalog {
    1: string name,
    2: string driver,
    3: string url,
    4: string username,
    5: string password
}

struct TJDBCOperationRef {
    1: i64 startTime,
    2: i64 endTime,
    3: string catalogName,
    4: string operationRefId,
    5: string sqlStatement,
    6: TJDBCResultSet tJDBCResultSet,
    7: TJDBCEngineEnum.TJDBCQueryStatus queryStatus,
    8: string message
}

struct TJDBCResultColumn {
    1: string columnName,
    2: TJDBCEngineEnum.TJDBCColumnType columnType,
    3: string columnClassName,
    4: string columnValue
}

struct TJDBCRsultRow {
    1: list<TJDBCResultColumn> columnList
}

struct TJDBCResultSet {
    1: list<TJDBCRsultRow> resultRowList;
}

struct TJDBCResultRef {
    1: TJDBCResultSet resultSet,
    2: TJDBCOperationRef operationRef
}