package com.gd.form.activity;

import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.jaeger.library.StatusBarUtil;

import butterknife.OnClick;

public class GhgActivity extends BaseActivity {

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
        return R.layout.activity_ghg;
    }

    @OnClick({
            R.id.iv_back,
            R.id.ll_highZone,
            R.id.ll_video_monitoring,
            R.id.ll_gdsm,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_highZone:
                openActivity(HighZoneActivity.class);
                break;
            case R.id.ll_video_monitoring:
                openActivity(VideoMonitoringActivity.class);
                break;
            case R.id.ll_gdsm:
                openActivity(MonitoringRepairActivity.class);
                break;

        }
    }
}