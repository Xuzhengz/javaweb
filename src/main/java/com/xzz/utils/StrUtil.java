package com.xzz.utils;

public class StrUtil {
    public static boolean isEmpty(String str) {
        if ("".equals(str) || str == null){
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
