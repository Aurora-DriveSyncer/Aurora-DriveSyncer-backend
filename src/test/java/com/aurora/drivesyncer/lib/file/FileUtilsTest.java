package com.aurora.drivesyncer.lib.file;

import org.junit.jupiter.api.Test;

import static com.aurora.drivesyncer.lib.file.FileUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileUtilsTest extends FileTestTemplate {
    @Test
    void testAppendSlashIfMissing() {
        assertEquals("123/", appendSlashIfMissing("123"));
        assertEquals("123/", appendSlashIfMissing("123/"));
        assertEquals("/123/", appendSlashIfMissing("/123"));
        assertEquals("/123/", appendSlashIfMissing("/123/"));

        assertEquals("/", appendSlashIfMissing("/"));
    }

    @Test
    void testRemovePrependingSlash() {
        assertEquals("123", removePrependingSlash("123"));
        assertEquals("123/", removePrependingSlash("123/"));
        assertEquals("123", removePrependingSlash("/123"));
        assertEquals("123/", removePrependingSlash("/123/"));

        assertEquals("", removePrependingSlash("/"));
    }

    @Test
    void testFormatPath() {
        assertEquals("123/", formatPath("123"));
        assertEquals("123/", formatPath("123/"));
        assertEquals("123/", formatPath("/123"));
        assertEquals("123/", formatPath("/123/"));

        assertEquals("", formatPath("/"));
    }

    @Test
    void testGetRelativePath() {
        assertEquals("", getRelativePath("/", "/"));
        assertEquals("", getRelativePath("/var/data", "/var/data"));

        assertEquals("stuff/xyz.dat", getRelativePath("/var/data/stuff/xyz.dat", "/var/data"));
        assertEquals("stuff/xyz.dat", getRelativePath("./data/stuff/xyz.dat", "./data"));
        assertEquals("stuff/xyz.dat", getRelativePath("./data/stuff/xyz.dat", "data"));
        assertEquals("stuff/xyz.dat", getRelativePath("data/stuff/xyz.dat", "./data"));
        assertEquals("stuff/xyz.dat", getRelativePath("data/stuff/xyz.dat", "data"));

        assertEquals("stuff/xyz.dat", getRelativePath("/var/data/stuff/xyz.dat/", "/var/data"));
        assertEquals("stuff/xyz.dat", getRelativePath("./data/stuff/xyz.dat/", "./data"));
        assertEquals("stuff/xyz.dat", getRelativePath("./data/stuff/xyz.dat/", "data"));
        assertEquals("stuff/xyz.dat", getRelativePath("data/stuff/xyz.dat/", "./data"));
        assertEquals("stuff/xyz.dat", getRelativePath("data/stuff/xyz.dat/", "data"));
    }


//    @Test
//    void testIsLink() throws IOException {
//        File file = createTempBinaryFile(1024);
//        File hardLink = createHardLink(file);
//        File softLink = createSoftLink(file);
//
//        assertTrue(file.exists());
//        assertTrue(hardLink.exists());
//        assertTrue(softLink.exists());
//
//        assertFalse(isSoftLink(file));
//        assertFalse(isHardLink(file));
//        assertTrue(isSoftLink(softLink));
//        assertFalse(isHardLink(softLink));
//        assertFalse(isSoftLink(hardLink));
//        assertTrue(isHardLink(hardLink));
//    }
}
