package com.gd.form.utils;

public class NumberUtil {
    public static boolean isNumber(String value) {
        return value.matches("\\d+\\.?\\d*");
    }
}
