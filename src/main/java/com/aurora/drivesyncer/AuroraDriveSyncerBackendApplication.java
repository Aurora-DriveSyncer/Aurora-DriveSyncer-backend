package com.aurora.drivesyncer;

import com.aurora.drivesyncer.entity.Config;
import com.aurora.drivesyncer.web.ConfigController;
import com.aurora.drivesyncer.worker.FileMonitor;
import com.aurora.drivesyncer.service.SyncService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@MapperScan("com.aurora.drivesyncer.mapper")
public class AuroraDriveSyncerBackendApplication {
    public static void main(String[] args) throws Exception {
        ApplicationContext app = SpringApplication.run(AuroraDriveSyncerBackendApplication.class, args);

        ConfigController configController = app.getBean(ConfigController.class);
        configController.setConfig(new Config("localhost", "SA", "", "/home/liu/test/aurora/", ""));
    }

}
