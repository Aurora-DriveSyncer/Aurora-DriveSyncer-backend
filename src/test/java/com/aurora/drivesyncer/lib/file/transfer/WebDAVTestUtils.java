package com.aurora.drivesyncer.lib.file.transfer;

import java.io.IOException;

public class WebDAVTestUtils {
    // 运行 WebDAV 测试需要用 docker 等方法部署 WebDAV 服务
    public static WebDAVClient webDAVClient =
            new WebDAVClient("http://localhost:8888/webdav/", "user", "user");

    // 如能连接上部署的 WebDAV 测试服务并删除所有文件，返回 true
    public static boolean initializeServer() {
        try {
            webDAVClient.deleteFile(".");
            return webDAVClient.listFiles("/").size() == 0;
        } catch (IOException e) {
            return false;
        }
    }
}