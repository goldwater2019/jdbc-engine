#!/usr/bin/env bash

JDBC_ENGINE_HOME=$(cd `dirname $0`/..;pwd)

nohup java -cp $JDBC_ENGINE_HOME/jar/jdbc-engine-driver.jar \
    com.ane56.engine.jdbc.pressure.DriverServerSideQuery $@ \
    $@ 2>&1 | tee logs/jdbc-engine-pressure-test-$USER.log & \
    echo $! > pid/pressure_test.pid