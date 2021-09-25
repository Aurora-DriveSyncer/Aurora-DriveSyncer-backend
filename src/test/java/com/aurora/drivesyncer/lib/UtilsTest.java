package com.aurora.drivesyncer.lib;

import org.junit.jupiter.api.Test;

import static com.aurora.drivesyncer.lib.Utils.formatLog;
import static com.aurora.drivesyncer.lib.Utils.padStartAndEnd;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilsTest {
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