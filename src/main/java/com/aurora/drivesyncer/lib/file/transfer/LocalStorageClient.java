package com.aurora.drivesyncer.lib.file.transfer;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class LocalStorageClient implements FileTransferClient {
    private final String root;

    public LocalStorageClient(String root) {
        this.root = root;
    }

    @Override
    public void open() throws IOException {
        File file = new File(root);
        if (!file.exists() && !file.mkdirs()) {
            throw new IOException("Can't create directory" + root);
        }
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public void testConnection() throws IOException {
        open();
    }

    @Override
    public List<String> listFiles(String path) throws IOException {
        return Stream
                .of(Objects.requireNonNull(new File(root, path).listFiles()))
                .map(File::getName)
                .toList();
    }

    public void putFile(String path, InputStream inputStream) throws IOException {
        createDirectoryRecursively(new File(path).getParent());
        FileOutputStream fileOutputStream = new FileOutputStream(new File(root, path));
        fileOutputStream.write(inputStream.readAllBytes());
    }

    public void getFile(String path, OutputStream outputStream) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(root, path));
        outputStream.write(fileInputStream.readAllBytes());
    }

    @Override
    public void deleteFile(String path) throws IOException {
        File file = new File(root, path);
        if (file.getName().equals("*")) {
            // 可以清空文件价
            FileUtils.cleanDirectory(file.getParentFile());
        } else if (file.isDirectory()) {
            // 也可以删除文件夹
            FileUtils.deleteDirectory(file);
        } else {
            // 也可以删除一个文件
            file.delete();
        }
    }

    public void createDirectory(String path) throws IOException {
        if (!new File(root, path).mkdirs()) {
            throw new IOException("Making directory " + path + " failed");
        }
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
        Files.move(Paths.get(root, srcPath), Paths.get(root, destPath));
    }

    public void copyFile(String srcPath, String destPath) throws IOException {
        Files.copy(Paths.get(root, srcPath), Paths.get(root, destPath));
    }

    public boolean exists(String path)throws IOException {
        return new File(root, path).exists();
    }
}