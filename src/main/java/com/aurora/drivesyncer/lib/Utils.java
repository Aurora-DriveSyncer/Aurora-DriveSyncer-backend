package com.aurora.drivesyncer.lib;

public class Utils {
    // 计算是否需要加复数 s
    public static String prependingS(int count) {
        return count != 1 ? "s" : "";
    }

    // 在字符串左右添加符号直至达到长度
    // 可用于美化日志
    public static String padStartAndEnd(String string, int length, char pad) {
        StringBuilder sb = new StringBuilder(length);
        sb.setLength((length - string.length()) / 2);
        sb.append(string);
        sb.setLength(length);
        return sb.toString().replace('\0', pad);
    }

    // 统一日志格式
    public static String formatLog(String log) {
        return padStartAndEnd(log, 80, '-');
    }
}
