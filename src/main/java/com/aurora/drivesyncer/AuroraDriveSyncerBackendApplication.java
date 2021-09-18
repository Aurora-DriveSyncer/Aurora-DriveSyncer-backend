package com.aurora.drivesyncer;

import com.aurora.drivesyncer.lib.file.watcher.FileMonitor;
import com.aurora.drivesyncer.service.SyncService;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

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
