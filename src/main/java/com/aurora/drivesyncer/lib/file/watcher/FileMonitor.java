package com.aurora.drivesyncer.lib.file.watcher;

import com.aurora.drivesyncer.service.SyncService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;


public class FileMonitor implements Runnable {
    private final File path;                                // 文件夹目录
    private final long interval;                            // 监听间隔
    private static final long DEFAULT_INTERVAL = 5000;      // 默认监听间隔 5s
    private final FileAlterationListenerAdaptor listener;   // 事件处理类对象
    private final SyncService syncService;
    private Thread thread;
    private final Log log = LogFactory.getLog(getClass());

    public FileMonitor(String pathname, SyncService syncService) {
        this(pathname, DEFAULT_INTERVAL, new FileListener(syncService), syncService);
    }

    public FileMonitor(String pathname, FileAlterationListenerAdaptor listener, SyncService syncService) {
        this(pathname, DEFAULT_INTERVAL, listener, syncService);
    }

    public FileMonitor(String pathname, long interval, FileAlterationListenerAdaptor listener, SyncService syncService) {
        this.path = new File(pathname);
        this.interval = interval;
        this.listener = listener;
        this.syncService = syncService;
    }

    @Override
    public void run() {
        try {
            fullScan();
            addFileListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void interrupt() {
        if (thread != null) {
            thread.interrupt();
        }
    }

    public void start() throws IOException {
        if (!path.exists()) {
            throw new FileNotFoundException(path.getCanonicalPath());
        }
        if (thread == null) {
            thread = new Thread(this);
        }
        thread.start();
    }

    private void fullScan() throws IOException {
        log.info("Start scanning " + path.getCanonicalPath());
        Collection<File> files = FileUtils.listFiles(path, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        for (File file: files) {
            syncService.addLocalFile(file);
        }
        log.info(String.format("Finish scanning %s (%d file(s) found)", path.getCanonicalPath(), files.size()));
    }

    private void addFileListener() throws Exception {
        FileAlterationObserver observer = new FileAlterationObserver(path);
        observer.addListener(listener);
        FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
        monitor.start();
        log.info("Start listening on change from " + path.getCanonicalPath());
    }
}