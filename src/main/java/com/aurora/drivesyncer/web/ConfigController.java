package com.aurora.drivesyncer.web;
import com.aurora.drivesyncer.entity.Config;
import com.aurora.drivesyncer.service.ConfigService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(path = "/api/config/")
public class ConfigController {
    @Autowired
    ConfigService configService;

    @GetMapping
    public Config getConfig() {
        return configService.getConfig();
    }

    @PutMapping
    public void setConfig(@RequestBody Config config) throws IOException {
        configService.setConfig(config);
    }
}
