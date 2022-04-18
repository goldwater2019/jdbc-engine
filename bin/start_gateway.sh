#!/usr/bin/env bash

if [ ! -n "$JDBC_ENGINE_HOME" ]; then
  JDBC_ENGINE_HOME=$(cd `dirname $0`/..;pwd)
fi

echo "JDBC_ENGINE_HOME: $JDBC_ENGINE_HOME"

cd $JDBC_ENGINE_HOME
if [ ! -d "pid/" ];then
  mkdir pid
fi


if [ ! -d "logs/" ];then
  mkdir logs
fi

nohup java -Xbootclasspath/a:$JDBC_ENGINE_HOME/conf -jar jar/jdbc-engine-gateway.jar \
  $@ 2>&1 | tee logs/jdbc-engine-gateway-$USER.log & \
    echo $! > pid/gateway.pid