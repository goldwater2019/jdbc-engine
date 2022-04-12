package com.ane56.engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.ane56.engine.controller", "com.ane56.engine.service"})
public class SimpleJDBCEngineGateway {
    public static void main(String[] args) {
        SpringApplication.run(SimpleJDBCEngineGateway.class, args);
    }
}
