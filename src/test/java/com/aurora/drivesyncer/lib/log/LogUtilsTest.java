package com.aurora.drivesyncer.lib.log;

import org.junit.jupiter.api.Test;

import static com.aurora.drivesyncer.lib.log.LogUtils.formatLog;
import static com.aurora.drivesyncer.lib.log.LogUtils.padStartAndEnd;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LogUtilsTest {
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