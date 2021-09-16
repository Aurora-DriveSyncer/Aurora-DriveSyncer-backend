package com.aurora.drivesyncer.lib.file.compress;

import com.aurora.drivesyncer.utils.FileTests;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GzipCompressorTests extends FileTests {
    public void testGzipCompressorOnFile(File origin) throws IOException {
        final String archivePath = testDirectory + "/archive", extractPath = testDirectory + "/extract";
        Compressor compressor = new GzipCompressor();
        File archive = compressor.compress(origin, archivePath);
        File extract = compressor.extract(archive, extractPath);
        assertTrue(FileUtils.contentEquals(origin, extract));
        System.out.printf("Huffman Compress on %s: %s -> %s\n", origin.getName(), humanReadableSize(origin), humanReadableSize(archive));
    }

    @Test
    public void testGzipOnTextFile() throws IOException {
        File file = readSampleTextFile();
        testGzipCompressorOnFile(file);
    }

    @Test
    public void testGzipOn1mbTextFile() throws IOException {
        File file = createTempTextFile(1024 * 1024, 'a', 'z');
        testGzipCompressorOnFile(file);
    }

    @Test
    public void testGzipOn1mbRepeatTextFile() throws IOException {
        File file = createTempTextFile(1024 * 1024, 'a', 'a');
        testGzipCompressorOnFile(file);
    }

    @Test
    public void testGzipOn1mbBinaryFile() throws IOException {
        File file = createTempBinaryFile(1024 * 1024);
        testGzipCompressorOnFile(file);
    }

    @Test
    public void testGzipOnEmptyFile() throws IOException {
        File file = createTempEmptyFile();
        testGzipCompressorOnFile(file);
    }

    @Test
    public void testGzipOn1bFile() throws IOException {
        File file = createTempBinaryFile(1);
        testGzipCompressorOnFile(file);
    }
}
