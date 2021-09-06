package com.aurora.drivesyncer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.aurora.drivesyncer.mapper")
public class AuroraDriveSyncerBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuroraDriveSyncerBackendApplication.class, args);
    }

}
