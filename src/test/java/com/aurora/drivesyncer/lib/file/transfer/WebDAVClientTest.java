package com.aurora.drivesyncer.lib.file.transfer;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.aurora.drivesyncer.lib.file.FileTestTemplate.readSampleTextFile;
import static org.junit.jupiter.api.Assertions.*;

public class WebDAVClientTest {
    // 运行 WebDAV 测试需要用 docker 等方法部署 WebDAV 服务
    public static WebDAVClient webDAVClient;

    // 不同环境下测试可能使用不同的 WebDAV 服务
    private static final WebDAVClient[] webDAVClientCandidates = {
            new WebDAVClient("http://webdav/webdav/", "user", "user"),
            new WebDAVClient("http://localhost:8888/webdav/", "user", "user")
    };

    // 如能连接上部署的 WebDAV 测试服务并删除所有文件，返回 true
    public static boolean initializeServer() {
        // 尝试备选 webDAVClient 直至成功访问
        for (WebDAVClient c : webDAVClientCandidates) {
            try {
                c.deleteFile(".");
                if (c.listFiles("/").size() == 0) {
                    webDAVClient = c;
                    return true;
                }
            } catch (IOException ignored) {
            }
        }
        return false;
    }

    @BeforeEach
    public void setup() {
        Assumptions.assumeTrue(initializeServer());
    }

    @AfterEach
    public void teardown() throws IOException {
        initializeServer();
    }

    @Test
    public void integrationTest() throws IOException {
        // list
        List<String> fileList = webDAVClient.listFiles(".");
        assertEquals(0, fileList.size());
        // create directory
        webDAVClient.createDirectory("test");
        // upload to root
        File file = readSampleTextFile();
        webDAVClient.putFile("pom.xml", file);
        // upload to subdirectory
        webDAVClient.putFile("test/pom.xml", file);
        // list
        fileList = webDAVClient.listFiles(".");
        assertEquals(2, fileList.size());
        assertTrue(fileList.contains("test"));
        assertTrue(fileList.contains("pom.xml"));
        // download
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        webDAVClient.getFile("pom.xml", baos);
        assertArrayEquals(FileUtils.readFileToByteArray(file), baos.toByteArray());
        baos = new ByteArrayOutputStream();
        webDAVClient.getFile("test/pom.xml", baos);
        assertArrayEquals(FileUtils.readFileToByteArray(file), baos.toByteArray());
        // delete
        webDAVClient.deleteFile("pom.xml");
        webDAVClient.deleteFile("/");
        // 重复删除会报错 404
        assertThrows(IOException.class, () -> {
            webDAVClient.deleteFile("pom.xml");
        });
        // list
        fileList = webDAVClient.listFiles(".");
        assertEquals(0, fileList.size());
    }

    @Test
    public void testCreateDirectoryRepeatedly() throws IOException {
        String directory1 = "a/";
        // 重复创建文件夹会报错 405
        assertFalse(webDAVClient.exists(directory1));
        webDAVClient.createDirectory(directory1);
        assertThrows(IOException.class, () -> {
            webDAVClient.createDirectory(directory1);
        });
        assertTrue(webDAVClient.exists(directory1));

        String directory2 = "b/c/d/e/f";
        // 使用 createDirectoryRecursively 则不会报错
        assertFalse(webDAVClient.exists(directory2));
        for (int i = 0; i < 10; i++) {
            webDAVClient.createDirectoryRecursively(directory2);
        }
        assertTrue(webDAVClient.exists(directory2));
    }
}