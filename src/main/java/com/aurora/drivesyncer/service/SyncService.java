package com.aurora.drivesyncer.service;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SyncService {
    void initialize() {
        // 清理数据库
        // 尝试连接
        // 扫描文件夹
        // 创建 SyncWorker 进程
        // 监听文件变化
    }

    void close() throws IOException {
        // 清理 SyncWorker 进程
        // 清理数据库
        // 清理 ftp 的文件
    }
}