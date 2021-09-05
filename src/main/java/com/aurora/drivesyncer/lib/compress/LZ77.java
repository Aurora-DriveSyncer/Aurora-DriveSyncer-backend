package com.aurora.drivesyncer.lib.compress;

import java.io.File;

public class LZ77 implements Compressor {
    @Override
    public File compress(File origin) {
        return origin;
    }

    @Override
    public File extract(File archive) {
        return archive;
    }
}
