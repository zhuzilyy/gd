package com.gd.form.model;

import java.io.Serializable;

/**
 * <p>类描述：登录返回数据bean
 * <p>创建人：wh
 * <p>创建时间：2018/6/26
 */
public class LoginBeanObj implements Serializable {
    private String realName;
    private String departName;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }
}
