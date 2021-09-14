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
public class FileInfoMapperTests {
    @Autowired
    FileInfoMapper fileInfoMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

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
        List<FileInfo> fileInfoList = fileInfoMapper.selectSyncingFile();
        assertEquals(2, fileInfoList.size());
        assertEquals("a.txt", fileInfoList.get(0).getFilename());
        assertEquals("b.txt", fileInfoList.get(1).getFilename());

        // 更新 1 个为 Synced
        FileInfo fileInfoA = fileInfoList.get(0), fileInfoB = fileInfoList.get(1);
        fileInfoA.setStatus(SyncStatus.Synced);
        fileInfoMapper.updateById(fileInfoA);
        fileInfoList = fileInfoMapper.selectSyncingFile();
        assertEquals(1, fileInfoList.size());
        assertEquals("b.txt", fileInfoList.get(0).getFilename());

        // 更新另一个为 Waiting
        fileInfoB.setStatus(SyncStatus.Waiting);
        fileInfoMapper.updateById(fileInfoB);
        fileInfoList = fileInfoMapper.selectSyncingFile();
        assertEquals(0, fileInfoList.size());
    }

    @AfterEach
    public void clearDatabase() {
        deleteFromTables(jdbcTemplate, "file_info");
    }
}
