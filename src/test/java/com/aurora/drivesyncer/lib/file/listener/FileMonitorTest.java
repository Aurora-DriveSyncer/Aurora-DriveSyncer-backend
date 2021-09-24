package com.aurora.drivesyncer.lib.file.listener;

import com.aurora.drivesyncer.entity.Config;
import com.aurora.drivesyncer.entity.FileInfo;
import com.aurora.drivesyncer.lib.file.FileTestTemplate;
import com.aurora.drivesyncer.mapper.FileInfoMapper;
import com.aurora.drivesyncer.service.SyncService;
import com.aurora.drivesyncer.worker.FileMonitor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileMonitorTest extends FileTestTemplate {
    @Autowired
    SyncService syncService;
    @Autowired
    FileInfoMapper fileInfoMapper;

    @Test
    void testFullScan() throws IOException, InterruptedException {
        Config config = new Config("http://localhost:8888/webdav/",
                "user",
                "user",
                ".",
                "aurora");
        syncService.setConfig(config);
        syncService.createBlockingQueues();
        FileMonitor fileMonitor = new FileMonitor("src/main/resources/", syncService);
        fileMonitor.fullScan();
        // 等待扫描完成
        Thread.sleep(1);
        assertNotEquals(0, fileInfoMapper.selectCount(null));
        List<FileInfo> fileInfoList = fileInfoMapper.selectList(null);
        FileInfo fileInfo = fileInfoMapper.selectByParentAndName("src/main/resources/", "application.yaml");
        assertNotNull(fileInfo.getLastAccessTime());
        assertNotNull(fileInfo.getFilename());
        assertNotNull(fileInfo.getPath());
        assertTrue(fileInfo.getSize() > 10);
    }
}