package com.aurora.drivesyncer.service;

import com.aurora.drivesyncer.entity.Config;
import com.aurora.drivesyncer.entity.FileInfo;
import com.aurora.drivesyncer.mapper.FileInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class SyncService {
    @Autowired
    private Config config;
    @Autowired
    private FileInfoMapper fileInfoMapper;

    private BlockingQueue<Integer> fileUploadQueue;
    private BlockingQueue<String> fileDeleteQueue;

    // 初始化同步服务
    public void initialize() throws IOException {
        // todo
        // 创建消息队列
        createBlockingQueues();
        // 清理数据库
        // 尝试连接

        // 扫描文件夹

        // 创建 SyncWorker 进程

        // 监听文件变化

    }

    // 清理同步服务
    public void close() throws IOException {
        // todo
        // 清理 SyncWorker 进程

        // 清理消息队列

        // 清理数据库
        fileInfoMapper.delete(null);
        // 清理 ftp 的文件

    }

    // 将发生了添加或更新的本地文件添加至数据库和队列
    public void addLocalFile(File file) throws IOException {
        FileInfo fileInfo = new FileInfo(file);
        fileInfo.setStatus(FileInfo.SyncStatus.Waiting);
        fileInfoMapper.insertOrUpdateByPathAndName(fileInfo);
        fileUploadQueue.add(fileInfo.getId());
    }

    // 将发生了删除的本地文件从数据库删除，并添加至队列
    public void deleteLocalFile(File file) throws IOException {
        fileInfoMapper.deleteByPathAndName(file.getParent(), file.getName());
        fileDeleteQueue.add(file.getPath());
    }

    public void createBlockingQueues() {
        fileUploadQueue = new LinkedBlockingQueue<>();
        fileDeleteQueue = new LinkedBlockingQueue<>();
    }
}
