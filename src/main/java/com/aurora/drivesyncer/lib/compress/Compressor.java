package com.aurora.drivesyncer.lib.compress;

import java.io.File;

public interface Compressor {
    public File compress(File origin);
    public File extract(File archive);
}
