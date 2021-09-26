package com.aurora.drivesyncer.integration;

import com.aurora.drivesyncer.entity.Config;
import com.aurora.drivesyncer.entity.FileInfo;
import com.aurora.drivesyncer.lib.file.FileTestTemplate;
import com.aurora.drivesyncer.lib.file.transfer.WebDAVClientTest;
import com.aurora.drivesyncer.mapper.FileInfoMapper;
import com.aurora.drivesyncer.web.ConfigController;
import com.aurora.drivesyncer.web.FileController;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

import static com.aurora.drivesyncer.lib.log.LogUtils.formatLog;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
public class SyncTest extends FileTestTemplate {
    @Autowired
    FileController fileController;
    @Autowired
    ConfigController configController;
    @Autowired
    FileInfoMapper fileInfoMapper;

    Log log = LogFactory.getLog(getClass());

    @BeforeEach
    public void setup() {
        Assumptions.assumeTrue(WebDAVClientTest.initializeServer());
    }

    @AfterEach
    public void teardown() throws IOException {
    }

    // 等待同步完成
    // 轮讯数据库，直至所有 fileInfo 均为 Synced
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
    void integrationSyncTest() throws IOException, InterruptedException {
        // 同步配置，开始同步
        log.info("START FIRST SYNCING");
        Config config = new Config("http://localhost:8888/webdav/",
                "user",
                "user",
                ".",
                "aurora");
        configController.setConfig(config);
        waitAllSynced();
        log.info(formatLog("FINISH FIRST SYNCING"));

        // 监听文件，开始同步
        log.info(formatLog("START SYNCING NEW FILE"));
        File file = createTempBinaryFile(1024 * 1024);
        waitAllSynced();
        log.info(formatLog("FINISH SYNCING NEW FILE"));

        // 下载文件
        log.info(formatLog("START DOWNLOADING FILE"));
        String[] fileList = {
                "pom.xml",
                "src/main/resources/application.yaml",
                "src/main/resources/schema.sql",
                "src/main/java/com/aurora/drivesyncer/AuroraDriveSyncerBackendApplication.java"
        };
        HttpServletResponse response = new MockHttpServletResponse();
        for (String path : fileList) {
            fileController.downloadFile(path, response);
            log.info(String.format("%s is the guessed Content-Type of %s", response.getContentType(), path));
        }
        log.info(formatLog("FINISH DOWNLOADING FILE"));

        // 恢复文件
        String restoreDest = "../aurora-restore/";
        log.info(formatLog("START RESTORING FILE TO " + restoreDest));
        fileController.restoreDrive(restoreDest);
        long originSize = FileUtils.sizeOfDirectory(new File("src"));
        for (int timeout = 0; timeout <= 50; timeout++) {
            Thread.sleep(2000);
            if (FileUtils.sizeOfDirectory(new File(restoreDest + "src")) == originSize) {
                break;
            }
            assertNotEquals(50, timeout);
            log.info(formatLog("WAITING FOR " + restoreDest + " REACHES ORIGIN'S SIZE"));
        }
        log.info(formatLog("FINISH RESTORING FILE"));
        FileUtils.deleteDirectory(new File(restoreDest));

//        log.info(formatLog("START SYNCING SOFT LINKS"));
//        createSoftLink(textFile);
//        waitAllSynced();
//        log.info(formatLog("FINISH SYNCING SOFT LINKS"));
//
//        log.info(formatLog("START SYNCING HARD LINKS"));
//        createHardLink(textFile);
//        waitAllSynced();
//        log.info(formatLog("FINISH SYNCING HARD LINKS"));
    }


//    @Test
//    void testSyncLinks() throws IOException {
//        File textFile = createTempBinaryFile(1024 * 1024);
//        File softLink = createSoftLink(textFile);
//        File hardLink = createHardLink(textFile);
//
//        log.info("START SYNCING LINKS TEST");
//        Config config = new Config("http://localhost:8888/webdav/",
//                "user",
//                "user",
//                testDirectory,
//                "aurora");
//        configController.setConfig(config);
//        waitAllSynced();
//        log.info("FINISH SYNCING LINKS TEST");
//        // 软硬链接不应当出现在 WebDAV 中
//        assertFalse(webDAVClient.exists(softLink.getName()));
//        assertFalse(webDAVClient.exists(hardLink.getName()));
//        // 软硬链接应当出现在数据库中
//        FileInfo softLinkInfo = fileInfoMapper.selectByParentAndName("", softLink.getName());
//        assertNotNull(softLinkInfo);
//        assertEquals(FileInfo.LinkType.SoftLink, softLinkInfo.getLinkType());
//        assertEquals(FileInfo.SyncStatus.Synced, softLinkInfo.getStatus());
//
//        FileInfo hardLinkInfo = fileInfoMapper.selectByParentAndName("", softLink.getName());
//        assertNotNull(hardLinkInfo);
//        assertEquals(FileInfo.LinkType.HardLink, hardLinkInfo.getLinkType());
//        assertEquals(FileInfo.SyncStatus.Synced, hardLinkInfo.getStatus());
//    }
}
