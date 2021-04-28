package com.gd.form.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.ServerModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.ToastUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePwdActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_originPwd)
    EditText etOriginPwd;
    @BindView(R.id.et_newPwd)
    EditText etNewPwd;
    @BindView(R.id.et_againPwd)
    EditText etAgainPwd;
    private String token, userId;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_change_pwd;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("修改密码");
        token = (String) SPUtil.get(ChangePwdActivity.this, "token", "");
        userId = (String) SPUtil.get(ChangePwdActivity.this, "userId", "");
    }

    @OnClick({
            R.id.iv_back,
            R.id.btn_confirm,
    })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(etOriginPwd.getText().toString())) {
                    ToastUtil.show("原密码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(etNewPwd.getText().toString())) {
                    ToastUtil.show("新密码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(etAgainPwd.getText().toString())) {
                    ToastUtil.show("确认密码不能为空");
                    return;
                }
                if (etNewPwd.getText().toString().length() < 6) {
                    ToastUtil.show("新密码不能小于6");
                    return;
                }
                if (!etNewPwd.getText().toString().trim().equals(etAgainPwd.getText().toString().trim())) {
                    ToastUtil.show("两次密码输入不一致");
                    return;
                }
                changePwd(etOriginPwd.getText().toString(), etNewPwd.getText().toString());
                break;
        }
    }

    private void changePwd(String originPwd, String newPwd) {
        JsonObject params = new JsonObject();
        params.addProperty("id", userId);
        params.addProperty("oldpassword", originPwd);
        params.addProperty("password", newPwd);
        Net.create(Api.class).changePwd(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        ToastUtil.show(result.getMsg());
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            finish();
                        }
                    }
                });
    }

}
