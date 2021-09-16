package com.aurora.drivesyncer.lib.file.hash;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public interface Hash {
    String hash(byte[] bytes);

    default String hash(String str) {
        return hash(str.getBytes(StandardCharsets.UTF_8));
    }

    default String hash(InputStream inputStream) throws IOException {
        return hash(FileUtils.readFileToByteArray(file));
    }
}
