package com.aurora.drivesyncer.lib.file.transfer;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static com.aurora.drivesyncer.utils.FileTests.readSampleTextFile;
import static org.junit.jupiter.api.Assertions.*;

class WebdavClientTest {
    WebdavClient webdavClient;

    @BeforeEach
    public void setup() {
        try {
            // 运行这个测试需要用 docker 等方法部署 webdav 服务
            // 且保证根目录为空
            webdavClient = new WebdavClient("http://localhost:8888/webdav/", "user", "user");
            webdavClient.deleteFile(".");   // 可以强制根目录所有文件
            Collection<String> fileList = webdavClient.listFiles("/");
            Assumptions.assumeTrue(fileList.size() == 0);
        } catch (IOException e) {
            Assumptions.assumeTrue(false);
        }
    }

    @AfterEach
    public void teardown() throws IOException {
        webdavClient.deleteFile(".");
    }

    @Test
    public void integrationTest() throws IOException {
        // list
        List<String> fileList = webdavClient.listFiles(".");
        assertEquals(0, fileList.size());
        // create directory
        webdavClient.createDirectory("test");
        // upload to root
        File file = readSampleTextFile();
        webdavClient.uploadFile("pom.xml", file);
        // upload to subdirectory
        webdavClient.uploadFile("test/pom.xml", file);
        // list
        fileList = webdavClient.listFiles(".");
        assertEquals(2, fileList.size());
        assertTrue(fileList.contains("test"));
        assertTrue(fileList.contains("pom.xml"));
        // download
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        webdavClient.downloadFile("pom.xml", byteArrayOutputStream);
        assertArrayEquals(FileUtils.readFileToByteArray(file), byteArrayOutputStream.toByteArray());
        byteArrayOutputStream = new ByteArrayOutputStream();
        webdavClient.downloadFile("test/pom.xml", byteArrayOutputStream);
        assertArrayEquals(FileUtils.readFileToByteArray(file), byteArrayOutputStream.toByteArray());
        // delete
        webdavClient.deleteFile("pom.xml");
        webdavClient.deleteFile("/");
        // delete again will return 404
        assertThrows(IOException.class, () -> {
            webdavClient.deleteFile("pom.xml");
        });
        // list
        fileList = webdavClient.listFiles(".");
        assertEquals(0, fileList.size());
    }
}