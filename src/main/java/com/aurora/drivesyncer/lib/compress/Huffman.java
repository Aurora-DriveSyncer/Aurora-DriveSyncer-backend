package com.aurora.drivesyncer.lib.compress;

import java.io.*;

public class Huffman implements Compressor {
    @Override
    public File compress(File origin, String archivePath) throws IOException {
        File archive = new File(archivePath);
        if (!archive.exists() && !archive.createNewFile()) {
            throw new IOException();
        }
        try (FileInputStream originInputStream = new FileInputStream(origin);
             FileOutputStream archiveOutputStream = new FileOutputStream(archive)) {
            int[] byteCount = new int[256];
            int c;
            // 第一遍读入 origin，统计频次
            while ((c = originInputStream.read()) != -1) {
                byteCount[c]++;
            }
            originInputStream.reset();
            // 根据频次构建 Huffman 树
            buildHuffmanTree(byteCount);

        }
        return archive;
    }

    @Override
    public File extract(File archive, String extractPath) throws IOException {
        File extract = new File(extractPath);
        if (!extract.exists() && !extract.createNewFile()) {
            throw new IOException();
        }
        try (FileInputStream archiveInputStream = new FileInputStream(archive);
             FileOutputStream extractOutputStream = new FileOutputStream(extract)) {

        }
        return extract;
    }

    private void buildHuffmanTree(int[] byteCount) {

    }
}
