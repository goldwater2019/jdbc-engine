package com.ane56.engine.jdbc.jmeter;

import com.ane56.engine.jdbc.exetuor.JDBCEngineExecutorServiceClientManager;
import com.ane56.engine.jdbc.model.JDBCEngineExecutorRef;
import com.ane56.engine.jdbc.model.JDBCResultRef;
import lombok.extern.slf4j.Slf4j;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.thrift.TException;

import java.util.UUID;

@Slf4j
public class JDBCEngineExecutorQueryTest extends AbstractJavaSamplerClient {

    private JDBCEngineExecutorServiceClientManager jdbcEngineExecutorServiceClientManager;

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult results = new SampleResult();//初始化记录结果
        results.sampleStart();//计时开始
        long startTime = System.currentTimeMillis();
        int maxIterationNum = 1;
        for (int i = 0; i < maxIterationNum; i++) {
            JDBCEngineExecutorRef engineExecutorRef = JDBCEngineExecutorRef.builder()
                    .executorRefId(UUID.randomUUID())
                    .host("192.168.66.117")
                    .port(8889)
                    .build();
            String querySQL = "select * from tx_dev.bd_center_center_month;";
//            String querySQL = "";
            JDBCResultRef jdbcResultRef = null;
            try {
                jdbcResultRef = jdbcEngineExecutorServiceClientManager.query("starrocks", querySQL, engineExecutorRef);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TException e) {
                e.printStackTrace();
            }
            System.out.println(jdbcResultRef);
            log.info("index: " + i + ", duration: " + (jdbcResultRef.getJdbcOperationRef().getEndTime() - jdbcResultRef.getJdbcOperationRef().getStartTime()));
        }
        System.out.println(System.currentTimeMillis() - startTime);
        results.sampleEnd();
        results.setSuccessful(true);
        results.setResponseData("成功", null);
        results.setDataType(SampleResult.TEXT);
        return results;
    }

    @Override
    public void setupTest(JavaSamplerContext context) {
        if (jdbcEngineExecutorServiceClientManager == null) {
            jdbcEngineExecutorServiceClientManager = JDBCEngineExecutorServiceClientManager.getInstance();
        }
        super.setupTest(context);
    }

    @Override
    public void teardownTest(JavaSamplerContext context) {
        super.teardownTest(context);
    }

    @Override
    public Arguments getDefaultParameters() {
        return super.getDefaultParameters();
    }
}
