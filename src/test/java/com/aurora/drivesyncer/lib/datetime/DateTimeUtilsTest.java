package com.aurora.drivesyncer.lib.datetime;

import com.aurora.drivesyncer.lib.file.FileTestTemplate;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static com.aurora.drivesyncer.entity.FileInfo.getAttribute;
import static com.aurora.drivesyncer.lib.datetime.DateTimeUtils.toLocalTime;

class DateTimeUtilsTest extends FileTestTemplate {
    @Test
    public void testToLocalTime() throws IOException {
        File fileA = new File(testDirectory + "test-file-time-a");
        if (!fileA.exists() && !fileA.createNewFile()) {
            throw new IOException();
        }
        System.out.println(toLocalTime(getAttribute(fileA).creationTime()));
    }
}