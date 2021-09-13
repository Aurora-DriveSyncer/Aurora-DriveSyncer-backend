package com.aurora.drivesyncer.lib.watcher;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;


public class FileMonitor {
    private final String path;                                // 文件夹目录
    private final long interval;                        // 监听间隔
    private static final long DEFAULT_INTERVAL = 1000;  // 默认监听间隔 1s
    private final FileAlterationListenerAdaptor listener;        // 事件处理类对象

    public FileMonitor(String path) {
        this.path = path;
        this.interval = DEFAULT_INTERVAL;
        this.listener = new FileListener();
    }

    public FileMonitor(String path, FileAlterationListenerAdaptor listener) {
        this.path = path;
        this.interval = DEFAULT_INTERVAL;
        this.listener = listener;
    }

    public FileMonitor(String path, long interval, FileAlterationListenerAdaptor listener) {
        this.path = path;
        this.interval = interval;
        this.listener = listener;
    }

    public void start() {
        FileAlterationObserver observer = new FileAlterationObserver(path);
        observer.addListener(listener);
        FileAlterationMonitor monitor = new FileAlterationMonitor(interval);
        monitor.addObserver(observer);
        System.out.println("add monitor");
        try {
            monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("starting monitor");
    }
}