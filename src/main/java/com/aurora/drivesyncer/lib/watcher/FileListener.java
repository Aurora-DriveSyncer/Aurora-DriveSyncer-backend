package com.aurora.drivesyncer.lib.watcher;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;


public class FileListener extends FileAlterationListenerAdaptor {
    @Override
    public void onFileChange(File file) {
        System.out.printf("%schange.............\n", file.getPath());
        super.onFileChange(file);
    }

    @Override
    public void onStart(FileAlterationObserver observer) {
        System.out.printf("Start listening %s...\n", observer.getDirectory());
        super.onStart(observer);
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
        System.out.println("Stop Listening.");
        super.onStop(observer);
    }

    @Override
    public void onDirectoryChange(File directory) {
        System.out.printf("%s dir change...\n", directory.getPath());
        super.onDirectoryChange(directory);
    }

    @Override
    public void onDirectoryCreate(File directory) {
        System.out.printf("%s dir create...\n.", directory.getPath());
        super.onDirectoryCreate(directory);
    }

    @Override
    public void onDirectoryDelete(File directory) {
        System.out.printf("%s dir delete...\n.", directory.getPath());
        super.onDirectoryCreate(directory);
    }
}