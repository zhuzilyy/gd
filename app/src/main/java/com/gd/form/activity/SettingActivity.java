package com.gd.form.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gd.form.BuildConfig;
import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.utils.ActivityCollectorUtil;
import com.gd.form.utils.SPUtil;
import com.gd.form.view.DeleteDialog;
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
    @BindView(R.id.tv_versionName)
    TextView tvVersionName;
    private DeleteDialog dialog;

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
        tvVersionName.setText("当前版本:V" + BuildConfig.VERSION_NAME);
        dialog = new DeleteDialog(this);
        dialog.setContent("确定退出");
        dialog.setOnClickBottomListener(new DeleteDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                ActivityCollectorUtil.finishAllActivity();
                openActivity(LoginActivity.class);
                dialog.dismiss();
            }

            @Override
            public void onNegativeClick() {
                dialog.dismiss();
            }
        });

    }

    @OnClick({
            R.id.iv_back,
            R.id.ll_changePwd,
            R.id.btn_quit,
            R.id.btn_selectFile,
            R.id.tv_versionName,
    })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_versionName:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,1);
                break;
            case R.id.btn_quit:
                dialog.show();
                break;
            case R.id.ll_changePwd:
                openActivity(ChangePwdActivity.class);
                break;
        }
    }
}
