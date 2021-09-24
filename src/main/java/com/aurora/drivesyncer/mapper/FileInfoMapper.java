package com.aurora.drivesyncer.mapper;

import com.aurora.drivesyncer.entity.FileInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Random;

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

    @Select("SELECT * FROM file_info WHERE path = #{parent} ORDER BY filename")
    List<FileInfo> selectByParent(@Param("parent") String parent);

    @Select("SELECT * FROM file_info WHERE path = #{parent} AND filename = #{filename}")
    FileInfo selectByParentAndName(@Param("parent") String parent, @Param("filename") String filename);

    default void insertOrUpdateByParentAndName(FileInfo fileInfo) {
        FileInfo fileInfo1 = this.selectByParentAndName(fileInfo.getPath(), fileInfo.getFilename());
        if (fileInfo1 != null) {
            fileInfo.setId(fileInfo1.getId());
            this.updateById(fileInfo);
        } else {
            while (true) {
                // 进行插入，若插入失败则重试
                try {
                    fileInfo.setId(new Random().nextInt());
                    this.insert(fileInfo);
                    break;
                } catch (Exception ignored) {
                    Log log = LogFactory.getLog(getClass());
                    log.warn("Retry insert fileInfo into database, file: " + fileInfo.getFullPath());
                }
            }
        }
    }

    @Delete("DELETE FROM file_info WHERE path = #{parent} AND filename = #{filename}")
    Integer deleteByParentAndName(@Param("parent") String parent, @Param("filename") String filename);

    @Select("SELECT * FROM file_info WHERE status = 'Syncing' ORDER BY path, filename")
    List<FileInfo> selectSyncingList();

//    @Update("UPDATE file_info SET status = #{status} WHERE id = #{id}")
//    Integer updateStatusById(@Param("id") Integer id, @Param("status") FileInfo.SyncStatus status);
}
