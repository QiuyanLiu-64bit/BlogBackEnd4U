package com.cqucs.blogbackend.util;

import java.util.UUID;
import java.util.Random;

public class CodeGeneratorUtil {
    /**
     * 生成指定长度的验证码
     * @param length 长度
     * @return
     */
    /*public static String generateCode(int length){
        return UUID.randomUUID().toString().substring(0, length);
    }*/
    public static String generateCode(int length){
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
