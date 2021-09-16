package com.aurora.drivesyncer.lib.file.encrypt;

import com.aurora.drivesyncer.utils.FileTests;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuroraEncryptorTests extends FileTests {
    public void testAuroraEncryptorOnFile(File origin, String passphrase) throws IOException {
        final String encryptedPath = testDirectory + "/encrypted", decryptedPath = testDirectory + "/decrypted";
        Encrpytor encrpytor = new AuroraEncryptor(passphrase);
        File encrypted = encrpytor.encrypt(origin, encryptedPath);
        File decrypted = encrpytor.decrypt(encrypted, decryptedPath);
        assertTrue(FileUtils.contentEquals(origin, decrypted));
    }

    @Test
    public void testAuroraEncryptorOnTextFile() throws IOException {
        File file = readSampleTextFile();
        String passphrase = "aurora-drivesyncer";
        testAuroraEncryptorOnFile(file, passphrase);
    }

    @Test
    public void testAuroraEncryptorOn1mbTextFile() throws IOException {
        File file = createTempTextFile(1024 * 1024, 'a', 'z');
        String passphrase = "aurora-drivesyncer";
        testAuroraEncryptorOnFile(file, passphrase);
    }

    @Test
    public void testAuroraEncryptorOn1mbBinaryFile() throws IOException {
        File file = createTempBinaryFile(1024 * 1024);
        String passphrase = "aurora-drivesyncer";
        testAuroraEncryptorOnFile(file, passphrase);
    }

    @Test
    public void testAuroraEncryptorOnEmptyFile() throws IOException {
        File file = createTempEmptyFile();
        String passphrase = "aurora-drivesyncer";
        testAuroraEncryptorOnFile(file, passphrase);
    }

    @Test
    public void testAuroraEncryptorOn1bFile() throws IOException {
        File file = createTempBinaryFile(1);
        String passphrase = "aurora-drivesyncer";
        testAuroraEncryptorOnFile(file, passphrase);
    }
}
