#!/usr/bin/env bash
rm -rf jdbc-engine-common/src/main/java/com/ane56/engine/jdbc/thrift &&
thrift -out jdbc-engine-common/src/main/java -r --gen java jdbc-engine-common/src/main/thrift/JDBCEngineDriver.thrift