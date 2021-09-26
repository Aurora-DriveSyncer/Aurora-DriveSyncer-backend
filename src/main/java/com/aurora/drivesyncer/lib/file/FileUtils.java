package com.aurora.drivesyncer.lib.file;

import java.io.File;
import java.net.URI;

public class FileUtils {
    // 如果字符串不以 / 结尾，则追加一个 /
    // 用以保证（文件夹路径）字符串以 / 结尾
    public static String appendSlashIfMissing(String path) {
        return path == null ? "" :
                path.endsWith("/") ? path :
                        path + "/";
    }

    // 如果字符串以 / 开头，则删去一个 /
    // 用以保证（相对路径）字符串不以 / 开头
    public static String removePrependingSlash(String path) {
        return path == null ? "" :
                path.startsWith("/") ? path.substring(1) :
                        path;
    }

    // 将相对文件夹路径格式化
    public static String formatPath(String path) {
        return removePrependingSlash(appendSlashIfMissing(path));
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

//    public static boolean isSoftLink(File file) {
//        return Files.isSymbolicLink(file.toPath());
//    }
//
//    public static boolean isHardLink(File file) throws IOException {
//        Object o  = Files.getAttribute(file.toPath(), "unix:nlink");
//        System.out.println(o);
//        return false;
//    }
}
