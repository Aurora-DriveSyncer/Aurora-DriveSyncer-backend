package com.aurora.drivesyncer.worker;

import com.aurora.drivesyncer.entity.Config;
import com.aurora.drivesyncer.mapper.FileInfoMapper;

import java.util.concurrent.BlockingQueue;

// 生产者，扫描更新的文件，加入 waitingFileQueue
public class WatchWorker implements Runnable {
    Config config;
    FileInfoMapper fileInfoMapper;
    BlockingQueue<Integer> waitingFileQueue;
    Thread thread;

    WatchWorker(Config config, FileInfoMapper fileInfoMapper, BlockingQueue<Integer> waitingFileQueue) {
        this.config = config;
        this.fileInfoMapper = fileInfoMapper;
        this.waitingFileQueue = waitingFileQueue;
    }

    @Override
    public void run() {
//        int id = fileInfoMapper.selectFirstWaitingFile();
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }
}
