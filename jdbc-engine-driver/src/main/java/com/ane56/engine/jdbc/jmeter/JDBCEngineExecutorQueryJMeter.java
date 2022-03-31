package com.ane56.engine.jdbc.jmeter;

import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;

public class JDBCEngineExecutorQueryJMeter {
    public static void main(String[] args) {
        JDBCEngineExecutorQueryTest jdbcEngineExecutorQueryTest = new JDBCEngineExecutorQueryTest();
        JavaSamplerContext arg0 = new JavaSamplerContext(jdbcEngineExecutorQueryTest.getDefaultParameters());
        jdbcEngineExecutorQueryTest.setupTest(arg0);
        jdbcEngineExecutorQueryTest.runTest(arg0);
        jdbcEngineExecutorQueryTest.teardownTest(arg0);
        System. exit(0);
    }
}