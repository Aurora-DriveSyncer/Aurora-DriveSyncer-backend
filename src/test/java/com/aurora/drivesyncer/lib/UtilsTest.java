package com.aurora.drivesyncer.lib;

import org.junit.jupiter.api.Test;

import static com.aurora.drivesyncer.lib.Utils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilsTest {
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

    @Test
    void testPadStartAndEnd() {
        assertEquals("---1234---", padStartAndEnd("1234", 10, '-'));
        assertEquals("---123----", padStartAndEnd("123", 10, '-'));
        assertEquals("----------", padStartAndEnd("", 10, '-'));
    }

    @Test
    void testFormatLog() {
        assertEquals("---------------------------FINISH SYNCING SOFT LINKS----------------------------", formatLog("FINISH SYNCING SOFT LINKS"));
        assertEquals("----------------------------START SYNCING HARD LINKS----------------------------", formatLog("START SYNCING HARD LINKS"));
        assertEquals("--------------------------------------------------------------------------------", formatLog(""));
    }
}