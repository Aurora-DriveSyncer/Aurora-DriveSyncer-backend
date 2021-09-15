package com.aurora.drivesyncer.lib.encrypt;

import java.io.File;
import java.io.IOException;

public interface Encrpytor {
    public File encrypt(File origin, String encryptedPath) throws IOException;

    public File decrypt(File encrypted, String decryptedPath) throws IOException;
}
