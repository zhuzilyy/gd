package com.gd.form.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.ToastUtil;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class AddWindVaneActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_stationNo)
    TextView tvStationNo;
    @BindView(R.id.et_distance)
    EditText etDistance;
    private String stationId, pipeId, windVanes;
    private String token, userId;
    private int SELECT_STATION = 101;
    private String activityName;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_add_wind_vane;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = (String) SPUtil.get(AddWindVaneActivity.this, "token", "");
        userId = (String) SPUtil.get(AddWindVaneActivity.this, "userId", "");
        if (getIntent() != null) {
            activityName = getIntent().getExtras().getString("name");
            if(activityName.equals("windVane")){
                tvTitle.setText("添加风向标");
            }else if(activityName.equals("videoMonitoring")){
                tvTitle.setText("添加视频监控");
            }else if(activityName.equals("advocacyBoard")){
                tvTitle.setText("添加宣教栏");
            }else if(activityName.equals("other")){
                tvTitle.setText("添加地震监测等设备设施");
            }
//            stationId = getIntent().getExtras().getString("stationId");
//            pipeId = getIntent().getExtras().getString("pipeId");
//            windVanes = getIntent().getExtras().getString("windVanes");
        }
    }

    @OnClick({
            R.id.iv_back,
            R.id.btn_commit,
            R.id.ll_stationNo,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_stationNo:
                Intent intentStation = new Intent(this, StationActivity.class);
                startActivityForResult(intentStation, SELECT_STATION);
                break;
            case R.id.btn_commit:
                if (TextUtils.isEmpty(etDistance.getText().toString())) {
                    ToastUtil.show("请输入距离");
                    return;
                }
                if(TextUtils.isEmpty(tvStationNo.getText().toString().trim())){
                    ToastUtil.show("请选择桩号");
                    return;
                }
                break;
        }
    }

    //    private void addWindVanes() {
//        JsonObject params = new JsonObject();
//        params.addProperty("stakeid", Integer.valueOf(stationId));
//        params.addProperty("pipeid", Integer.valueOf(pipeId));
//        if (!TextUtils.isEmpty(windVanes)) {
//            params.addProperty("windvanes", windVanes + ";" +etName.getText().toString());
//        } else {
//            params.addProperty("windvanes", etName.getText().toString());
//        }
//        Net.create(Api.class).addWindVane(token, params)
//                .enqueue(new NetCallback<ServerModel>(this, true) {
//                    @Override
//                    public void onResponse(ServerModel result) {
//                        ToastUtil.show(result.getMsg());
//                        if (result.getCode() == Constant.SUCCESS_CODE) {
//                            Intent intent = new Intent();
//                            intent.setAction("com.action.update");
//                            sendBroadcast(intent);
//                            finish();
//                        }
//                    }
//                });
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == SELECT_STATION) {
            stationId = data.getStringExtra("stationId");
            pipeId = data.getStringExtra("pipeId");
            String stationName = data.getStringExtra("stationName");
            tvStationNo.setText(stationName);
        }

    }
}
