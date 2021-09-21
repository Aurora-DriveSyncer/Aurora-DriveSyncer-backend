package com.aurora.drivesyncer.mapper;

import com.aurora.drivesyncer.entity.FileInfo;
import com.aurora.drivesyncer.entity.FileInfo.SyncStatus;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.springframework.test.jdbc.JdbcTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FileInfoMapperTest {
    @Autowired
    FileInfoMapper fileInfoMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @AfterEach
    public void clearDatabase() {
        deleteFromTables(jdbcTemplate, "file_info");
    }
    
    void insertFileInfo(String path, String filename, SyncStatus status) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setPath(path);
        fileInfo.setFilename(filename);
        fileInfo.setStatus(status);
        fileInfoMapper.insert(fileInfo);
    }

    // 测试 MyBatis 是否能正确转换 Enum 和其字符串值
    @Test
    void testMyBatisHandelEnum() {
        for (SyncStatus status: SyncStatus.values()) {
            insertFileInfo("/", "a.txt", status);

            QueryWrapper<FileInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("status", status.name());
            assertEquals(1, fileInfoMapper.selectCount(wrapper));
            assertEquals(status, fileInfoMapper.selectOne(null).getStatus());
            clearDatabase();
        }
    }

    @Test
    void testUpdateFirstWaitingFileToSyncing() {
        // 只有 c.txt 是等待更新
        insertFileInfo("/", "a.txt", SyncStatus.Syncing);
        insertFileInfo("/", "b.txt", SyncStatus.Syncing);
        insertFileInfo("/path/", "c.txt", SyncStatus.Waiting);

        QueryWrapper<FileInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", SyncStatus.Syncing);
        assertEquals(2, fileInfoMapper.selectCount(queryWrapper));

        // 更新并返回 c.txt
        FileInfo fileInfo = fileInfoMapper.updateFirstWaitingFileToSyncing();
        assertNotNull(fileInfo);
        assertEquals("c.txt", fileInfo.getFilename());
        assertEquals(3, fileInfoMapper.selectCount(queryWrapper));

        // 没有发生更新
        fileInfo = fileInfoMapper.updateFirstWaitingFileToSyncing();
        assertNull(fileInfo);
        assertEquals(3, fileInfoMapper.selectCount(queryWrapper));
    }

    @Test
    void testSelectSyncingFile() {
        insertFileInfo("/", "a.txt", SyncStatus.Syncing);
        insertFileInfo("/", "b.txt", SyncStatus.Syncing);
        insertFileInfo("/path/", "c.txt", SyncStatus.Waiting);

        // 有 2 个 Syncing
        List<FileInfo> fileInfoList = fileInfoMapper.selectSyncingList();
        assertEquals(2, fileInfoList.size());
        assertEquals("a.txt", fileInfoList.get(0).getFilename());
        assertEquals("b.txt", fileInfoList.get(1).getFilename());

        // 更新 1 个为 Synced
        FileInfo fileInfoA = fileInfoList.get(0), fileInfoB = fileInfoList.get(1);
        fileInfoA.setStatus(SyncStatus.Synced);
        fileInfoMapper.updateById(fileInfoA);
        fileInfoList = fileInfoMapper.selectSyncingList();
        assertEquals(1, fileInfoList.size());
        assertEquals("b.txt", fileInfoList.get(0).getFilename());

        // 更新另一个为 Waiting
        fileInfoB.setStatus(SyncStatus.Waiting);
        fileInfoMapper.updateById(fileInfoB);
        fileInfoList = fileInfoMapper.selectSyncingList();
        assertEquals(0, fileInfoList.size());
    }

    @Test
    void insertOrUpdateByPathAndName() {
        assertEquals(0, fileInfoMapper.selectCount(null));

        FileInfo fileInfo = new FileInfo();
        fileInfo.setPath("/");
        fileInfo.setFilename("a.txt");
        fileInfo.setStatus(SyncStatus.Synced);
        fileInfoMapper.insertOrUpdateByPathAndName(fileInfo);
        assertEquals(1, fileInfoMapper.selectCount(null));

        fileInfo.setFilename("b.txt");
        fileInfo.setId(null);
        fileInfoMapper.insertOrUpdateByPathAndName(fileInfo);
        assertEquals(2, fileInfoMapper.selectCount(null));

        fileInfo.setPath("folder");
        fileInfo.setId(null);
        fileInfoMapper.insertOrUpdateByPathAndName(fileInfo);
        assertEquals(3, fileInfoMapper.selectCount(null));

        fileInfo.setStatus(SyncStatus.Syncing);
        fileInfo.setId(null);
        fileInfoMapper.insertOrUpdateByPathAndName(fileInfo);
        assertEquals(3, fileInfoMapper.selectCount(null));

        FileInfo fileInfo1 = fileInfoMapper.selectById(fileInfo.getId());
        assertEquals(SyncStatus.Syncing, fileInfo1.getStatus());
    }


//    @Test
//    void testUpdateStatusById() {
//        insertFileInfo("/", "a.txt", SyncStatus.Syncing);
//        insertFileInfo("/", "b.txt", SyncStatus.Syncing);
//        insertFileInfo("/path/", "c.txt", SyncStatus.Waiting);
//
//        // 有 2 个 Syncing
//        List<FileInfo> fileInfoList = fileInfoMapper.selectSyncingList();
//        assertEquals(2, fileInfoList.size());
//        assertEquals("a.txt", fileInfoList.get(0).getFilename());
//        assertEquals("b.txt", fileInfoList.get(1).getFilename());
//
//        Integer integer;
//
//        // 使用 updateStatusById 更新 1 个为 Synced
//        FileInfo fileInfoA = fileInfoList.get(0), fileInfoB = fileInfoList.get(1);
//        integer = fileInfoMapper.updateStatusById(fileInfoA.getId(), SyncStatus.Synced);
//        assertEquals(1, integer);
//        fileInfoA = fileInfoMapper.selectById(fileInfoA.getId());
//        assertEquals(SyncStatus.Synced, fileInfoA.getStatus());
//
//        // 使用 updateStatusById 更新另一个为 Waiting
//        fileInfoB.setStatus(SyncStatus.Waiting);
//        integer = fileInfoMapper.updateStatusById(fileInfoB.getId(), SyncStatus.Waiting);
//        assertEquals(1, integer);
//        fileInfoB = fileInfoMapper.selectById(fileInfoB.getId());
//        assertEquals(SyncStatus.Waiting, fileInfoB.getStatus());
//    }
}
