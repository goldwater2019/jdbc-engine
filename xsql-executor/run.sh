#!/usr/bin/env bash

CLASS=com.ane56.xsql.service.XSqlExexutorServiceApplication
CURRENT_DIR=$(cd "$(dirname "$0")";pwd)
JAR_FILES=$(find $CURRENT_DIR/target -name  '*.jar')
CLASSPATH=""
for JAR_FILE in $JAR_FILES
  do
    CLASSPATH="$JAR_FILE:$CLASSPATH"
  done
ENDCHAR=${CLASSPATH: -1}
if [[ $ENDCHAR==";" ]]; then
  CLASSPATH=${CLASSPATH%?}
fi

java -cp $CLASSPATH $CLASS