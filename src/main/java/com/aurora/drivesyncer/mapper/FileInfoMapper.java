package com.aurora.drivesyncer.mapper;

import com.aurora.drivesyncer.entity.FileInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileInfoMapper extends BaseMapper<FileInfo> {
    @Select("SELECT * FROM file_info WHERE status = 'Waiting' ORDER BY path, filename LIMIT 1")
    FileInfo selectFirstWaiting();

    default FileInfo updateFirstWaitingFileToSyncing() {
        FileInfo fileInfo = this.selectFirstWaiting();
        if (fileInfo == null) {
            return null;
        }
        fileInfo.setStatus(FileInfo.SyncStatus.Syncing);
        this.updateById(fileInfo);
        return fileInfo;
    }

    @Select("SELECT * FROM file_info WHERE path = #{path} ORDER BY filename")
    List<FileInfo> selectByPath(@Param("path") String path);

    @Select("SELECT * FROM file_info WHERE path = #{path} AND filename = #{filename}")
    FileInfo selectByPathAndName(@Param("path") String path, @Param("filename") String filename);

    default FileInfo insertOrUpdateByPathAndName(FileInfo fileInfo) {
        FileInfo fileInfo1 = this.selectByPathAndName(fileInfo.getPath(), fileInfo.getFilename());
        if (fileInfo1 != null) {
            fileInfo.setId(fileInfo1.getId());
            this.updateById(fileInfo);
        } else {
            this.insert(fileInfo);
        }
        return fileInfo;
    }

    @Delete("Delete * FROM file_info WHERE path = #{path} AND filename = #{filename}")
    Integer deleteByPathAndName(@Param("path") String path, @Param("filename") String filename);

    @Select("SELECT * FROM file_info WHERE status = 'Syncing' ORDER BY path, filename")
    List<FileInfo> selectSyncingList();

//    @Update("UPDATE file_info SET status = #{status} WHERE id = #{id}")
//    Integer updateStatusById(@Param("id") Integer id, @Param("status") FileInfo.SyncStatus status);
}
