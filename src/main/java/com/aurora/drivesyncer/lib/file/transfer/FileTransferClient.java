package com.aurora.drivesyncer.lib.file.transfer;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileTransferClient extends Closeable {
    public void open() throws IOException;

    @Override
    public void close() throws IOException;

    public List<String> listFiles(String path) throws IOException;

    public void uploadFile(String path, File file) throws IOException;

    public void downloadFile(String path, File file) throws IOException;

    public void deleteFile(String path) throws IOException;
}
