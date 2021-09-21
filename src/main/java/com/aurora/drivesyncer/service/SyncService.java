package com.aurora.drivesyncer.service;

import com.aurora.drivesyncer.entity.Config;
import com.aurora.drivesyncer.entity.FileInfo;
import com.aurora.drivesyncer.lib.ftp.FtpClient;
import com.aurora.drivesyncer.mapper.FileInfoMapper;
import com.aurora.drivesyncer.worker.DeleteWorker;
import com.aurora.drivesyncer.worker.FileMonitor;
import com.aurora.drivesyncer.worker.UploadWorker;
import com.aurora.drivesyncer.worker.Worker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class SyncService {
    @Autowired
    private Config config;
    @Autowired
    private FileInfoMapper fileInfoMapper;
    @Value("${aurora.worker.upload}")
    private int uploadWorkerCount;
    @Value("${aurora.worker.delete}")
    private int deleteWorkerCount;

    private BlockingQueue<Integer> fileUploadQueue;
    private BlockingQueue<String> fileDeleteQueue;
    private List<Worker> workers;
    Log log = LogFactory.getLog(getClass());

    // 初始化同步服务
    public void open() throws IOException {
        // 判断文件夹是否存在
        File root = new File(config.getLocalPath());
        if (!root.exists()) {
            throw new FileNotFoundException(root.getPath());
        }
        // 尝试连接
        FtpClient ftpClient = new FtpClient(config.getUrl(), config.getUsername(), config.getPassword());
        ftpClient.open();
        ftpClient.close();
        // 创建消息队列
        createBlockingQueues();
        // 清理数据库
        fileInfoMapper.delete(null);
        // 创建 Worker 线程
        Worker fileMonitor = new FileMonitor(config.getLocalPath(), this);
        workers.add(fileMonitor);
        fileMonitor.start();
        for (int i = 0; i < uploadWorkerCount; i++) {
            Worker worker = new UploadWorker(config, fileInfoMapper, fileUploadQueue);
            workers.add(worker);
            worker.start();
        }
        for (int i = 0; i < deleteWorkerCount; i++) {
            Worker worker = new DeleteWorker(config, fileInfoMapper, fileDeleteQueue);
            workers.add(worker);
            worker.start();
        }
    }

    // 清理同步服务
    public void close() throws IOException {
        // 倒序清理 SyncWorker 线程
        for (int i = workers.size() - 1; i >= 0; i--) {
            workers.get(i).interrupt();
        }
        workers.clear();
        // 清理数据库
        fileInfoMapper.delete(null);
        // 清理 ftp 的文件
        FtpClient ftpClient = new FtpClient(config.getUrl(), config.getUsername(), config.getPassword());
        ftpClient.open();
        ftpClient.deleteFile(".");
        ftpClient.close();
    }

    // 将发生了添加或更新的本地文件添加至数据库和队列
    public void addLocalFile(File file) throws IOException {
        FileInfo fileInfo = new FileInfo(file);
        fileInfo.setStatus(FileInfo.SyncStatus.Waiting);
        fileInfoMapper.insertOrUpdateByPathAndName(fileInfo);
        log.info("Update " + file.getPath() + " from database");
        fileUploadQueue.add(fileInfo.getId());
    }

    // 将发生了删除的本地文件从数据库删除，并添加至队列
    public void deleteLocalFile(File file) throws IOException {
        fileInfoMapper.deleteByPathAndName(file.getParent(), file.getName());
        log.info("Delete " + file.getPath() + " from database");
        fileDeleteQueue.add(file.getPath());
    }

    public void createBlockingQueues() {
        fileUploadQueue = new LinkedBlockingQueue<>();
        fileDeleteQueue = new LinkedBlockingQueue<>();
    }
}
