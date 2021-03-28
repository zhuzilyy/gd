package com.gd.form.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.jaeger.library.StatusBarUtil;

import butterknife.OnClick;

public class XhglActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_xhgl;
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({
            R.id.iv_back,
            R.id.ll_sgbh,
            R.id.ll_sdwb,

    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_sgbh:
               openActivity(SgbhActivity.class);
                break;
            case R.id.ll_sdwb:
               openActivity(SdwbActivity.class);
                break;
        }
    }
}