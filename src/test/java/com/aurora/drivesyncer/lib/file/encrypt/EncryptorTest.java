package com.aurora.drivesyncer.lib.file.encrypt;

import com.aurora.drivesyncer.utils.FileTests;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class EncryptorTest extends FileTests {
    static public final Encryptor[] encryptors = new Encryptor[]{
            new AuroraEncryptor("aurora-drivesyncer")
    };

    public void testEncryptorsOnFile(File origin) throws IOException {
        InputStream originFileInputStream = new FileInputStream(origin);
        byte[] originBytes = originFileInputStream.readAllBytes();
        originFileInputStream.close();
        for (Encryptor encryptor : encryptors) {
            InputStream encryptedInputStream = encryptor.encrypt(new ByteArrayInputStream(originBytes));
            InputStream decryptedInputStream = encryptor.decrypt(encryptedInputStream);
            assertArrayEquals(originBytes, decryptedInputStream.readAllBytes());
        }
    }

    @Test
    public void testEncryptorsOnTextFile() throws IOException {
        File file = readSampleTextFile();
        testEncryptorsOnFile(file);
    }

    @Test
    public void testEncryptorsOn1mbTextFile() throws IOException {
        File file = createTempTextFile(1024 * 1024, 'a', 'z');
        testEncryptorsOnFile(file);
    }

    @Test
    public void testEncryptorsOn1mbBinaryFile() throws IOException {
        File file = createTempBinaryFile(1024 * 1024);
        testEncryptorsOnFile(file);
    }

    @Test
    public void testEncryptorsOnEmptyFile() throws IOException {
        File file = createTempEmptyFile();
        testEncryptorsOnFile(file);
    }

    @Test
    public void testEncryptorsOn1bFile() throws IOException {
        File file = createTempBinaryFile(1);
        testEncryptorsOnFile(file);
    }
}
