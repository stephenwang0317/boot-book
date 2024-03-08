package com.wjm.bootbook.utils;

import org.springframework.util.DigestUtils;

import java.util.Random;

/**
 * @author stephen wang
 */
public class Md5Utils {
    public static String encrypt(String plainText, String salt) {
        return DigestUtils.md5DigestAsHex((salt + plainText + salt).getBytes()).toUpperCase();
    }


    /**
     * 自定义简单生成盐，是一个随机生成的长度为16的字符串，每一个字符是随机的十六进制字符
     */
    private static final char[] HEX_NUM = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String getSalt() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < sb.capacity(); i++) {
            sb.append(HEX_NUM[random.nextInt(16)]);
        }
        return sb.toString();
    }
}
