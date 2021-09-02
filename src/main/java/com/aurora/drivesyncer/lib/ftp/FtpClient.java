package com.aurora.drivesyncer.lib.ftp;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

class FtpClient implements Closeable {

    private final String url;
    private final String user;
    private final String password;
    private FTPClient ftp;

    public FtpClient(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public void open() throws IOException {
        ftp = new FTPClient();

        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        ftp.connect(url);
        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new IOException("Exception in connecting to FTP Server");
        }

        ftp.login(user, password);
    }

    public void close() throws IOException {
        ftp.disconnect();
    }

    List<String> listFiles(String path) throws IOException {
        FTPFile[] files = ftp.listFiles(path);
        return Arrays.stream(files)
                .map(FTPFile::getName)
                .collect(Collectors.toList());
    }

    void downloadFile(File file, String path) throws IOException {
        ftp.retrieveFile(path, new FileOutputStream(file));
    }

    void uploadFile(File file, String path) throws IOException {
        ftp.storeFile(path, new FileInputStream(file));
    }
}