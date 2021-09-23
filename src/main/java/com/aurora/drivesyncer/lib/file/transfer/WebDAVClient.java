package com.aurora.drivesyncer.lib.file.transfer;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.aurora.drivesyncer.lib.Utils.appendSlashIfMissing;
import static com.aurora.drivesyncer.lib.Utils.removePrependingSlash;

public class WebdavClient implements FileTransferClient {

    // url like http://localhost:8888/webdav/
    private final String url;
    private final String user;
    private final String password;
    // https://github.com/lookfirst/sardine/wiki/UsageGuide
    private final Sardine sardine;

    public WebdavClient(String url, String user, String password) {
        this.url =  appendSlashIfMissing(url);
        this.user = user;
        this.password = password;
        this.sardine = SardineFactory.begin(user, password);
    }

    // webdav 不需要 open
    public void open() throws IOException {
    }

    // webdav 不需要 close
    @Override
    public void close() throws IOException {
    }

    private String pathToURL(String path) {
        return this.url + removePrependingSlash(path);
    }

    public List<String> listFiles(String path) throws IOException {
        List<DavResource> resources = sardine.list(pathToURL(path));
        resources.remove(0); // remove '.'
        return resources
                .stream()
                .map(DavResource::getName)
                .collect(Collectors.toList());
    }

    public void uploadFile(String path, InputStream inputStream) throws IOException {
        sardine.put(pathToURL(path), inputStream);
    }

    public void uploadFile(String path, File file) throws IOException {
        uploadFile(path, new FileInputStream(file));
    }

    public void downloadFile(String path, OutputStream outputStream) throws IOException {
        InputStream inputStream = sardine.get(pathToURL(path));
        byte[] data = inputStream.readAllBytes();
        inputStream.close();
        outputStream.write(data);
    }

    public void downloadFile(String path, File file) throws IOException {
        downloadFile(path, new FileOutputStream(file));
    }

    public void deleteFile(String path) throws IOException {
        System.out.println("Deleting " + pathToURL(path));
        sardine.delete(pathToURL(path));
    }

    public void createDirectory(String path) throws IOException {
        sardine.createDirectory(pathToURL(path));
    }

    public void moveFile(String srcPath, String destPath) throws IOException {
        sardine.move(pathToURL(srcPath), pathToURL(destPath));
    }

    public void copyFile(String srcPath, String destPath) throws IOException {
        sardine.copy(pathToURL(srcPath), pathToURL(destPath));
    }

    public boolean exists(String path)throws IOException {
        return sardine.exists(pathToURL(path));
    }
}