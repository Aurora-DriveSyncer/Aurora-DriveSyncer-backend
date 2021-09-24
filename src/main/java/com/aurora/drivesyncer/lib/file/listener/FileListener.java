package com.aurora.drivesyncer.lib.file.listener;

import com.aurora.drivesyncer.service.SyncService;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;


public class FileListener extends FileAlterationListenerAdaptor {
    Log log = LogFactory.getLog(getClass());
    SyncService syncService;

    public FileListener(SyncService syncService) {
        this.syncService = syncService;
    }

    @Override
    public void onStart(FileAlterationObserver observer) {
        log.debug(String.format("Start scanning %s...", observer.getDirectory()));
        super.onStart(observer);
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
        log.debug(String.format("Finish scanning %s...", observer.getDirectory()));
        super.onStop(observer);
    }

    @Override
    public void onFileCreate(File file) {
        log.info(String.format("%s create...", file.getPath()));
        try {
            syncService.addLocalFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onFileCreate(file);
    }

    @Override
    public void onFileChange(File file) {
        log.info(String.format("%s change...", file.getPath()));
        try {
            syncService.addLocalFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onFileChange(file);
    }

    @Override
    public void onFileDelete(File file) {
        log.info(String.format("%s deleted...", file.getPath()));
        try {
            syncService.deleteLocalFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onFileDelete(file);
    }


    @Override
    public void onDirectoryChange(File directory) {
        log.info(String.format("%s dir change...", directory.getPath()));
        super.onDirectoryChange(directory);
    }

    @Override
    public void onDirectoryCreate(File directory) {
        log.info(String.format("%s dir create...", directory.getPath()));
        super.onDirectoryCreate(directory);
    }

    @Override
    public void onDirectoryDelete(File directory) {
        log.info(String.format("%s dir delete...", directory.getPath()));
        super.onDirectoryCreate(directory);
    }
}