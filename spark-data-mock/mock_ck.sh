#!/usr/bin/env bash

CLASS=com.ane56.bigdata.SparkClickhouseOriginalMock
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

if [[ -n $HADOOP_HOME ]]; then
  CLASSPATH=$CLASSPATH:$HADOOP_HOME/etc/hadoop
fi

if [[ -n $HIVE_HOME ]]; then
  CLASSPATH=$CLASSPATH:$HIVE_HOME/conf
fi

echo $CLASSPATH

java -cp $CLASSPATH $CLASS $@