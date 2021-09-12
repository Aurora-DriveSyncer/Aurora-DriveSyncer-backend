package com.aurora.drivesyncer.service;

import com.aurora.drivesyncer.entity.Config;
import com.aurora.drivesyncer.mapper.FileInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SyncService {
    @Autowired
    private Config config;
    @Autowired
    private FileInfoMapper fileInfoMapper;


    void initialize() throws IOException {
        // 清理数据库
        fileInfoMapper.delete(null);

        // 尝试连接

        // 扫描文件夹

        // 创建 SyncWorker 进程

        // 监听文件变化

    }

    void close() throws IOException {
        // 清理 SyncWorker 进程

        // 清理数据库
        fileInfoMapper.delete(null);

        // 清理 ftp 的文件

    }
}
