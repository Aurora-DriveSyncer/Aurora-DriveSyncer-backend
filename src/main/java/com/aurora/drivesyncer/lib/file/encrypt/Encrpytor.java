package com.aurora.drivesyncer.lib.file.encrypt;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface Encrpytor {
    public InputStream encrypt(InputStream originInputStream) throws IOException;

    public InputStream decrypt(InputStream encryptedInputStream) throws IOException;
}
