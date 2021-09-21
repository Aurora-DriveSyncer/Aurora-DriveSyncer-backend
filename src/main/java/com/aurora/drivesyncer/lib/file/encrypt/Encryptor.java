package com.aurora.drivesyncer.lib.file.encrypt;

import java.io.IOException;
import java.io.InputStream;

public interface Encryptor {
    public InputStream encrypt(InputStream originInputStream) throws IOException;

    public InputStream decrypt(InputStream encryptedInputStream) throws IOException;
}
