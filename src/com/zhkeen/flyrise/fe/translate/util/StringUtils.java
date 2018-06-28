package com.zhkeen.flyrise.fe.translate.util;

public class StringUtils {

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

}
