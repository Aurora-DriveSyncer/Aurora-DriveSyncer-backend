package com.aurora.drivesyncer.web;

import com.aurora.drivesyncer.entity.FileInfo;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class FileController {

    @GetMapping(value = "/list/")
    public List<FileInfo> getFileList(@RequestParam String path) {
        return new ArrayList<>();
    }

    @GetMapping(value = "/download/")
    public void downloadFile(@RequestParam String path) {
    }

    @GetMapping(value = "/syncing/")
    public List<FileInfo> getSyncingList() {
        return new ArrayList<>();
    }
}
