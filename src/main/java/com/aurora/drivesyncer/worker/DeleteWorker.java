package com.aurora.drivesyncer.worker;

import com.aurora.drivesyncer.entity.Config;
import com.aurora.drivesyncer.lib.file.transfer.FileTransferClient;
import com.aurora.drivesyncer.lib.file.transfer.WebDAVClient;
import com.aurora.drivesyncer.mapper.FileInfoMapper;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

// 消费者，从 fileDeleteQueue 中取出等待删除的文件，然后进行删除
public class DeleteWorker extends Worker {
    Config config;

    FileTransferClient fileTransferClient;

    FileInfoMapper fileInfoMapper;
    BlockingQueue<String> fileDeleteQueue;

    public DeleteWorker(Config config, FileInfoMapper fileInfoMapper, BlockingQueue<String> fileDeleteQueue) {
        this.config = config;
        this.fileInfoMapper = fileInfoMapper;
        this.fileDeleteQueue = fileDeleteQueue;
        this.fileTransferClient = new WebDAVClient(config.getUrl(), config.getUsername(), config.getPassword());
    }

    @Override
    public void run() {
        try {
            fileTransferClient.open();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        while (true) {
            try {
                // 从 waitingFileQueue 中取出等待上传的文件（阻塞操作）
                String filepath = fileDeleteQueue.take();
                deleteFile(filepath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteFile(String filepath) throws IOException {
        fileTransferClient.deleteFile(filepath);
        log.info("Deleting " + filepath + "from FTP Server");
    }
}
