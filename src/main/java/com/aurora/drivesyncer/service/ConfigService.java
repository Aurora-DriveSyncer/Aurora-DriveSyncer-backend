package com.aurora.drivesyncer.service;

import com.aurora.drivesyncer.entity.Config;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ConfigService {
    Config config;

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) throws IOException {
        this.config = config;
    }
}
