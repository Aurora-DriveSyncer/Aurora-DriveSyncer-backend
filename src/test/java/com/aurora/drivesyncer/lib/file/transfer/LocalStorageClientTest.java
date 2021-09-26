package com.aurora.drivesyncer.lib.file.transfer;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.aurora.drivesyncer.lib.file.FileTestTemplate.readSampleTextFile;
import static com.aurora.drivesyncer.lib.file.FileTestTemplate.testDirectory;
import static org.junit.jupiter.api.Assertions.*;

public class LocalStorageClientTest {
    final String root = testDirectory;
    LocalStorageClient localStorageClient = new LocalStorageClient(root);
    
    @BeforeEach
    public void setup() throws IOException {
        localStorageClient.testConnection();
        try {
            localStorageClient.deleteFile("");
        } catch (IOException e) {
        }
        localStorageClient.createDirectory("");
    }

    @AfterEach
    public void teardown() throws IOException {
        localStorageClient.deleteFile("");
    }

    @Test
    public void integrationTest() throws IOException {
        // list
        List<String> fileList = localStorageClient.listFiles("");
        assertEquals(0, fileList.size());
        // create directory
        localStorageClient.createDirectory("test");
        // upload to root
        File file = readSampleTextFile();
        localStorageClient.putFile("pom.xml", file);
        // upload to subdirectory
        localStorageClient.putFile("test/pom.xml", file);
        // list
        fileList = localStorageClient.listFiles("");
        assertEquals(2, fileList.size());
        assertTrue(fileList.contains("test"));
        assertTrue(fileList.contains("pom.xml"));
        // download
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        localStorageClient.getFile("pom.xml", baos);
        assertArrayEquals(FileUtils.readFileToByteArray(file), baos.toByteArray());
        baos = new ByteArrayOutputStream();
        localStorageClient.getFile("test/pom.xml", baos);
        assertArrayEquals(FileUtils.readFileToByteArray(file), baos.toByteArray());
        // delete
        localStorageClient.deleteFile("pom.xml");
        localStorageClient.deleteFile("*");
        // 重复删除不会报错
        localStorageClient.deleteFile("pom.xml");
        localStorageClient.deleteFile("*");
        // list
        fileList = localStorageClient.listFiles("");
        assertEquals(0, fileList.size());
    }

    @Test
    public void testCreateDirectoryRepeatedly() throws IOException {
        String directory2 = "b/c/d/e/f";
        assertFalse(localStorageClient.exists(directory2));
        for (int i = 0; i < 10; i++) {
            localStorageClient.createDirectoryRecursively(directory2);
        }
        assertTrue(localStorageClient.exists(directory2));
    }
}