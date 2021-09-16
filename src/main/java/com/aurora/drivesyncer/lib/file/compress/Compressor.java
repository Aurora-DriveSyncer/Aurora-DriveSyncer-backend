package com.aurora.drivesyncer.lib.file.compress;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface Compressor {
    public InputStream compress(InputStream originInputStream) throws IOException;

    public InputStream extract(InputStream archiveInputStream) throws IOException;
}
