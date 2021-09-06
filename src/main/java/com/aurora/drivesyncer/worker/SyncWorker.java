package com.aurora.drivesyncer.worker;

import com.aurora.drivesyncer.entity.Config;
import com.aurora.drivesyncer.mapper.FileInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

public class SyncWorker implements Runnable {

    private FileInfoMapper fileInfoMapper;
    private Config config;
    private Thread t;

    SyncWorker(Config config, FileInfoMapper fileInfoMapper) {
        this.config = config;
        this.fileInfoMapper = fileInfoMapper;
    }

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
