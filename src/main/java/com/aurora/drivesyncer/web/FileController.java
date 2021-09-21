package com.aurora.drivesyncer.web;

import com.aurora.drivesyncer.entity.FileInfo;
import com.aurora.drivesyncer.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/")
public class FileController {
    @Autowired
    private FileService fileService;

    @GetMapping(value = "/list/")
    public List<FileInfo> getFileList(@RequestParam String path) {
        return fileService.getFileListByPath(path);
    }

    @GetMapping(value = "/download/")
    public void downloadFile(@RequestParam String path) {
        // todo
    }

    @GetMapping(value = "/syncing/")
    public List<FileInfo> getSyncingList() {
        return fileService.getSyncingList();
    }
}
