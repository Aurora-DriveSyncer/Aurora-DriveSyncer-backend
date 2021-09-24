package com.aurora.drivesyncer.lib;

import java.io.File;
import java.net.URI;

public class Utils {
    // 计算是否需要加复数 s
    public static String prependingS(int count) {
        return count != 1 ? "s" : "";
    }

    // 如果字符串不以 / 结尾，则追加一个 /
    // 用以保证（文件夹路径）字符串以 / 结尾
    public static String appendSlashIfMissing(String str) {
        if (!str.endsWith("/")) {
            str += "/";
        }
        return str;
    }

    // 如果字符串以 / 开头，则删去一个 /
    // 用以保证（相对路径路径）字符串不以 / 开头
    public static String removePrependingSlash(String str) {
        if (str.startsWith("/")) {
            str = str.substring(1);
        }
        return str;
    }

    // 获取两个路径（路径不需要存在）的相对路径
    public static String getRelativePath(String target, String base) {
        return getRelativePath(new File(target), new File(base));
    }

    // 获取两个文件（文件不需要存在）的相对路径
    // 保证结果不以 / 开头、不以 / 结尾
    public static String getRelativePath(File target, File base) {
        URI baseURI = base.toURI();
        URI targetURI = target.toURI();
        return baseURI.relativize(targetURI).getPath();
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
