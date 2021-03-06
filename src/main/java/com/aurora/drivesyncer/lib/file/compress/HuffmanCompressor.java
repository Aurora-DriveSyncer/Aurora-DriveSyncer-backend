//package com.aurora.drivesyncer.lib.file.compress;
//
//import java.io.*;
//
//public class HuffmanCompressor implements Compressor {
//    static final int HUFFMAN_LENGTH = 8;
//    static final int HUFFMAN_SIZE = 1 << HUFFMAN_LENGTH;
//
//    @Override
//    public InputStream compress(InputStream originInputStream) throws IOException {
//        File archive = new File(archivePath);
//        if (!archive.exists() && !archive.createNewFile()) {
//            throw new IOException();
//        }
//        try (FileInputStream originInputStream = new FileInputStream(origin);
//             FileOutputStream archiveOutputStream = new FileOutputStream(archive)) {
//            int c;
//            while ((c = originInputStream.read()) != -1) {
//                archiveOutputStream.write(c);
//            }
//        }
//        return archive;
//    }
//
//    @Override
//    public InputStream extract(InputStream archiveInputStream) throws IOException {
//        File extract = new File(extractPath);
//        if (!extract.exists() && !extract.createNewFile()) {
//            throw new IOException();
//        }
//        try (FileInputStream archiveInputStream = new FileInputStream(archive);
//             FileOutputStream extractOutputStream = new FileOutputStream(extract)) {
//            int c;
//            while ((c = archiveInputStream.read()) != -1)
//                extractOutputStream.write(c);
//        }
//        return extract;
//    }
//
////    private Map<Byte, String> buildHuffmanTree(int[] byteCount) {
////          for int
////    }
//}
