package com.aurora.drivesyncer.integration;

import com.aurora.drivesyncer.entity.Config;
import com.aurora.drivesyncer.entity.FileInfo;
import com.aurora.drivesyncer.lib.file.FileTestTemplate;
import com.aurora.drivesyncer.lib.file.transfer.WebDAVTestUtils;
import com.aurora.drivesyncer.mapper.FileInfoMapper;
import com.aurora.drivesyncer.web.ConfigController;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.aurora.drivesyncer.lib.Utils.formatLog;

@SpringBootTest
public class SyncTest extends FileTestTemplate {
    @Autowired
    ConfigController configController;
    @Autowired
    FileInfoMapper fileInfoMapper;

    Log log = LogFactory.getLog(getClass());

    @BeforeEach
    public void setup() {
        Assumptions.assumeTrue(WebDAVTestUtils.deleteAllFiles());
    }

    @AfterEach
    public void teardown() throws IOException {
        // WebDAVTestUtils.deleteAllFiles();
    }

    void waitAllSynced() {
        try {
            Thread.sleep(5000);
            while (true) {
                QueryWrapper<FileInfo> wrapper = new QueryWrapper<>();
                wrapper.ne("status", FileInfo.SyncStatus.Synced);
                if (fileInfoMapper.selectCount(wrapper) == 0) {
                    break;
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException ignored) {
        }
    }

    @Test
    void integrationSyncTest() throws IOException {
        // 同步配置，开始同步
        log.info("START FIRST SYNCING");
        Config config = new Config("http://localhost:8888/webdav/",
                "user",
                "user",
                ".",
                "aurora");
        configController.setConfig(config);
        waitAllSynced();
        log.info(formatLog( "FINISH FIRST SYNCING"));

        log.info(formatLog("START SYNCING TEXT FILE"));
        File textFile = createTempBinaryFile(1024 * 1024);
        waitAllSynced();
        log.info(formatLog("FINISH SYNCING TEXT FILE"));

        log.info(formatLog("START SYNCING SOFT LINKS"));
        Path textFilePath = textFile.toPath();
        Path softLink = Paths.get(testDirectory, "soft_link");
        Files.createSymbolicLink(softLink, textFilePath);
        waitAllSynced();
        log.info(formatLog("FINISH SYNCING SOFT LINKS"));

        log.info(formatLog("START SYNCING HARD LINKS"));
        Path hardLink = Paths.get(testDirectory, "hard_link");
        Files.createLink(hardLink, textFilePath);
        waitAllSynced();
        log.info(formatLog("FINISH SYNCING HARD LINKS"));
    }
}
