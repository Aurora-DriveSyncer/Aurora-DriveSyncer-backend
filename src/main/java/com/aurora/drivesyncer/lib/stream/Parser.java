package com.aurora.drivesyncer.lib.stream;

import java.io.*;

public class Parser {
    static public ByteArrayInputStream toInputStream(ByteArrayOutputStream byteArrayOutputStream) {
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    static public ByteArrayOutputStream toOutputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(inputStream.readAllBytes());
        return byteArrayOutputStream;
    }

}