package com.aurora.drivesyncer.lib.file.transfer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static com.aurora.drivesyncer.lib.file.FileTestTemplate.readSampleTextFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApacheCommonFTPClientTest {
    private FakeFtpServer fakeFtpServer;

    private ApacheCommonFTPClient ftpClient;

    @BeforeEach
    public void setupEach() throws IOException {
        fakeFtpServer = new FakeFtpServer();
        fakeFtpServer.addUserAccount(new UserAccount("user", "password", "/data"));

        FileSystem fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new DirectoryEntry("/data"));
        fileSystem.add(new FileEntry("/data/foobar.txt", "abcdef 1234567890"));
        fakeFtpServer.setFileSystem(fileSystem);
        fakeFtpServer.setServerControlPort(0);
        fakeFtpServer.start();
        int port = fakeFtpServer.getServerControlPort();

        ftpClient = new ApacheCommonFTPClient("localhost", port, "user", "password");
        ftpClient.open();
    }

    @AfterEach
    public void teardownEach() throws IOException {
        ftpClient.close();
        fakeFtpServer.stop();
    }

    @Test
    public void listFiles() throws IOException {
        Collection<String> files = ftpClient.listFiles("");
        assertTrue(files.contains("foobar.txt"));
    }

    @Test
    public void downloadFile() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ftpClient.getFile("/data/foobar.txt",byteArrayOutputStream);
        assertTrue(byteArrayOutputStream.size() > 0);
        assertEquals("abcdef 1234567890", byteArrayOutputStream.toString());
    }

    @Test
    public void uploadFile() throws  IOException {

        File file = readSampleTextFile();
        ftpClient.putFile("/buz.txt",file);
        assertTrue(fakeFtpServer.getFileSystem().exists("/buz.txt"));
    }
}