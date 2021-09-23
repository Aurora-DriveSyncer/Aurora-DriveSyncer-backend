package com.aurora.drivesyncer.lib.file.transfer;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ApacheCommonFTPClient implements FileTransferClient {

    private final String url;
    private final int port;
    private final String user;
    private final String password;
    private FTPClient ftp;

    public ApacheCommonFTPClient(String url, String user, String password) {
        this(url, 21, user, password);
    }
    public ApacheCommonFTPClient(String url, int port, String user, String password) {
        this.url = url;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public void open() throws IOException {
        ftp = new org.apache.commons.net.ftp.FTPClient();

        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        ftp.connect(url, port);
//        int reply = ftp.getReplyCode();
//        if (!FTPReply.isPositiveCompletion(reply)) {
//            ftp.disconnect();
//            throw new IOException("Exception in connecting to FTP Server");
//        }

        ftp.login(user, password);
    }

    @Override
    public void close() throws IOException {
        ftp.disconnect();
    }

    public List<String> listFiles(String path) throws IOException {
        FTPFile[] files = ftp.listFiles(path);
        return Arrays.stream(files)
                .map(FTPFile::getName)
                .collect(Collectors.toList());
    }

    public void uploadFile(String path, InputStream inputStream) throws IOException {
        ftp.enterLocalPassiveMode();
        if (!ftp.storeFile(path, inputStream)) {
            throw new IOException();
        }
    }

    public void uploadFile(String path, File file) throws IOException {
        uploadFile(path, new FileInputStream(file));
    }

    public void downloadFile(String path, OutputStream outputStream) throws IOException {
        ftp.enterLocalPassiveMode();
        if (!ftp.retrieveFile(path, outputStream)) {
            throw new IOException();
        }
        ftp.enterLocalActiveMode();
    }

    public void downloadFile(String path, File file) throws IOException {
        downloadFile(path, new FileOutputStream(file));
    }

    public void deleteFile(String path) throws IOException {
        ftp.deleteFile(path);
    }
}