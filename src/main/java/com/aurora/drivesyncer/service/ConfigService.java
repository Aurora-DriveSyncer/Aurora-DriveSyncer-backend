package com.aurora.drivesyncer.service;

import com.aurora.drivesyncer.entity.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ConfigService {
    @Autowired
    private Config config;
    @Autowired
    private SyncService syncService;

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) throws IOException {
        syncService.close();
        this.config = config;
        syncService.initialize();
    }
}
