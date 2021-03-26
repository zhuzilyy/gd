package com.gd.form.net;

import com.google.gson.annotations.SerializedName;

/**
 * <p>类描述：最外层返回数据
 * <p>创建人：wh
 * <p>创建时间：2019/6/20
 */
public class Result<T> {

    public boolean success;
    public String msg;
    public String code;
    public T obj;

}
