#!/usr/bin/env bash

JDBC_ENGINE_HOME=$(cd `dirname $0`/..;pwd)

java -cp $JDBC_ENGINE_HOME/jar/jdbc-engine-driver.jar \
    com.ane56.engine.jdbc.pressure.DriverServerSideQuery $@