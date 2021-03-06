package com.aurora.drivesyncer.entity;

import com.aurora.drivesyncer.lib.file.FileUtils;
import com.aurora.drivesyncer.lib.file.hash.Hash;
import com.aurora.drivesyncer.lib.file.hash.SpringMD5;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import static com.aurora.drivesyncer.lib.datetime.DateTimeUtils.toLocalTime;
import static com.aurora.drivesyncer.lib.file.FileUtils.formatDirPath;

public class FileInfo {
    public enum SyncStatus
    {
        Waiting,    // 等待同步
        Syncing,    // 同步中（包括上传和删除）
        Synced      // 已同步
    }
//    public enum LinkType
//    {
//        HardLink,   // 硬链接
//        SoftLink,   // 软链接
//        RegularFile // 常规文件
//    }

    public static Hash hashAlgorithm = new SpringMD5();

    public static BasicFileAttributes getAttribute(File file) throws IOException {
        return Files.readAttributes(Path.of(file.getPath()), BasicFileAttributes.class);
    }

    @TableId(type = IdType.AUTO)
    private Integer id;
    // 文件名，不含路径
    private String filename;
    // 文件所在文件夹的相对路径（以同步目录为根目录），保证以 / 结尾
    // 为保证 API 不变，沿用 path 的命名
    private String path;
    // 格式为 YYYY-MM-DD yyyy-MM-dd HH:mm:ss
    private String creationTime;
    private String lastAccessTime;
    private String lastModifiedTime;
    private Boolean isDirectory;
//    private LinkType linkType;
    private Long size;
    private String hash;
    private SyncStatus status;

    public FileInfo() {

    }

    public FileInfo(File file, String base) throws IOException {
        this.filename = file.getName();
        String relativePath = FileUtils.getRelativePath(file.getParent(), base);
        // setPath 会将路径名规范化
        this.setPath(relativePath);
        BasicFileAttributes attr = getAttribute(file);
        this.creationTime = toLocalTime(attr.creationTime());
        this.lastAccessTime = toLocalTime(attr.lastAccessTime());
        this.lastModifiedTime = toLocalTime(attr.lastModifiedTime());
        this.isDirectory = file.isDirectory();
        this.size = file.length();
        if (!this.isDirectory) {
            this.hash = hashAlgorithm.hash(file);
        }
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

    // 保证 parent 不以 / 开头
    // 且保证 parent 不为 "" 时，以 / 结尾
    public void setPath(String path) {
        this.path = formatDirPath(path);
    }

    public String getFullPath() {return path + filename;}

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

//    public LinkType getLinkType() {
//        return linkType;
//    }
//
//    public void setLinkType(LinkType linkType) {
//        this.linkType = linkType;
//    }

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
