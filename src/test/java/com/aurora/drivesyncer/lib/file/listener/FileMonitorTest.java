package com.aurora.drivesyncer.lib.file.listener;

import com.aurora.drivesyncer.entity.FileInfo;
import com.aurora.drivesyncer.mapper.FileInfoMapper;
import com.aurora.drivesyncer.service.SyncService;
import com.aurora.drivesyncer.utils.FileTests;
import com.aurora.drivesyncer.worker.FileMonitor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileMonitorTest extends FileTests {
    @Autowired
    SyncService syncService;
    @Autowired
    FileInfoMapper fileInfoMapper;

    @Test
    void testFullScan() throws IOException, InterruptedException {
        syncService.createBlockingQueues();
        FileMonitor fileMonitor = new FileMonitor("src/main/resources/", syncService);
        fileMonitor.fullScan();
        // 等待扫描完成
        Thread.sleep(1);
        assertNotEquals(0, fileInfoMapper.selectCount(null));
        List<FileInfo> fileInfoList = fileInfoMapper.selectList(null);
        FileInfo fileInfo = fileInfoMapper.selectByPathAndName("src/main/resources/", "application.yaml");
        assertNotNull(fileInfo.getLastAccessTime());
        assertNotNull(fileInfo.getFilename());
        assertNotNull(fileInfo.getPath());
        assertTrue(fileInfo.getSize() > 10);
    }
}