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

import java.io.*;

import static com.aurora.drivesyncer.lib.file.FileUtils.appendSlashIfMissing;
import static com.aurora.drivesyncer.lib.file.FileUtils.formatPath;

// 消费者，从 fileUploadQueue 中取出等待上传的文件，然后进行上传
public class DownloadWorker extends Worker {
    Config config;

    Compressor compressor;
    Encryptor encryptor;
    FileTransferClient fileTransferClient;

    FileInfoMapper fileInfoMapper;

    public DownloadWorker(Config config, FileInfoMapper fileInfoMapper) {
        this.config = config;
        this.fileInfoMapper = fileInfoMapper;

        this.compressor = new GzipCompressor();
        this.encryptor = new AuroraEncryptor(config.getFilePassword());
        this.fileTransferClient = new WebDAVClient(config.getUrl(), config.getUsername(), config.getPassword());
    }

    @Override
    public void run() {
        // todo Finish uploading src/main/resources///schema.sql
    }

    public byte[] downloadFile(String path) throws IOException {
        File file = new File(path);
        String name = file.getName();
        String parent = formatPath(file.getParent());
        FileInfo fileInfo = fileInfoMapper.selectByParentAndName(parent, name);
        if (fileInfo == null) {
            throw new FileNotFoundException();
        }
        return downloadFile(fileInfo);
    }

    public byte[] downloadFile(FileInfo fileInfo) throws IOException {
        String fullPath = fileInfo.getFullPath();
        log.info("Downloading " + fullPath);
        // 下载文件
        byte[] data = fileTransferClient.downloadFileToByteArray(fullPath);
        // 解密，解压
        log.info("Decrypting and extracting " + fullPath);
        InputStream inputStream = new ByteArrayInputStream(data);
        InputStream decryptedInputStream = encryptor.decrypt(inputStream);
        InputStream extractedInputStream = compressor.extract(decryptedInputStream);
        return extractedInputStream.readAllBytes();
    }

}
