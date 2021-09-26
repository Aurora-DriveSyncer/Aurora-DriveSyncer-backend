package com.aurora.drivesyncer.web;

import com.aurora.drivesyncer.entity.Config;
import com.aurora.drivesyncer.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(path = "/api/config/")
public class ConfigController {
    @Autowired
    private ConfigService configService;

    @GetMapping
    public Config getConfig() {
        return configService.getConfig();
    }

    @PutMapping
    public Config setConfig(@RequestBody Config config) throws IOException {
        configService.setConfig(config);
        return config;
    }
}
