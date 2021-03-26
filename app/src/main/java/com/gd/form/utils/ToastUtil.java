package com.gd.form.utils;

import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.StringRes;

import com.gd.form.app.App;

/**
 * <p>类描述：Toast工具类
 * <p>创建人：wh
 * <p>创建时间：2019/6/20
 */

public class ToastUtil {

    private static Toast toast;

    /**
     * 解决间隔时间短的情况下，toast显示不连贯的问题
     */
    public static void show(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(App.getInstance(), msg, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.setText(msg);
            toast.show();
        }

    }
    /**
     * 解决间隔时间短的情况下，toast显示不连贯的问题
     */
    public static void show(@StringRes int msg) {
        if (toast == null) {
            toast = Toast.makeText(App.getInstance(), msg, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.setText(msg);
            toast.show();
        }
    }
}
