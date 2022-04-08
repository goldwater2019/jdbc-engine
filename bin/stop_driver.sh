#!/usr/bin/env bash

kill -9 $(jps | grep 'JDBCEngineDriverServiceServer'| awk '{print $1}' | sort -n)