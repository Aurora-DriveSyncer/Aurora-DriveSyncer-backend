package com.aurora.drivesyncer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@MapperScan("com.aurora.drivesyncer.mapper")
public class AuroraDriveSyncerBackendApplication {
    public static void main(String[] args) throws Exception {
        ApplicationContext app = SpringApplication.run(AuroraDriveSyncerBackendApplication.class, args);
    }
}
