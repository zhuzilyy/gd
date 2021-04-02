package com.gd.form.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.jaeger.library.StatusBarUtil;

import butterknife.OnClick;

public class FfglActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.color_green));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_ffgl;
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({
            R.id.iv_back,
            R.id.ll_zone_electricity,
            R.id.ll_insulating_property,
            R.id.ll_gdsm,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_zone_electricity:
                openActivity(ZoneElectricityActivity.class);
                break;
            case R.id.ll_insulating_property:
                openActivity(InsulatingPropertyActivity.class);
                break;
            case R.id.ll_gdsm:
                openActivity(DeviceActivity.class);
                break;

        }
    }
}