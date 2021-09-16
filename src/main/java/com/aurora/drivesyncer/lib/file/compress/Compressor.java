package com.aurora.drivesyncer.lib.file.compress;

import java.io.File;
import java.io.IOException;

public interface Compressor {
    public File compress(File origin, String archivePath) throws IOException;

    public File extract(File archive, String extractPath) throws IOException;
}
