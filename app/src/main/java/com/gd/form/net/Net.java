package com.gd.form.net;


/**
 * <p>类描述：网络请求
 * <p>创建人：wh
 * <p>创建时间：2019/6/20
 */
public class Net {
    public synchronized static <T> T create(Class<T> clazz) {
        return ServiceGenerator.createServiceFrom(clazz);
    }
}
