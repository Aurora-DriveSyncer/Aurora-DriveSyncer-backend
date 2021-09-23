package com.aurora.drivesyncer.lib;

public class Utils {
    static public String appendSlashIfMissing(String str) {
        if (!str.endsWith("/")) {
            str += "/";
        }
        return str;
    }

    static public String removePrependingSlash(String str) {
        if (str.startsWith("/")) {
            str = str.substring(1);
        }
        return str;
    }
}
