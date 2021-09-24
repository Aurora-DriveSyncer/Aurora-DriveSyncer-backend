package com.aurora.drivesyncer.lib.file.transfer;

import java.io.*;
import java.util.List;

public interface FileTransferClient extends Closeable {
    void open() throws IOException;

    @Override
    void close() throws IOException;

    void testConnection() throws IOException;

    List<String> listFiles(String path) throws IOException;

    void uploadFile(String path, InputStream inputStream) throws IOException;

    default void uploadFile(String path, File file) throws IOException {
        uploadFile(path, new FileInputStream(file));
    }

    void downloadFile(String path, OutputStream outputStream) throws IOException;

    default void downloadFile(String path, File file) throws IOException {
        downloadFile(path, new FileOutputStream(file));
    }

    void deleteFile(String path) throws IOException;
}
