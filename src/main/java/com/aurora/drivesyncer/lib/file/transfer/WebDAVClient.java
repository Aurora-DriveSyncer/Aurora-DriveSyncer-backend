package com.aurora.drivesyncer.lib.file.transfer;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

import static com.aurora.drivesyncer.lib.Utils.appendSlashIfMissing;
import static com.aurora.drivesyncer.lib.Utils.removePrependingSlash;

public class WebDAVClient implements FileTransferClient {

    // 保证以 / 结尾，类似于 http://localhost:8888/webdav/
    private final String url;
    private final String user;
    private final String password;
    // 参考：https://github.com/lookfirst/sardine/wiki/UsageGuide
    private final Sardine sardine;
    Log log = LogFactory.getLog(getClass());

    public WebDAVClient(String url, String user, String password) {
        this.url =  appendSlashIfMissing(url);
        this.user = user;
        this.password = password;
        this.sardine = SardineFactory.begin(user, password);
    }

    // WebDAV 不需要 open
    @Override
    public void open() throws IOException {
    }

    // WebDAV 不需要 close
    @Override
    public void close() throws IOException {
    }

    @Override
    public void testConnection() throws IOException {
        listFiles("");
    }

    private String pathToURL(String path) {
        return this.url + removePrependingSlash(path);
    }

    @Override
    public List<String> listFiles(String path) throws IOException {
        List<DavResource> resources = sardine.list(pathToURL(path));
        resources.remove(0); // remove '.'
        return resources
                .stream()
                .map(DavResource::getName)
                .collect(Collectors.toList());
    }

    public void uploadFile(String path, InputStream inputStream) throws IOException {
        createDirectoryRecursively(new File(path).getParent());
        sardine.put(pathToURL(path), inputStream);
    }

    public void downloadFile(String path, OutputStream outputStream) throws IOException {
        InputStream inputStream = sardine.get(pathToURL(path));
        byte[] data = inputStream.readAllBytes();
        inputStream.close();
        outputStream.write(data);
    }

    @Override
    public void deleteFile(String path) throws IOException {
        sardine.delete(pathToURL(path));
    }

    public void createDirectory(String path) throws IOException {
        log.info("Creating directory " + path);
        sardine.createDirectory(pathToURL(path));
    }

    public void createDirectoryRecursively(String path) throws IOException {
        // 由于大部分时候根本不需要创建文件夹，所以先判定是否存在，再开始逐级创建
        if (path == null || exists(path)) {
            return;
        }
        createDirectoryRecursively(new File(path).getParent());
        // 高并发下会出现重复创建文件夹的 Exception，但不影响结果
        try {
            createDirectory(path);
        } catch (IOException ignored) {
        }
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