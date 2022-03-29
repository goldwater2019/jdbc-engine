-----------------------------------------


| java.sql.type | thrift.type | thrift.enum  | sql2thrift | thrift2sql       |
| ------------- | ----------  |--------------| ---------- |------------------|
| java.sql.Date | long        | DATE         | x.toLocalDate() | new Date(x) |
|