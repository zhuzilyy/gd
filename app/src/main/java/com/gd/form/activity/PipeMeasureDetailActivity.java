package com.gd.form.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.MeasureModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.TimeUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.util.List;

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
    @BindView(R.id.tv_tester)
    TextView tvTester;
    private String token, userId;
    private String stationId, time;

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
        if (getIntent() != null) {
            stationId = getIntent().getExtras().getString("stationId");
            time = getIntent().getExtras().getString("time");
        }
        getDetail(stationId, time);
    }

    private void getDetail(String stationId, String time) {
        //获取测量数据
        JsonObject params = new JsonObject();
        params.addProperty("stakeid", stationId);
        params.addProperty("measuredate", TimeUtil.longToFormatTimeHMS(Long.parseLong(time)));
        Net.create(Api.class).getMeasureRecordDetail(token, params)
                .enqueue(new NetCallback<List<MeasureModel>>(this, true) {
                    @Override
                    public void onResponse(List<MeasureModel> list) {
                        if (list != null && list.size() > 0) {
                            MeasureModel measureModel = list.get(0);
                            tvTime.setText(TimeUtil.longToFormatTime(measureModel.getMeasuredate().getTime()));
                            tvPipeDepth.setText(measureModel.getPipedeep() + "");
                            tvOpticalCableDepth.setText(measureModel.getCabledeep() + "");
                            tvDepthNotEnough.setText(measureModel.getLockdeep() + "");
                            tvMethod.setText(measureModel.getResolution());
                            tvTester.setText(measureModel.getTester());
                        }
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
