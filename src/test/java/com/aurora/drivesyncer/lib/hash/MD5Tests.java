package com.aurora.drivesyncer.lib.hash;

import com.aurora.drivesyncer.utils.FileTests;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MD5Tests extends FileTests {
    @Test
    public void testMD5OnString() {
        String str = "qwertuiop[]\\;'.,/1";
        // echo -n "qwertuiop[]\\;'.,/1" | md5sum
        String md5result = "4256e6c8f2a73fbb508d637ede0c687d";
        Hash springMd5 = new SpringMD5();
        assertEquals(md5result, springMd5.hash(str));
    }

    @Test
    public void testMD5OnEmptyString() {
        String str = "";
        // echo -n "" | md5sum
        String md5result = "d41d8cd98f00b204e9800998ecf8427e";
        Hash springMd5 = new SpringMD5();
        assertEquals(md5result, springMd5.hash(str));
    }

    @Test
    public void testMD5OnTextFile() throws IOException {
        File file = readSampleTextFile();
        Hash springMd5 = new SpringMD5();
        assertNotNull(springMd5.hash(file));
    }

    @Test
    public void testMD5OnEmptyFile() throws IOException {
        // echo -n "" | md5sum
        String md5result = "d41d8cd98f00b204e9800998ecf8427e";
        File file = createTempEmptyFile();
        Hash springMd5 = new SpringMD5();
        assertEquals(md5result, springMd5.hash(file));
    }
}
