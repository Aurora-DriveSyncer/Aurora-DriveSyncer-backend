package com.aurora.drivesyncer.entity;

import com.aurora.drivesyncer.lib.file.hash.Hash;
import com.aurora.drivesyncer.lib.file.hash.SpringMD5;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import static com.aurora.drivesyncer.lib.datetime.DateTime.toLocalTime;

public class FileInfo {
    public enum SyncStatus
    {
        Waiting, // 等待同步
        Syncing, // 同步中（包括上传和删除）
        Synced   // 已同步
    }

    static public Hash hashAlgorithm = new SpringMD5();

    static public BasicFileAttributes getAttribute(File file) throws IOException {
        return Files.readAttributes(Path.of(file.getPath()), BasicFileAttributes.class);
    }

    private Integer id;
    // 文件名，不含路径
    private String filename;
    // 相对路径，以 / 结尾
    private String path;
    // 格式为 YYYY-MM-DD
    private String creationTime;
    private String lastAccessTime;
    private String lastModifiedTime;
    private Boolean isDirectory;
    private Long size;
    private String hash;
    private SyncStatus status;

    public FileInfo() {

    }

    public FileInfo(File file) throws IOException {
        this.filename = file.getName();
        this.path = file.getParent();
        BasicFileAttributes attr = getAttribute(file);
        this.creationTime = toLocalTime(attr.creationTime());
        this.lastAccessTime = toLocalTime(attr.lastAccessTime());
        this.lastModifiedTime = toLocalTime(attr.lastModifiedTime());
        this.isDirectory = file.isDirectory();
        this.size = file.length();
        this.hash = hashAlgorithm.hash(file);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(String lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public String getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(String lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public Boolean getDirectory() {
        return isDirectory;
    }

    public void setDirectory(Boolean directory) {
        isDirectory = directory;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public SyncStatus getStatus() {
        return status;
    }

    public void setStatus(SyncStatus status) {
        this.status = status;
    }
}
