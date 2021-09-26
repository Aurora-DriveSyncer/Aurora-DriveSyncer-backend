package com.aurora.drivesyncer.service;

import com.aurora.drivesyncer.entity.FileInfo;
import com.aurora.drivesyncer.mapper.FileInfoMapper;
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

    public List<FileInfo> getFileListByPath(String path) {
        return fileInfoMapper.selectByParent(path);
    }

    public List<FileInfo> getSyncingList() {
        return fileInfoMapper.selectSyncingList();
    }

    public byte[] getFileContent(String path) throws IOException {
        return syncService.getDownloadWorker().downloadFile(path);
    }
}
