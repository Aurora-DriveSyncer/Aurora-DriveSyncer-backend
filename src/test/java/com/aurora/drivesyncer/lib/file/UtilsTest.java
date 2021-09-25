package com.aurora.drivesyncer.lib.file;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilsTest extends FileTestTemplate {
    @Test
    void testAppendSlashIfMissing() {
        assertEquals("123/", com.aurora.drivesyncer.lib.file.Utils.appendSlashIfMissing("123"));
        assertEquals("123/", com.aurora.drivesyncer.lib.file.Utils.appendSlashIfMissing("123/"));
        assertEquals("/123/", com.aurora.drivesyncer.lib.file.Utils.appendSlashIfMissing("/123"));
        assertEquals("/123/", com.aurora.drivesyncer.lib.file.Utils.appendSlashIfMissing("/123/"));

        assertEquals("/", com.aurora.drivesyncer.lib.file.Utils.appendSlashIfMissing("/"));
    }

    @Test
    void testRemovePrependingSlash() {
        assertEquals("123", com.aurora.drivesyncer.lib.file.Utils.removePrependingSlash("123"));
        assertEquals("123/", com.aurora.drivesyncer.lib.file.Utils.removePrependingSlash("123/"));
        assertEquals("123", com.aurora.drivesyncer.lib.file.Utils.removePrependingSlash("/123"));
        assertEquals("123/", com.aurora.drivesyncer.lib.file.Utils.removePrependingSlash("/123/"));

        assertEquals("", com.aurora.drivesyncer.lib.file.Utils.removePrependingSlash("/"));
    }

    @Test
    void testGetRelativePath() {
        assertEquals("", com.aurora.drivesyncer.lib.file.Utils.getRelativePath("/", "/"));
        assertEquals("", com.aurora.drivesyncer.lib.file.Utils.getRelativePath("/var/data", "/var/data"));

        assertEquals("stuff/xyz.dat", com.aurora.drivesyncer.lib.file.Utils.getRelativePath("/var/data/stuff/xyz.dat", "/var/data"));
        assertEquals("stuff/xyz.dat", com.aurora.drivesyncer.lib.file.Utils.getRelativePath("./data/stuff/xyz.dat", "./data"));
        assertEquals("stuff/xyz.dat", com.aurora.drivesyncer.lib.file.Utils.getRelativePath("./data/stuff/xyz.dat", "data"));
        assertEquals("stuff/xyz.dat", com.aurora.drivesyncer.lib.file.Utils.getRelativePath("data/stuff/xyz.dat", "./data"));
        assertEquals("stuff/xyz.dat", com.aurora.drivesyncer.lib.file.Utils.getRelativePath("data/stuff/xyz.dat", "data"));

        assertEquals("stuff/xyz.dat", com.aurora.drivesyncer.lib.file.Utils.getRelativePath("/var/data/stuff/xyz.dat/", "/var/data"));
        assertEquals("stuff/xyz.dat", com.aurora.drivesyncer.lib.file.Utils.getRelativePath("./data/stuff/xyz.dat/", "./data"));
        assertEquals("stuff/xyz.dat", com.aurora.drivesyncer.lib.file.Utils.getRelativePath("./data/stuff/xyz.dat/", "data"));
        assertEquals("stuff/xyz.dat", com.aurora.drivesyncer.lib.file.Utils.getRelativePath("data/stuff/xyz.dat/", "./data"));
        assertEquals("stuff/xyz.dat", com.aurora.drivesyncer.lib.file.Utils.getRelativePath("data/stuff/xyz.dat/", "data"));
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
