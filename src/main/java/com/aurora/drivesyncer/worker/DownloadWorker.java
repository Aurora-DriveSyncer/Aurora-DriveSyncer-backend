package com.aurora.drivesyncer.worker;

import com.aurora.drivesyncer.entity.Config;
import com.aurora.drivesyncer.entity.FileInfo;
import com.aurora.drivesyncer.lib.file.compress.Compressor;
import com.aurora.drivesyncer.lib.file.compress.GzipCompressor;
import com.aurora.drivesyncer.lib.file.encrypt.AES128Encryptor;
import com.aurora.drivesyncer.lib.file.encrypt.Encryptor;
import com.aurora.drivesyncer.lib.file.transfer.FileTransferClient;
import com.aurora.drivesyncer.lib.file.transfer.WebDAVClient;
import com.aurora.drivesyncer.mapper.FileInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.io.*;
import java.util.List;

import static com.aurora.drivesyncer.lib.file.FileUtils.formatDirPath;
import static com.aurora.drivesyncer.lib.log.LogUtils.prependS;

// DownloadWorker 负责下载和还原
// 下载：通过调用 downloadFile，将 sourceClient 的文件解密、解压后作为 HTTP 报文返回
// 还原：通过调用 downloadFile，将 sourceClient 的文件解密、解压后传输到 destinationClient
public class DownloadWorker extends Worker {
    Config config;

    Compressor compressor;
    Encryptor encryptor;
    FileTransferClient sourceClient, destinationClient;

    FileInfoMapper fileInfoMapper;

    public DownloadWorker(Config config, FileInfoMapper fileInfoMapper) {
        this(config, fileInfoMapper, null);
    }

    public DownloadWorker(Config config, FileInfoMapper fileInfoMapper, FileTransferClient destinationClient) {
        this.config = config;
        this.fileInfoMapper = fileInfoMapper;

        this.compressor = new GzipCompressor();
        this.encryptor = new AES128Encryptor(config.getFilePassword());
        this.sourceClient = new WebDAVClient(config.getUrl(), config.getUsername(), config.getPassword());
        this.destinationClient = destinationClient;
    }

    // run 函数为还原文件
    @Override
    public void run() {
        QueryWrapper<FileInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("is_directory", 0);
        List<FileInfo> fileList = fileInfoMapper.selectList(wrapper);
        for (FileInfo fileInfo : fileList) {
            try {
                // 下载文件
                InputStream inputStream = downloadFileToInputStream(fileInfo);
                // 将下载的文件还原到 destinationClient
                destinationClient.putFile(fileInfo.getFullPath(), inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info(String.format("Restore success with %d file%s", fileList.size(), prependS(fileList.size())));
    }

    public byte[] downloadFile(String path) throws IOException {
        File file = new File(path);
        String name = file.getName();
        String parent = formatDirPath(file.getParent());
        FileInfo fileInfo = fileInfoMapper.selectByParentAndName(parent, name);
        if (fileInfo == null) {
            throw new FileNotFoundException(path);
        } else if (fileInfo.getDirectory()) {
            throw new IOException(path + " is a directory");
        }
        return downloadFile(fileInfo);
    }

    public byte[] downloadFile(FileInfo fileInfo) throws IOException {
        return downloadFileToInputStream(fileInfo).readAllBytes();
    }

    public InputStream downloadFileToInputStream(FileInfo fileInfo) throws IOException {
        String fullPath = fileInfo.getFullPath();
        log.info("Downloading " + fullPath);
        // 下载文件
        byte[] data = sourceClient.getFileToByteArray(fullPath);
        // 解密，解压
        InputStream inputStream = new ByteArrayInputStream(data);
        InputStream decryptedInputStream = encryptor.decrypt(inputStream);
        InputStream extractedInputStream = compressor.extract(decryptedInputStream);
        return extractedInputStream;
    }

}
