package com.aurora.drivesyncer.lib.hash;

import org.springframework.util.DigestUtils;

// 该库仅用于验证自己编写的 MD5 算法的正确性
public class SpringMD5 implements Hash {
    @Override
    public String hash(byte[] bytes) {
        return DigestUtils.md5DigestAsHex(bytes);
    }
}
