package com.aurora.drivesyncer.service;

import com.aurora.drivesyncer.entity.Config;
import com.aurora.drivesyncer.entity.FileInfo;
import com.aurora.drivesyncer.lib.file.FileUtils;
import com.aurora.drivesyncer.lib.file.transfer.FileTransferClient;
import com.aurora.drivesyncer.lib.file.transfer.WebDAVClient;
import com.aurora.drivesyncer.lib.log.LogUtils;
import com.aurora.drivesyncer.mapper.FileInfoMapper;
import com.aurora.drivesyncer.worker.*;
import com.sun.istack.NotNull;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class SyncService {
    @Autowired
    private FileInfoMapper fileInfoMapper;
    @Value("${aurora.worker.upload}")
    private int uploadWorkerCount;
    @Value("${aurora.worker.delete}")
    private int deleteWorkerCount;

    private Config config;
    private BlockingQueue<Integer> fileUploadQueue;
    private BlockingQueue<String> fileDeleteQueue;
    private final List<Worker> workers = new ArrayList<>();
    private DownloadWorker downloadWorker;
    Log log = LogFactory.getLog(getClass());


    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    @NotNull
    public DownloadWorker getDownloadWorker() throws IOException {
        if (downloadWorker == null) {
            throw new IOException();
        }
        return downloadWorker;
    }

    // 初始化同步服务
    public void open() throws IOException {
        log.info("Initializing SyncService");
        if (config == null) {
            return;
        }
        // 判断文件夹是否存在
        File root = new File(config.getLocalPath());
        if (!root.exists() || !root.isDirectory()) {
            throw new IOException();
        }
        // 清理文件服务器的旧文件
        log.info("Cleaning old files on file server");
        FileTransferClient fileTransferClient
                = new WebDAVClient(config.getUrl(), config.getUsername(), config.getPassword());
        fileTransferClient.open();
        fileTransferClient.deleteFile(".");
        fileTransferClient.close();
        // 创建消息队列
        log.info("Creating message queues");
        createBlockingQueues();
        // 清理数据库
        log.info("Clearing database");
        fileInfoMapper.delete(null);
        // 创建 FileMonitor 线程
        log.info("Creating 1 FileMonitor");
        Worker fileMonitor = new FileMonitor(config.getLocalPath(), this);
        workers.add(fileMonitor);
        fileMonitor.start(1);
        // 创建 UploadWorker 线程
        log.info("Creating " + uploadWorkerCount + " UploadWorker" + LogUtils.prependingS(uploadWorkerCount));
        for (int i = 0; i < uploadWorkerCount; i++) {
            Worker worker = new UploadWorker(config, fileInfoMapper, fileUploadQueue);
            workers.add(worker);
            worker.start(i);
        }
        // 创建 DeleteWorker 线程
        log.info("Creating " + deleteWorkerCount + " DeleteWorker" + LogUtils.prependingS(deleteWorkerCount));
        for (int i = 0; i < deleteWorkerCount; i++) {
            Worker worker = new DeleteWorker(config, fileInfoMapper, fileDeleteQueue);
            workers.add(worker);
            worker.start(i);
        }
        // 创建 DownloadWorker 提供下载服务
        log.info("Creating DownloadWorker");
        downloadWorker = new DownloadWorker(config, fileInfoMapper);
    }

    // 清理同步服务
    public void close() throws IOException {
        log.info("Cleaning SyncService");
        if (config == null) {
            log.info("Skipping cleaning SyncService since config is null");
            return;
        }
        // 倒序清理 SyncWorker 线程
        log.info("Cleaning workers");
        for (int i = workers.size() - 1; i >= 0; i--) {
            workers.get(i).interrupt();
        }
        workers.clear();
        // 清理失效的 DownloadWorker
        downloadWorker = null;
        // 清理数据库
        log.info("Cleaning database");
        fileInfoMapper.delete(null);
        // 清理文件服务器的文件
        log.info("Cleaning files on file server");
        FileTransferClient fileTransferClient
                = new WebDAVClient(config.getUrl(), config.getUsername(), config.getPassword());
        fileTransferClient.open();
        fileTransferClient.deleteFile(".");
        fileTransferClient.close();
    }

    // 将发生了添加或更新的本地文件添加至数据库和队列
    public void addLocalFile(File file) throws IOException {
        // 存入数据库的是相对路径
        FileInfo fileInfo = new FileInfo(file, config.getLocalPath());
        fileInfo.setStatus(FileInfo.SyncStatus.Waiting);
        fileInfoMapper.insertOrUpdateByParentAndName(fileInfo);
        // log 也使用相对路径
        String relativePath = fileInfo.getFullPath();
        log.info("Update " + relativePath + " to database");
        fileUploadQueue.add(fileInfo.getId());
    }

    // 将发生了删除的本地文件从数据库删除，并添加至队列
    public void deleteLocalFile(File file) throws IOException {
        String relativeParent = FileUtils.getRelativePath(file.getParent(), config.getLocalPath()) + "/";
        String relativePath = relativeParent + file.getName();
        fileInfoMapper.deleteByParentAndName(relativeParent, file.getName());
        log.info("Delete " + relativePath + " from database");
        fileDeleteQueue.add(relativePath);
    }

    public void createBlockingQueues() {
        fileUploadQueue = new LinkedBlockingQueue<>();
        fileDeleteQueue = new LinkedBlockingQueue<>();
    }
}
