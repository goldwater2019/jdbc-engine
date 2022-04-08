#!/usr/bin/env bash

kill -9 $(jps | grep 'JDBCEngineExecutorServiceServer'| awk '{print $1}' | sort -n)