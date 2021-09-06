package com.aurora.drivesyncer.mapper;

import com.aurora.drivesyncer.entity.FileInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface FileInfoMapper extends BaseMapper<FileInfo> {
    @Update("UPDATE file_info " +
            "SET status='Syncing' " +
            "WHERE id = ( " +
            "SELECT id from file_info WHERE status = 'Waiting' ORDER BY path, filename LIMIT 1)")
    @Options(useGeneratedKeys = true)
    Integer updateFirstWaitingFileToSyncing();
}
