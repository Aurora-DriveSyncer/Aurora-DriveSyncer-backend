package com.aurora.drivesyncer.worker;

import com.aurora.drivesyncer.entity.Config;
import com.aurora.drivesyncer.mapper.FileInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

public class SyncWorker implements Runnable {
    // 扫描数据库的间隔
    final int WATCH_LOOP = 5;
    final FileInfoMapper fileInfoMapper;
    final Config config;
    Thread t;


    SyncWorker(Config config, FileInfoMapper fileInfoMapper) {
        this.config = config;
        this.fileInfoMapper = fileInfoMapper;
    }

    @Override
    public void run() {
//        int id = fileInfoMapper.selectFirstWaitingFile();
    }

    public void start() {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }

}
