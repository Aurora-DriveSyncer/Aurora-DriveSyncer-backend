package com.aurora.drivesyncer.lib.file.transfer;

import java.io.*;
import java.util.List;

public interface FileTransferClient extends Closeable {
    void open() throws IOException;

    @Override
    void close() throws IOException;

    void testConnection() throws IOException;

    List<String> listFiles(String path) throws IOException;

    void putFile(String path, InputStream inputStream) throws IOException;

    default void putFile(String path, File file) throws IOException {
        putFile(path, new FileInputStream(file));
    }

    void getFile(String path, OutputStream outputStream) throws IOException;

    default void getFile(String path, File file) throws IOException {
        getFile(path, new FileOutputStream(file));
    }

    default byte[] getFileToByteArray(String path) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        getFile(path, baos);
        return baos.toByteArray();
    }

    void deleteFile(String path) throws IOException;
}
