package com.aurora.drivesyncer;

import com.aurora.drivesyncer.lib.file.watcher.FileMonitor;
import com.aurora.drivesyncer.service.SyncService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.File;

@SpringBootApplication
@MapperScan("com.aurora.drivesyncer.mapper")
public class AuroraDriveSyncerBackendApplication {
    public static void main(String[] args) throws Exception {
        ApplicationContext app = SpringApplication.run(AuroraDriveSyncerBackendApplication.class, args);

        SyncService syncService = app.getBean(SyncService.class);
        FileMonitor fileMonitor = new FileMonitor("/home/liu/test/aurora", syncService);
        fileMonitor.start();

    }

}
