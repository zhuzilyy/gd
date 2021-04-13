package com.gd.form.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.MeasureRecordModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class PipeMeasureDetailActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_pipeDepth)
    TextView tvPipeDepth;
    @BindView(R.id.tv_opticalCableDepth)
    TextView tvOpticalCableDepth;
    @BindView(R.id.tv_depthNotEnough)
    TextView tvDepthNotEnough;
    @BindView(R.id.tv_method)
    TextView tvMethod;
    private String token, userId;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_pipe_measure_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("测量记录详情");
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        getDetail();
    }

    private void getDetail() {
        //获取测量数据
        JsonObject params = new JsonObject();
        Net.create(Api.class).getMeasureRecordDetail(token, params)
                .enqueue(new NetCallback<MeasureRecordModel>(this, true) {
                    @Override
                    public void onResponse(MeasureRecordModel recordModel) {


                    }
                });
    }

    @OnClick({
            R.id.iv_back,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

        }
    }
}
