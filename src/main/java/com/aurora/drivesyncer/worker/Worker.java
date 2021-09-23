package com.aurora.drivesyncer.worker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class Worker implements Runnable {
    Thread thread;
    Log log = LogFactory.getLog(getClass());

    @Override
    abstract public void run();

    public void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public void interrupt() {
        if (thread != null) {
            thread.interrupt();
        }
    }
}
