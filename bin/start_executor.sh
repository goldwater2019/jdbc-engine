#!/usr/bin/env bash

JDBC_ENGINE_HOME=$(cd `dirname $0`/..;pwd)

cd $JDBC_ENGINE_HOME
if [ ! -d "pid/" ];then
  mkdir pid
fi


if [ ! -d "logs/" ];then
  mkdir logs
fi

nohup java -cp $JDBC_ENGINE_HOME/jar/jdbc-engine-executor.jar \
  com.ane56.engine.jdbc.executor.JDBCEngineExecutorServiceServer $@ 2>&1 | tee logs/jdbc-engine-executor-$USER.log & \
                                                                        echo $! > pid/executor.pid