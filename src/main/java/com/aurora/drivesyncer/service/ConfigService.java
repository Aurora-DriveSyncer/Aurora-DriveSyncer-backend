package com.aurora.drivesyncer.service;

import com.aurora.drivesyncer.entity.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ConfigService {
    private Config config;
    @Autowired
    private SyncService syncService;

    public Config getConfig() {
        return config == null ? new Config() : config;
    }

    public void setConfig(Config config) throws IOException {
        syncService.close();
        try {
            this.config = config;
            syncService.setConfig(config);
            syncService.open();
        } catch (Exception e) {
            this.config = null;
            throw e;
        }
    }
}
