
### quick start

#### zk

使用docker快速部署一个`zk`服务

```shell
docker run -it -d --name zk -p 2181:2181 -p 2888:2888 -p 3888:3888 zookeeper  
```


#### JDBC_ENGINE_DRIVER

`driver`侧启动需要满足下列两个条件中的一个:
1. 指定环境变量`JDBC_ENGINE_HOME`
2. 指定启动参数`--conf-dir=/path/to/conf/dir`

启动方式: 
```shell
java -cp jar/jdbc-engine-driver.jar com.ane56.engine.jdbc.driver.JDBCEngineDriverServiceServer
```

#### JDBC_ENGINE_EXECUTOR

`executor`侧启动需要满足下列两个条件中的一个:
1. 指定环境变量`JDBC_ENGINE_HOME`
2. 指定启动参数`--conf-dir=/path/to/conf/dir`

启动方式:
```shell
java -cp jar/jdbc-engine-executor.jar com.ane56.engine.jdbc.executor.JDBCEngineExecutorServiceServer
```

#### JDBC_ENGINE_GATEWAY

`gateway` 侧启动需要满足下面的条件之一
1. 指定环境变量`JDBC_ENGINE_HOME`
2. 指定启动参数`-Dengine.jdbc.config-dir`

```shell
java -Xbootclasspath/a:/path/to/conf/dir -jar jdbc-engine-gateway/target/jdbc-engine-gateway-1.0-SNAPSHOT.jar
```
