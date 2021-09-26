package com.aurora.drivesyncer.web;

import com.aurora.drivesyncer.entity.FileInfo;
import com.aurora.drivesyncer.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLConnection;
import java.util.List;

@RestController
@RequestMapping(path = "/api/")
public class FileController {
    @Autowired
    private FileService fileService;

    // 获取该文件夹下文件的列表
    @GetMapping(value = "/list/")
    public List<FileInfo> getFileList(@RequestParam String path) {
        return fileService.getFileListByPath(path);
    }

    // 获取所有文件
    @GetMapping(value = "/list/all/")
    public List<FileInfo> getAllFile() {
        return fileService.getFileList();
    }

    // 获取正在同步的文件列表
    @GetMapping(value = "/syncing/")
    public List<FileInfo> getSyncingList() {
        return fileService.getSyncingList();
    }

    // 下载文件，如果不存在则返回 404
    @GetMapping(value = "/download/")
    public void downloadFile(@RequestParam String path, HttpServletResponse response) throws IOException {
        byte[] data;
        try {
            data = fileService.getFileContent(path);
            response.getOutputStream().write(data);
            response.flushBuffer();
        } catch (FileNotFoundException e) {
            response.setStatus(404);
            return;
        }
        // 设置响应头
        String name = new File(path).getName();
        response.addHeader("Content-disposition", String.format("attachment;filename=\"%s\"", name));
        // 似乎不能很好的解析 Content-Type
        response.setContentType(URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(data)));
    }

    // 将云盘的文件还原到的本地
    @GetMapping(value = "/restore/")
    public void restoreDrive(@RequestParam String path) throws IOException {
        fileService.restoreDrive(path);
    }
}
