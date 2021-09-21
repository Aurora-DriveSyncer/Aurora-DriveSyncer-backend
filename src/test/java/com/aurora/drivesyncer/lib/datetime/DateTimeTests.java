package com.aurora.drivesyncer.lib.datetime;

import com.aurora.drivesyncer.utils.FileTests;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static com.aurora.drivesyncer.entity.FileInfo.getAttribute;
import static com.aurora.drivesyncer.lib.datetime.DateTime.toLocalTime;

class DateTimeTests extends FileTests {
    @Test
    public void testToLocalTime() throws IOException {
        File fileA = new File(testDirectory + "test-file-time-a");
        if (!fileA.exists() && !fileA.createNewFile()) {
            throw new IOException();
        }
        System.out.println(toLocalTime(getAttribute(fileA).creationTime()));
    }
}