package com.aurora.drivesyncer.lib.file.compress;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static com.aurora.drivesyncer.lib.stream.StreamUtils.toInputStream;

public class GzipCompressor implements Compressor {
    @Override
    public InputStream compress(InputStream originInputStream) throws IOException {
            ByteArrayOutputStream compressOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gzipOutputStream = new GZIPOutputStream(compressOutputStream);
            gzipOutputStream.write(originInputStream.readAllBytes());
            gzipOutputStream.close();
            return toInputStream(compressOutputStream);
    }

    @Override
    public InputStream extract(InputStream archiveInputStream) throws IOException {
        return new GZIPInputStream(archiveInputStream);
    }
}
