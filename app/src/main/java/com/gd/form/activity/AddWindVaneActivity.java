package com.gd.form.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.gd.form.utils.NumberUtil;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.ToastUtil;
import com.google.gson.JsonObject;
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
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.ll_name)
    LinearLayout llName;
    @BindView(R.id.view_name)
    View view_name;
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
            if (activityName.equals("windVane")) {
                tvTitle.setText("添加风向标");
            } else if (activityName.equals("videoMonitoring")) {
                tvTitle.setText("添加视频监控");
            } else if (activityName.equals("advocacyBoard")) {
                tvTitle.setText("添加宣教栏");
            } else if (activityName.equals("other")) {
                tvTitle.setText("添加地震监测等设备设施");
                llName.setVisibility(View.VISIBLE);
                view_name.setVisibility(View.VISIBLE);
            }
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
                if (TextUtils.isEmpty(tvStationNo.getText().toString().trim())) {
                    ToastUtil.show("请选择桩号");
                    return;
                }
                if (!NumberUtil.isNumber(etDistance.getText().toString())) {
                    ToastUtil.show("距离格式输入不正确");
                    return;
                }
                if (activityName.equals("other")) {
                    if (TextUtils.isEmpty(etName.getText().toString())) {
                        ToastUtil.show("名称不能为空");
                        return;
                    }
                }
                if (activityName.equals("windVane")) {
                    addWindVanes(etDistance.getText().toString());
                }else if(activityName.equals("advocacyBoard")){
                    addBoard(etDistance.getText().toString());
                }else if(activityName.equals("videoMonitoring")){
                    addVideo(etDistance.getText().toString());
                }else if(activityName.equals("other")){
                    addOther(etDistance.getText().toString(),etName.getText().toString());
                }
                break;
        }
    }

    private void addWindVanes(String distance) {
        JsonObject params = new JsonObject();
        params.addProperty("stakeid", Integer.valueOf(stationId));
        params.addProperty("windvanes", distance);
        Net.create(Api.class).addWindVane(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        ToastUtil.show(result.getMsg());
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent();
                            intent.putExtra("name",tvStationNo.getText().toString());
                            intent.putExtra("distance",etDistance.getText().toString());
                            intent.putExtra("id",stationId);
                            intent.setAction("com.action.update");
                            sendBroadcast(intent);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    }
                });
    }
    private void addBoard(String distance) {
        JsonObject params = new JsonObject();
        params.addProperty("stakeid", Integer.valueOf(stationId));
        params.addProperty("pedurail", distance);
        Net.create(Api.class).addBoard(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        ToastUtil.show(result.getMsg());
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent();
                            intent.putExtra("name",tvStationNo.getText().toString());
                            intent.putExtra("distance",etDistance.getText().toString());
                            intent.putExtra("id",stationId);
                            intent.setAction("com.action.update");
                            sendBroadcast(intent);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    }
                });
    }
    private void addVideo(String distance) {
        JsonObject params = new JsonObject();
        params.addProperty("stakeid", Integer.valueOf(stationId));
        params.addProperty("viewmonitor", distance);
        Net.create(Api.class).addVideoMonitoring(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        ToastUtil.show(result.getMsg());
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent();
                            intent.putExtra("name",tvStationNo.getText().toString());
                            intent.putExtra("distance",etDistance.getText().toString());
                            intent.putExtra("id",stationId);
                            intent.setAction("com.action.update");
                            sendBroadcast(intent);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    }
                });
    }
    private void addOther(String distance,String name) {
        JsonObject params = new JsonObject();
        params.addProperty("stakeid", Integer.valueOf(stationId));
        params.addProperty("othername", name);
        params.addProperty("others", distance);
        Net.create(Api.class).addOthers(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        ToastUtil.show(result.getMsg());
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent();
                            intent.putExtra("name",tvStationNo.getText().toString());
                            intent.putExtra("distance",etDistance.getText().toString());
                            intent.putExtra("otherName",name);
                            intent.putExtra("id",stationId);
                            intent.setAction("com.action.update");
                            sendBroadcast(intent);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                    }
                });
    }

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
