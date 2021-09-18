package com.aurora.drivesyncer.lib.file.compress;

import com.aurora.drivesyncer.utils.FileTests;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class CompressorTests extends FileTests {
    static public final Compressor[] compressors = new Compressor[]{
            new GzipCompressor()
    };
    Log log = LogFactory.getLog(getClass());

    public void testCompressorsOnFile(File origin) throws IOException {
        byte[] originBytes, compressedBytes, extractBytes;
        try (InputStream originFileInputStream = new FileInputStream(origin)) {
            originBytes = originFileInputStream.readAllBytes();
        }
        for (Compressor compressor : compressors) {
            try (InputStream compressedInputStream = compressor.compress(new ByteArrayInputStream(originBytes))) {
                compressedBytes = compressedInputStream.readAllBytes();
            }
            try (InputStream compressedInputStream2 = new ByteArrayInputStream(compressedBytes)) {
                InputStream extractInputStream = compressor.extract(compressedInputStream2);
                extractBytes = extractInputStream.readAllBytes();
            }
            assertArrayEquals(originBytes, extractBytes);
            log.info(String.format("%s on %s: %s -> %s\n",
                    compressor.getClass().getName(),
                    origin.getName(),
                    humanReadableSize(originBytes.length),
                    humanReadableSize(compressedBytes.length)));
        }
    }

    @Test
    public void testGzipOnTextFile() throws IOException {
        File file = readSampleTextFile();
        testCompressorsOnFile(file);
    }

    @Test
    public void testGzipOn1mbTextFile() throws IOException {
        File file = createTempTextFile(1024 * 1024, 'a', 'z');
        testCompressorsOnFile(file);
    }

    @Test
    public void testGzipOn1mbRepeatTextFile() throws IOException {
        File file = createTempTextFile(1024 * 1024, 'a', 'a');
        testCompressorsOnFile(file);
    }

    @Test
    public void testGzipOn1mbBinaryFile() throws IOException {
        File file = createTempBinaryFile(1024 * 1024);
        testCompressorsOnFile(file);
    }

    @Test
    public void testGzipOnEmptyFile() throws IOException {
        File file = createTempEmptyFile();
        testCompressorsOnFile(file);
    }

    @Test
    public void testGzipOn1bFile() throws IOException {
        File file = createTempBinaryFile(1);
        testCompressorsOnFile(file);
    }
}
