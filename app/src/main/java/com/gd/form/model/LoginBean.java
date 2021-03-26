package com.gd.form.model;

/**

 */
public class LoginBean {

    private String msg;
    private boolean success;
    private LoginBeanObj obj;

    public LoginBeanObj getObj() {
        return obj;
    }

    public void setObj(LoginBeanObj obj) {
        this.obj = obj;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
