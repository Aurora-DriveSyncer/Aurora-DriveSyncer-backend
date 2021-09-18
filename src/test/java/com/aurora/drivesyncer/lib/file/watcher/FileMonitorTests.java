package com.aurora.drivesyncer.lib.file.watcher;

import com.aurora.drivesyncer.service.SyncService;
import com.aurora.drivesyncer.utils.FileTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileMonitorTests extends FileTests {
    @Autowired
    SyncService syncService;

    @Test
    void testFullScan() throws IOException {
        //todo
    }

    @Test
    void testAddFileListener() throws IOException {
        //todo
    }
}