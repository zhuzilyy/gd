package com.gd.form.activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.LoginModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.ToastUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_username)
    EditText etUserName;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.cb_rememberPwd)
    CheckBox cb_rememberPwd;
    private String userName, pwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userName = (String) SPUtil.get(LoginActivity.this, "userName", "");
        pwd = (String) SPUtil.get(LoginActivity.this, "pwd", "");
        etUserName.setText(userName);
        etPwd.setText(pwd);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_login;
    }

    @OnClick({
            R.id.bt_login,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                if (TextUtils.isEmpty(etUserName.getText().toString())) {
                    ToastUtil.show("请输入用户名");
                    return;
                }
                if (TextUtils.isEmpty(etPwd.getText().toString())) {
                    ToastUtil.show("请输入密码");
                    return;
                }
                if (cb_rememberPwd.isChecked()) {
                    SPUtil.put(LoginActivity.this, "userName", etUserName.getText().toString());
                    SPUtil.put(LoginActivity.this, "pwd", etPwd.getText().toString());
                } else {
                    SPUtil.put(LoginActivity.this, "userName", "");
                    SPUtil.put(LoginActivity.this, "pwd", "");
                }
                login(etUserName.getText().toString(), etPwd.getText().toString());
                break;

        }
    }

    //登录
    private void login(String userName, String pwd) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", userName);
        jsonObject.addProperty("password", pwd);
        Net.create(Api.class).login(jsonObject)
                .enqueue(new NetCallback<LoginModel>(this, true) {
                    @Override
                    public void onResponse(LoginModel loginModel) {
                        if (loginModel.getCode() == Constant.SUCCESS_CODE) {
                            SPUtil.put(LoginActivity.this, "token", loginModel.getMsg());
                            SPUtil.put(LoginActivity.this, "userId", userName);
                            SPUtil.put(LoginActivity.this, "employName", loginModel.getEmployname());
                            SPUtil.put(LoginActivity.this, "workingName", loginModel.getFessionname());
                            SPUtil.put(LoginActivity.this, "dptName", loginModel.getDptname());
                            SPUtil.put(LoginActivity.this, "telNumber", loginModel.getTelenumber());
                            SPUtil.put(LoginActivity.this, "mail", loginModel.getMail());
                            SPUtil.put(LoginActivity.this, "roleId", loginModel.getRoleid());
                            SPUtil.put(LoginActivity.this, "departmentId", loginModel.getDptid() + "");
                            ToastUtil.show("登录成功");
                            openActivity(MainActivity.class);
                            finish();
                        } else {
                            ToastUtil.show(loginModel.getMsg());
                        }
                    }
                });

    }

}
