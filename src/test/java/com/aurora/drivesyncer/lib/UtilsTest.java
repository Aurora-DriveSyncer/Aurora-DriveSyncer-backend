package com.aurora.drivesyncer.lib;

import org.junit.jupiter.api.Test;

import static com.aurora.drivesyncer.lib.Utils.appendSlashIfMissing;
import static com.aurora.drivesyncer.lib.Utils.removePrependingSlash;
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
}