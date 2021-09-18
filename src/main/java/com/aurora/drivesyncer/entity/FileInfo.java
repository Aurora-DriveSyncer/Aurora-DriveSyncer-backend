package com.aurora.drivesyncer.entity;

import com.aurora.drivesyncer.lib.file.hash.Hash;
import com.aurora.drivesyncer.lib.file.hash.SpringMD5;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class FileInfo {
    public enum SyncStatus
    {
        Waiting, Syncing, Synced
    }
    static public Hash hashAlgorithm = new SpringMD5();

    private Integer id;
    private String filename;
    private String path;
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
        BasicFileAttributes attr = Files.readAttributes(Path.of(this.path), BasicFileAttributes.class);
        this.creationTime = attr.creationTime().toString();
        this.lastAccessTime = attr.lastAccessTime().toString();
        this.lastModifiedTime = attr.lastModifiedTime().toString();
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
