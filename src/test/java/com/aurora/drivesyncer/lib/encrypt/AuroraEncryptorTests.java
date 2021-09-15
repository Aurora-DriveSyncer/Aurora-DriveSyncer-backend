package com.aurora.drivesyncer.lib.encrypt;

import com.aurora.drivesyncer.utils.FileTests;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

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
        File file = new File("src/main/resources/application.yaml");
        String passphrase = "aurora-drivesyncer";
        testAuroraEncryptorOnFile(file, passphrase);
    }

    @Test
    public void testAuroraEncryptorOn1mbTextFile() throws IOException {
        String path = testDirectory + "/java-test-1mb-file";
        int size = 1024 * 1024;
        int lower = 'a';
        int upper = 'z';

        File file = new File(path);
        try(PrintWriter printWriter = new PrintWriter(file)) {
            new Random()
                    .ints(lower, upper + 1)
                    .limit(size)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .chars()
                    .forEachOrdered(printWriter::print);
        }
        String passphrase = "aurora-drivesyncer";
        testAuroraEncryptorOnFile(file, passphrase);
    }

    @Test
    public void testAuroraEncryptorOn1mbBinaryFile() throws IOException {
        String path = testDirectory + "/java-test-1mb-file";
        int size = 1024 * 1024;
        byte[] bytes = new byte[size];
        new Random().nextBytes(bytes);
        File file = new File(path);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(bytes);
        }
        String passphrase = "aurora-drivesyncer";
        testAuroraEncryptorOnFile(file, passphrase);
    }

    @Test
    public void testAuroraEncryptorOnEmptyFile() throws IOException {
        String path = testDirectory + "/java-test-empty-file";
        File file = new File(path);
        if (!file.exists() && !file.createNewFile()) {
            throw new IOException();
        }
        String passphrase = "aurora-drivesyncer";
        testAuroraEncryptorOnFile(file, passphrase);
    }

    @Test
    public void testAuroraEncryptorOn1bFile() throws IOException {
        String path = testDirectory + "/java-test-1b-file";
        File file = new File(path);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write('a');
        }
        String passphrase = "aurora-drivesyncer";
        testAuroraEncryptorOnFile(file, passphrase);
    }
}
