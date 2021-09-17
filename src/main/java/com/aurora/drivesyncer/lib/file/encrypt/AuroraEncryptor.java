package com.aurora.drivesyncer.lib.file.encrypt;

import java.io.*;

public class AuroraEncryptor implements Encryptor {
    private final String passphrase;

    public AuroraEncryptor(String passphrase) {
        this.passphrase = passphrase;
    }

    @Override
    public InputStream encrypt(InputStream originInputStream) throws IOException {
        return originInputStream;
    }

    @Override
    public InputStream decrypt(InputStream encryptedInputStream) throws IOException {
        return encryptedInputStream;
    }
}
