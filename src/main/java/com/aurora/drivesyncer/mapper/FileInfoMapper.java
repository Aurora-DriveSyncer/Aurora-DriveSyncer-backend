package com.aurora.drivesyncer.mapper;

import com.aurora.drivesyncer.entity.FileInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface FileInfoMapper extends BaseMapper<FileInfo> {
    @Select("SELECT * from file_info WHERE status = 'Waiting' ORDER BY path, filename LIMIT 1")
    FileInfo selectFirstWaitingFile();

    default FileInfo updateFirstWaitingFileToSyncing() {
        FileInfo fileInfo = this.selectFirstWaitingFile();
        if (fileInfo == null)
            return null;
        fileInfo.setStatus(FileInfo.SyncStatus.Syncing);
        this.updateById(fileInfo);
        return fileInfo;
    }
}
