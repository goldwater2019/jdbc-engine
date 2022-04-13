#!/usr/bin/env bash

JDBC_ENGINE_HOME=$(cd `dirname $0`/..;pwd)
echo "JDBC ENGINE HOME: $JDBC_ENGINE_HOME"
echo "CD TO $JDBC_ENGINE_HOME"
cd $JDBC_ENGINE_HOME
# mvn clean install package -DskipTests

if [ ! -d "jar/" ];then
  mkdir jar
fi

# cp
cp jdbc-engine-common/target/*-jar-with-dependencies.jar jar/jdbc-engine-common.jar
cp jdbc-engine-driver/target/*-jar-with-dependencies.jar jar/jdbc-engine-driver.jar
cp jdbc-engine-executor/target/*-jar-with-dependencies.jar jar/jdbc-engine-executor.jar
cp jdbc-engine-gateway/target/*.jar jar/jdbc-engine-gateway.jar

if [ ! -d "target/" ];then
  mkdir target
fi

tar -zcvf target/jdbc-engine.tgz ../jdbc-engine/bin ../jdbc-engine/jar