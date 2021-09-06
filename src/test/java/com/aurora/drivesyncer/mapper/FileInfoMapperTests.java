package com.aurora.drivesyncer.mapper;

import com.aurora.drivesyncer.entity.FileInfo;
import com.aurora.drivesyncer.entity.FileInfo.SyncStatus;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;

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
    void updateFirstWaitingFileToSyncing() {
        insertFileInfo("/", "a.txt", SyncStatus.Syncing);
        insertFileInfo("/", "b.txt", SyncStatus.Syncing);
        insertFileInfo("/path/", "c.txt", SyncStatus.Waiting);

        assertEquals(3, fileInfoMapper.selectCount(null));

        Integer cId = fileInfoMapper.updateFirstWaitingFileToSyncing();
        FileInfo cInfo = fileInfoMapper.selectById(cId);
        assertEquals("c.txt", cInfo.getFilename());

        cId = fileInfoMapper.updateFirstWaitingFileToSyncing();
        assertNull(cId);
    }

    @AfterEach
    public void clearDatabase() {
        deleteFromTables(jdbcTemplate, "file_info");
    }
}
