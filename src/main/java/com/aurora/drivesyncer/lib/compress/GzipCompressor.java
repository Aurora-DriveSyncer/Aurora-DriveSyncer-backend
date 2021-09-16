package com.aurora.drivesyncer.lib.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipCompressor implements Compressor {
    @Override
    public File compress(File origin, String archivePath) throws IOException {
        File archive = new File(archivePath);
        try (FileInputStream fileInputStream = new FileInputStream(origin);
             FileOutputStream fileOutputStream = new FileOutputStream(archive);
             GZIPOutputStream gzipOutputStream = new GZIPOutputStream(fileOutputStream)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fileInputStream.read(buffer)) != -1) {
                gzipOutputStream.write(buffer, 0, len);
            }
        }
        return archive;
    }

    @Override
    public File extract(File archive, String extractPath) throws IOException {
        File extract = new File(extractPath);
        try (FileInputStream fileInputStream = new FileInputStream(archive);
             GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);
             FileOutputStream fileOutputStream = new FileOutputStream(extract)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
        }
        return extract;
    }
}
