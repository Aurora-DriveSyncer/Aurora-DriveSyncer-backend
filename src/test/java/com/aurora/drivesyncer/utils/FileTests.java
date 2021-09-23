package com.aurora.drivesyncer.utils;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Random;

public class FileTests {
    static public final String testDirectory = ".test/";

    @BeforeAll
    static public void setup() throws IOException {
        File testDir = new File(testDirectory);
        if (!testDir.exists() && !testDir.mkdirs()) {
            throw new IOException();
        }
    }

    @AfterAll
    static public void teardown() throws IOException {
        FileUtils.deleteDirectory(new File(testDirectory));
    }

    // 将大小转为形如 KB 的字符串
    public static String humanReadableSize(long size) {
        double value = size;
        CharacterIterator ci = new StringCharacterIterator(" KMGTPE");
        while (value >= 1000) {
            value /= 1024;
            ci.next();
        }
        value *= Long.signum(size);
        return String.format("%.1f %cB", value, ci.current());
    }

    // 创建一个空文件并返回
    public static File createTempEmptyFile() throws IOException {
        String path = testDirectory + "empty-file";
        File file = new File(path);
        if (!file.exists() && !file.createNewFile()) {
            throw new IOException();
        }
        return file;
    }

    // 读取 pom.xml 用以测试
    public static File readSampleTextFile() {
        return new File("pom.xml");
    }

    // 创建一个随机文本文件
    public static File createTempTextFile(int size, char lower, char upper) throws IOException {
        String path = String.format("%srandom-from-%c-to-%c-file", testDirectory, lower, upper);

        File file = new File(path);
        StringBuilder sb = new Random()
                .ints(lower, upper + 1)
                .limit(size)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.print(sb);
        }
        return file;
    }

    // 创建一个随机二进制文件
    public static File createTempBinaryFile(int size) throws IOException {
        String path = testDirectory + "random-binary-file";
        byte[] bytes = new byte[size];
        new Random().nextBytes(bytes);
        File file = new File(path);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(bytes);
        }
        return file;
    }
}
