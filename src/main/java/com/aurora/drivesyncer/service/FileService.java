package com.aurora.drivesyncer.service;

import com.aurora.drivesyncer.entity.FileInfo;
import com.aurora.drivesyncer.lib.file.transfer.LocalStorageClient;
import com.aurora.drivesyncer.mapper.FileInfoMapper;
import com.aurora.drivesyncer.worker.DownloadWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    @Autowired
    private FileInfoMapper fileInfoMapper;
    @Autowired
    private SyncService syncService;

    public List<FileInfo> getFileList() {
        return fileInfoMapper.selectList(null);
    }

    public List<FileInfo> getFileListByPath(String path) {
        return fileInfoMapper.selectByParent(path);
    }

    public List<FileInfo> getSyncingList() {
        return fileInfoMapper.selectSyncingList();
    }

    public byte[] getFileContent(String path) throws IOException {
        return syncService.getDownloadWorker().downloadFile(path);
    }

    public void restoreDrive(String path) throws IOException {
        LocalStorageClient localStorageClient = new LocalStorageClient(path);
        localStorageClient.testConnection();
        DownloadWorker downloadWorker = new DownloadWorker(syncService.getConfig(), fileInfoMapper, localStorageClient);
        downloadWorker.start(1);
    }
}
