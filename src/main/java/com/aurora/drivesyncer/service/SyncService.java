package com.aurora.drivesyncer.service;

import com.aurora.drivesyncer.entity.Config;
import com.aurora.drivesyncer.entity.FileInfo;
import com.aurora.drivesyncer.mapper.FileInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

@Service
public class SyncService {
    @Autowired
    private Config config;
    @Autowired
    private FileInfoMapper fileInfoMapper;

    BlockingQueue<Integer> fileUploadQueue;
    BlockingQueue<String> fileDeleteQueue;

    // 初始化同步服务
    public void initialize() throws IOException {
        // todo
        // 清理数据库
        fileInfoMapper.delete(null);

        // 尝试连接

        // 扫描文件夹

        // 创建 SyncWorker 进程

        // 监听文件变化

    }

    // 清理同步服务
    public void close() throws IOException {
        // todo
        // 清理 SyncWorker 进程

        // 清理数据库
        fileInfoMapper.delete(null);

        // 清理 ftp 的文件

    }

    // 将本地文件添加至数据库和队列
    public void addLocalFile(File file) throws IOException {
        FileInfo fileInfo = new FileInfo(file);
        fileInfo.setStatus(FileInfo.SyncStatus.Waiting);
        fileInfoMapper.insert(fileInfo);
        // todo: insert or update ?
        fileUploadQueue.add(fileInfo.getId());
    }

    public void deleteLocalFile(File file) throws IOException {
        fileInfoMapper.delete();
    }
}
