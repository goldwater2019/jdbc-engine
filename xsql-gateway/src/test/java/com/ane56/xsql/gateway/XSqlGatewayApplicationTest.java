package com.ane56.xsql.gateway;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: zhangxinsen
 * @Date: 2022/4/27 1:55 PM
 * @Desc:
 * @Version: v1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:bootstrap.yml")
public class XSqlGatewayApplicationTest {

    @Test
    public void testPing() {
        System.out.println("hello world");
    }
}
