package com.gd.form.utils;


import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * <p>类描述：Gson工具类
 */
public class GsonUtil {

    /**
     * 对象转换成json字符串
     */
    public static String toString(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }


    /**
     * json字符串转成list
     */
    public static <T> T fromJson(String str, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }


}
