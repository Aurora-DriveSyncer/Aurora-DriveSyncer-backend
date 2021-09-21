package com.aurora.drivesyncer.service;

import com.aurora.drivesyncer.entity.FileInfo;
import com.aurora.drivesyncer.mapper.FileInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    @Autowired
    private FileInfoMapper fileInfoMapper;

    public List<FileInfo> getFileListByPath(String path) {
        return fileInfoMapper.selectByPath(path);
    }

    public List<FileInfo> getSyncingList() {
        return fileInfoMapper.selectSyncingList();
    }
}
