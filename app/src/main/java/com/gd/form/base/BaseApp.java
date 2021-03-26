package com.gd.form.base;

import android.app.Application;

/**
 * <p>类描述：Application基类
 * <p>创建人：wh
 * <p>创建时间：2019/6/20
 */
public class BaseApp extends Application {

    private static BaseApp instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }

    public static BaseApp getInstance() {
        return instance;
    }
}
