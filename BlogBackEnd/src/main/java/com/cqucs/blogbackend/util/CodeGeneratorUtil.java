package com.cqucs.blogbackend.util;

import java.util.UUID;

public class CodeGeneratorUtil {
    /**
     * 生成指定长度的验证码
     * @param length 长度
     * @return
     */
    public static String generateCode(int length){
        return UUID.randomUUID().toString().substring(0, length);
    }
}
