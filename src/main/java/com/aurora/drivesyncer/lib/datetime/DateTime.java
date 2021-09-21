package com.aurora.drivesyncer.lib.datetime;

import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateTime {
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String toLocalTime(FileTime fileTime) {
        df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return df.format(fileTime.toMillis());
    }
}
