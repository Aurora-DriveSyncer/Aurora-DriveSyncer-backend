package com.aurora.drivesyncer.utils;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class FileTests {
    static public final String testDirectory = "aurora-test-temp";

    @BeforeAll
    static public void setup() throws IOException {
        if (!new File(testDirectory).mkdirs()) {
            throw new IOException();
        }
    }

    @AfterAll
    static public void teardown() throws IOException {
        FileUtils.deleteDirectory(new File(testDirectory));
    }

    public static String humanReadableSize(long size) {
        double value = size;
        CharacterIterator ci = new StringCharacterIterator(" KMGTPE");
        while (value >= 1000){
            value /= 1024;
            ci.next();
        }
        value *= Long.signum(size);
        return String.format("%.1f %cB", value, ci.current());
    }

    public static String humanReadableSize(File file) {
        return humanReadableSize(file.length());
    }
}
