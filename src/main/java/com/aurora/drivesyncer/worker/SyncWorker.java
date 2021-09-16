package com.aurora.drivesyncer.worker;

import com.aurora.drivesyncer.entity.Config;
import com.aurora.drivesyncer.entity.FileInfo;
import com.aurora.drivesyncer.mapper.FileInfoMapper;

import java.util.concurrent.BlockingQueue;

// 消费者，从 waitingFileQueue 中取出等待上传的文件，然后进行上传
public class SyncWorker implements Runnable {
    Config config;
    FileInfoMapper fileInfoMapper;
    BlockingQueue<Integer> waitingFileQueue;
    Thread thread;

    SyncWorker(Config config, FileInfoMapper fileInfoMapper, BlockingQueue<Integer> waitingFileQueue) {
        this.config = config;
        this.fileInfoMapper = fileInfoMapper;
        this.waitingFileQueue = waitingFileQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // 从 waitingFileQueue 中取出等待上传的文件（阻塞操作）
                Integer fileId = waitingFileQueue.take();
                // 更新数据库 status 字段
                fileInfoMapper.updateStatusById(fileId, FileInfo.SyncStatus.Syncing);
                // 对文件压缩

                // 对文件加密

                // 上传文件

                // 上传完成

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

}
