package com.gd.form.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;

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
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.color_green));
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
            R.id.ll_gdsm,
            R.id.ll_zcny,
            R.id.ll_wzwj,
            R.id.ll_tbxj,
            R.id.ll_wrj,
            R.id.ll_video_monitoring,

    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_video_monitoring:
                openActivity(VideoMonitoringActivity.class);
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_sgbh:
                openActivity(SgbhActivity.class);
                break;
            case R.id.ll_sdwb:
                openActivity(SdwbActivity.class);
                break;
            case R.id.ll_gdsm:
                openActivity(PipelineDepthActivity.class);
                break;
            case R.id.ll_zcny:
                openActivity(WeightCarActivity.class);
                break;
            case R.id.ll_wzwj:
                openActivity(EndorsementActivity.class);
                break;
            case R.id.ll_tbxj:
                openActivity(HikingCheckActivity.class);
                break;
            case R.id.ll_wrj:
                openActivity(FlyRecordActivity.class);
                break;
        }
    }
}