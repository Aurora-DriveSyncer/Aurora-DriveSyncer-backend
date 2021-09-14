package com.aurora.drivesyncer.lib.compress;

import com.aurora.drivesyncer.utils.FileTests;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HuffmanTests extends FileTests {
    public void testHuffmanOnFile(File origin) throws IOException {
//        final String archivePath = testDirectory + "/archive", extractPath = testDirectory + "/extract";
//        Compressor compressor = new Huffman();
//        File archive = compressor.compress(origin, archivePath);
//        File extract = compressor.extract(archive, extractPath);
//        System.out.printf("Huffman Compress: %s -> %s\n", humanReadableSize(archive), humanReadableSize(extract));
//        assertTrue(FileUtils.contentEquals(origin, extract));
    }

    @Test
    public void testHuffmanOnTextFile() throws IOException {
        File file = new File("src/main/resources/application.yaml");
        testHuffmanOnFile(file);
    }

    @Test
    public void testHuffmanOn1mbTextFile() throws IOException {
        String path = testDirectory + "/java-test-1mb-file";
        int size = 1024 * 1024;
        int lower = 'a';
        int upper = 'z';

        File file = new File(path);
        try(PrintWriter printWriter = new PrintWriter(file)) {
            new Random()
                    .ints(lower, upper + 1)
                    .limit(size)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .chars()
                    .forEachOrdered(printWriter::print);
        }
        testHuffmanOnFile(file);
    }

    @Test
    public void testHuffmanOn1mbBinaryFile() throws IOException {
        String path = testDirectory + "/java-test-1mb-file";
        int size = 1024 * 1024;
        byte[] bytes = new byte[size];
        new Random().nextBytes(bytes);
        File file = new File(path);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(bytes);
        }
        testHuffmanOnFile(file);
    }

    @Test
    public void testHuffmanOnEmptyFile() throws IOException {
        String path = testDirectory + "/java-test-empty-file";
        File file = new File(path);
        if (!file.exists() && !file.createNewFile()) {
            throw new IOException();
        }
        testHuffmanOnFile(file);
    }

    @Test
    public void testHuffmanOn1bFile() throws IOException {
        String path = testDirectory + "/java-test-1b-file";
        File file = new File(path);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write('a');
        }
        testHuffmanOnFile(file);
    }
}
