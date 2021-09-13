package com.aurora.drivesyncer.mapper;

import com.aurora.drivesyncer.entity.FileInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileInfoMapper extends BaseMapper<FileInfo> {
    @Select("SELECT * FROM file_info WHERE status = 'Waiting' ORDER BY path, filename LIMIT 1")
    FileInfo selectFirstWaitingFile();

    default FileInfo updateFirstWaitingFileToSyncing() {
        FileInfo fileInfo = this.selectFirstWaitingFile();
        if (fileInfo == null) {
            return null;
        }
        fileInfo.setStatus(FileInfo.SyncStatus.Syncing);
        this.updateById(fileInfo);
        return fileInfo;
    }

    @Select("SELECT * FROM file_info WHERE path = #{path} ORDER BY filename")
    List<FileInfo> selectFileByPath(@Param("path") String path);

    @Select("SELECT * FROM file_info WHERE status = 'Syncing' ORDER BY path, filename")
    List<FileInfo> selectSyncingFile();
}
