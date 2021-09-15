package com.aurora.drivesyncer.lib.compress;

import com.aurora.drivesyncer.utils.FileTests;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HuffmanCompressorTests extends FileTests {
    public void testHuffmanCompressorOnFile(File origin) throws IOException {
//        final String archivePath = testDirectory + "/archive", extractPath = testDirectory + "/extract";
//        Compressor compressor = new HuffmanCompressor();
//        File archive = compressor.compress(origin, archivePath);
//        File extract = compressor.extract(archive, extractPath);
//        System.out.printf("Huffman Compress: %s -> %s\n", humanReadableSize(archive), humanReadableSize(extract));
//        assertTrue(FileUtils.contentEquals(origin, extract));
    }

    @Test
    public void testHuffmanOnTextFile() throws IOException {
        File file = readSampleTextFile();
        testHuffmanCompressorOnFile(file);
    }

    @Test
    public void testHuffmanOn1mbTextFile() throws IOException {
        File file = createTempTextFile(1024 * 1024);
        testHuffmanCompressorOnFile(file);
    }

    @Test
    public void testHuffmanOn1mbBinaryFile() throws IOException {
        File file = createTempBinaryFile(1024 * 1024);
        testHuffmanCompressorOnFile(file);
    }

    @Test
    public void testHuffmanOnEmptyFile() throws IOException {
        File file = createTempEmptyFile();
        testHuffmanCompressorOnFile(file);
    }

    @Test
    public void testHuffmanOn1bFile() throws IOException {
        File file = createTempBinaryFile(1);
        testHuffmanCompressorOnFile(file);
    }
}
