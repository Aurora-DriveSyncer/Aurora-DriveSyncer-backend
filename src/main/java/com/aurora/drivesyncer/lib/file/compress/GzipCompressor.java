package com.aurora.drivesyncer.lib.file.compress;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipCompressor implements Compressor {
    @Override
    public InputStream compress(InputStream originInputStream) throws IOException {
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
    public InputStream extract(InputStream archiveInputStream) throws IOException {
        return new GZIPInputStream(archiveInputStream);
    }
}
