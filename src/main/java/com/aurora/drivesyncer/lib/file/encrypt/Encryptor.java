package com.aurora.drivesyncer.lib.file.encrypt;

import java.io.IOException;
import java.io.InputStream;

public interface Encryptor {
    InputStream encrypt(InputStream originInputStream) throws IOException;

    InputStream decrypt(InputStream encryptedInputStream) throws IOException;
}
