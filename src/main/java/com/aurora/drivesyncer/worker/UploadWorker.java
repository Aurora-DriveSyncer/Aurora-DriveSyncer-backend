package com.aurora.drivesyncer.worker;

import com.aurora.drivesyncer.entity.Config;
import com.aurora.drivesyncer.entity.FileInfo;
import com.aurora.drivesyncer.lib.file.compress.Compressor;
import com.aurora.drivesyncer.lib.file.compress.GzipCompressor;
import com.aurora.drivesyncer.lib.file.encrypt.AuroraEncryptor;
import com.aurora.drivesyncer.lib.file.encrypt.Encryptor;
import com.aurora.drivesyncer.lib.file.transfer.FileTransferClient;
import com.aurora.drivesyncer.lib.file.transfer.WebDAVClient;
import com.aurora.drivesyncer.mapper.FileInfoMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;

// 消费者，从 fileUploadQueue 中取出等待上传的文件，然后进行上传
public class UploadWorker extends Worker {
    Config config;

    Compressor compressor;
    Encryptor encryptor;
    FileTransferClient fileTransferClient;

    FileInfoMapper fileInfoMapper;
    BlockingQueue<Integer> fileUploadQueue;

    public UploadWorker(Config config, FileInfoMapper fileInfoMapper, BlockingQueue<Integer> fileUploadQueue) {
        this.config = config;
        this.fileInfoMapper = fileInfoMapper;
        this.fileUploadQueue = fileUploadQueue;
        this.compressor = new GzipCompressor();
        this.encryptor = new AuroraEncryptor(config.getFilePassword());
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
                Integer fileId = fileUploadQueue.take();
                // file 可能被放进两次队列，或已被删除
                // 如果数据库中不存在或状态已经更新，就不用重复更新了
                FileInfo fileInfo = fileInfoMapper.selectById(fileId);
                if (fileInfo == null || fileInfo.getStatus() != FileInfo.SyncStatus.Waiting) {
                    if (fileInfo == null) {
                        log.warn("FileInfo not found. Skipping uploading");
                    } else {
                        log.warn(fileInfo.getFullPath() + " is " + fileInfo.getStatus() + ". Skipping uploading");
                    }
                    continue;
                }
                // 更新数据库 status 字段
                fileInfo.setStatus(FileInfo.SyncStatus.Syncing);
                fileInfoMapper.updateById(fileInfo);
                // 上传文件
                uploadFile(fileInfo);
                // 上传完成
                fileInfo.setStatus(FileInfo.SyncStatus.Synced);
                fileInfoMapper.updateById(fileInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadFile(FileInfo fileInfo) throws IOException {
        // 对文件压缩
        String fullPath = fileInfo.getFullPath();
        log.info("Zipping and encrypting " + fullPath);
        File file = new File(config.getLocalPath(), fullPath);
        InputStream inputStream = new FileInputStream(file);
        InputStream zippedInputStream = compressor.compress(inputStream);
        // 对文件加密
        InputStream encryptedInputStream = encryptor.encrypt(zippedInputStream);
        // 上传文件
        log.info("Uploading " + fullPath);
        fileTransferClient.putFile(fullPath, encryptedInputStream);
        log.info("Finish uploading " + fileInfo.getFullPath());
    }
}
