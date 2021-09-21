package com.aurora.drivesyncer.lib.file.watcher;

import com.aurora.drivesyncer.entity.FileInfo;
import com.aurora.drivesyncer.mapper.FileInfoMapper;
import com.aurora.drivesyncer.service.SyncService;
import com.aurora.drivesyncer.utils.FileTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class FileMonitorTests extends FileTests {
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
        FileInfo fileInfo = fileInfoMapper.selectByPathAndName("src/main/resources/", "application.yaml");
//        assert 大小大于 0
//                时间大于 0
//            。。。。。
    }

    @Test
    void testAddFileListener() throws IOException {
        //todo: test
    }
}