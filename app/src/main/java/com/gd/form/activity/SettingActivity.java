package com.gd.form.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.utils.ActivityCollectorUtil;
import com.gd.form.utils.SPUtil;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_tel)
    TextView tvTel;
    @BindView(R.id.tv_mail)
    TextView tvMail;
    @BindView(R.id.tv_workingName)
    TextView tvWorkingName;
    @BindView(R.id.tv_departmentName)
    TextView tvDepartmentName;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("设置");
        String employName = (String) SPUtil.get(this, "employName", "");
        String workingName = (String) SPUtil.get(this, "workingName", "");
        String dptName = (String) SPUtil.get(this, "dptName", "");
        String telNumber = (String) SPUtil.get(this, "telNumber", "");
        String mail = (String) SPUtil.get(this, "mail", "");
        tvName.setText(employName);
        tvWorkingName.setText(workingName);
        tvDepartmentName.setText(dptName);
        tvTel.setText(telNumber);
        tvMail.setText(mail);
    }

    @OnClick({
            R.id.iv_back,
            R.id.ll_changePwd,
            R.id.btn_quit
    })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_quit:
                ActivityCollectorUtil.finishAllActivity();
                openActivity(LoginActivity.class);
                break;
            case R.id.ll_changePwd:
                openActivity(ChangePwdActivity.class);
                break;
        }
    }
}
