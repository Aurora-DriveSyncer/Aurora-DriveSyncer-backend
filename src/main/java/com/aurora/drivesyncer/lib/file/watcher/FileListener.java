package com.aurora.drivesyncer.lib.file.watcher;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class FileListener extends FileAlterationListenerAdaptor {
    Log log = LogFactory.getLog(getClass());
    
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
        log.info(String.format("%s create ...", file.getPath()));
        super.onFileCreate(file);
    }

    @Override
    public void onFileChange(File file) {
        log.info(String.format("%s change...", file.getPath()));
        super.onFileChange(file);
    }

    @Override
    public void onFileDelete(File file) {
        log.info(String.format("%s deleted...", file.getPath()));
        super.onFileDelete(file);
    }



    @Override
    public void onDirectoryChange(File directory) {
        log.info(String.format("%s dir change...", directory.getPath()));
        super.onDirectoryChange(directory);
    }

    @Override
    public void onDirectoryCreate(File directory) {
        log.info(String.format("%s dir create....", directory.getPath()));
        super.onDirectoryCreate(directory);
    }

    @Override
    public void onDirectoryDelete(File directory) {
        log.info(String.format("%s dir delete....", directory.getPath()));
        super.onDirectoryCreate(directory);
    }
}