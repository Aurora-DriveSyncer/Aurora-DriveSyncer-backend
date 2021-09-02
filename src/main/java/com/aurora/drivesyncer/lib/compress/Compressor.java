package com.aurora.drivesyncer.lib.compress;

import java.io.File;

public interface Compressor {
    void compress(File input, File output);
    void decompress(File input, File output);
}
