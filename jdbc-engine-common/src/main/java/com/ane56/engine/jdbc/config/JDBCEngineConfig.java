package com.ane56.engine.jdbc.config;

public class JDBCEngineConfig {


    // kyuubi.ha.zookeeper.quorum
    public static String haZookeeperQuorum = "10.10.106.102:2181";

    public static String haZookeeperDriverUriPath = "/engine/jdbc/driver/uri";

    public static String haZookeeperExecutorUriPath = "/engine/jdbc/executor/uri";


    public static int jdbcEngineDriverTimeout = 10000;
}
