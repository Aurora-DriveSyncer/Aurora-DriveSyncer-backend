package com.aurora.drivesyncer.lib.stream;

import java.io.*;

public class Parser {
    static public ByteArrayInputStream toInputStream(String in) {
        return new ByteArrayInputStream(in.getBytes());
    }
    // ?
    static public ByteArrayInputStream toInputStream(OutputStream out) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos = (ByteArrayOutputStream) out;
        return new ByteArrayInputStream(baos.toByteArray());
    }

    // ?
    static public String toString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int ch;
        while ((ch = inputStream.read()) != -1) {
            swapStream.write(ch);
        }
        return swapStream.toString();
    }

    // ?
    static public String toString(OutputStream outputStream) {
        return outputStream.toString();
    }

    // ?
    static public ByteArrayOutputStream toOutputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int ch;
        while ((ch = inputStream.read()) != -1) {
            swapStream.write(ch);
        }
        return swapStream;
    }

    // ?
    static public ByteArrayOutputStream toOutputStream(String s) throws IOException {
        return toOutputStream(toInputStream(s));
    }
}