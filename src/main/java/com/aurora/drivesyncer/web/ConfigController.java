package com.aurora.drivesyncer.web;
import com.aurora.drivesyncer.entity.Config;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/config/")
public class ConfigController {
    @GetMapping
    public Config getConfig() {
        return new Config();
    }

    @PutMapping
    public void modifyConfig(@RequestBody Config config) {
    }
}
