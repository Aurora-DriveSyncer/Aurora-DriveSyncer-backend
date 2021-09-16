package com.aurora.drivesyncer.lib.file.encrypt;

import java.io.*;

public class AuroraEncryptor implements Encrpytor {
    private final String passphrase;

    public AuroraEncryptor(String passphrase) {
        this.passphrase = passphrase;
    }

    @Override
    public InputStream encrypt(InputStream originInputStream) throws IOException {
        File encrypted = new File(encryptedPath);
        if (!encrypted.exists() && !encrypted.createNewFile()) {
            throw new IOException();
        }
        try (FileInputStream originInputStream = new FileInputStream(origin);
             FileOutputStream encrpytedOutputStream = new FileOutputStream(encrypted)) {
            int c;
            while ((c = originInputStream.read()) != -1) {
                encrpytedOutputStream.write(c);
            }
        }
        return encrypted;
    }

    @Override
    public InputStream decrypt(InputStream encryptedInputStream) throws IOException {
        File decrypted = new File(decryptedPath);
        if (!decrypted.exists() && !decrypted.createNewFile()) {
            throw new IOException();
        }
        try (FileInputStream encryptedInputStream = new FileInputStream(encrypted);
             FileOutputStream decryptedOutputStream = new FileOutputStream(decrypted)) {
            int c;
            while ((c = encryptedInputStream.read()) != -1) {
                decryptedOutputStream.write(c);
            }
        }
        return decrypted;
    }
}