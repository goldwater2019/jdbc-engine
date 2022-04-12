-----------------------------------------

| java.sql.type | thrift.type | thrift.enum  | sql2thrift | thrift2sql       |
| ------------- | ----------  |--------------| ---------- |------------------|
| java.sql.Date | long        | DATE         | x.toLocalDate() | new Date(x) |


### quick start

#### zk

using docker, launch a zk server  

```shell
docker run -it -d --name zk -p 2181:2181 -p 2888:2888 -p 3888:3888 zookeeper  
```


