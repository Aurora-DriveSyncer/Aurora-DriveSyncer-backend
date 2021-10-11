package com.aurora.drivesyncer.lib.file.encrypt;

import com.aurora.drivesyncer.lib.file.hash.Hash;
import com.aurora.drivesyncer.lib.file.hash.SpringMD5;

import java.io.ByteArrayInputStream;
import java.security.*;
import java.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class AES128Encryptor implements Encryptor {
    private final String passphrase;
    private final byte[] key;

    public AES128Encryptor(String passphrase) {
        this.passphrase = passphrase;
        String keyString = new SpringMD5().hash(passphrase.getBytes(StandardCharsets.UTF_8));
        this.key = keyString.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public InputStream encrypt(InputStream originInputStream) throws IOException {
        try {
            byte[] originBytes = originInputStream.readAllBytes();

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKey secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(originBytes);

            return new ByteArrayInputStream(encryptedBytes);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }

    @Override
    public InputStream decrypt(InputStream encryptedInputStream) throws IOException {
        try {
            byte[] encryptedBytes = encryptedInputStream.readAllBytes();

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKey keySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            return new ByteArrayInputStream(decryptedBytes);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            throw new IOException();
        }
    }
}
