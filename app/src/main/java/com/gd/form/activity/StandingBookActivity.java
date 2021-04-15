package com.gd.form.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.SearchStationModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.ToastUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class StandingBookActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_pipeTagBaseInfo)
    TextView tvPipeTagBaseInfo;
    @BindView(R.id.tv_pipePerson)
    TextView tvPipePerson;
    @BindView(R.id.tv_station)
    TextView tvStation;
    @BindView(R.id.tv_highZone)
    TextView tvHighZone;
    @BindView(R.id.tv_building)
    TextView tvBuilding;
    @BindView(R.id.tv_tunnel)
    TextView tvTunnel;
    @BindView(R.id.tv_windVane)
    TextView tvWindVane;
    @BindView(R.id.tv_advocacyBoard)
    TextView tvAdvocacyBoard;
    @BindView(R.id.tv_videoMonitoring)
    TextView tvVideoMonitoring;
    @BindView(R.id.tv_water)
    TextView tvWater;
    @BindView(R.id.tv_well)
    TextView tvWell;
    @BindView(R.id.tv_other)
    TextView tvOther;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_pipeName)
    TextView tvPipeName;
    @BindView(R.id.tv_pipeTag)
    TextView tvPipeTag;
    @BindView(R.id.mainSearchTextView)
    TextView mainSearchTextView;
    private int SELECT_STATION = 100;
    private String pipeId, stationId;
    private String token, userId;
    private String departmentName, pipeName;
    private String pipeOwners, stations, windVanes, advocacyBoard, viewMonitor, waterProtect, well, other, tunnelId;
    private MyReceiver myReceiver;
    private int departmentId;
    private String highZoneId, buildingId, stationName;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_standing_book;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("管道及附属设施台账");
        ivRight.setVisibility(View.VISIBLE);
        token = (String) SPUtil.get(StandingBookActivity.this, "token", "");
        userId = (String) SPUtil.get(StandingBookActivity.this, "userId", "");
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.action.update");
        registerReceiver(myReceiver, intentFilter);
    }

    @OnClick({
            R.id.iv_back,
            R.id.iv_right,
            R.id.btn_infoCheck,
            R.id.btn_infoChange,
            R.id.btn_personCheck,
            R.id.btn_personChange,
            R.id.btn_stationCheck,
            R.id.btn_stationChange,
            R.id.btn_highZoneCheck,
            R.id.btn_highZoneChange,
            R.id.btn_buildingCheck,
            R.id.btn_buildingChange,
            R.id.btn_tunnelCheck,
            R.id.btn_tunnelChange,
            R.id.btn_windVaneCheck,
            R.id.btn_windVaneChange,
            R.id.btn_advocacyBoardCheck,
            R.id.btn_advocacyBoardChange,
            R.id.btn_videoCheck,
            R.id.btn_videoChange,
            R.id.btn_waterCheck,
            R.id.btn_waterChange,
            R.id.btn_wellCheck,
            R.id.btn_wellChange,
            R.id.btn_otherCheck,
            R.id.btn_otherChange,
            R.id.ll_search,
    })
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        bundle.putString("stationId", stationId);
        bundle.putString("pipeId", pipeId);
        switch (view.getId()) {
            //管道标识基础信息查看
            case R.id.ll_search:
                Intent intentStation = new Intent(this, StationActivity.class);
                startActivityForResult(intentStation, SELECT_STATION);
                break;
            //管道标识基础信息查看
            case R.id.btn_infoCheck:
                bundle.putString("tag", "check");
                if (isCanAddProperty()) {
                    openActivity(PipeTagActivity.class, bundle);
                }
                break;
            //管道标识基础信息维护
            case R.id.btn_infoChange:
            case R.id.iv_right:
                bundle.putString("tag", "add");
                if (isCanAddProperty()) {
                    openActivity(PipeTagActivity.class, bundle);
                }
                break;
            //管道责任人查看
            case R.id.btn_personCheck:
                bundle.putString("pipeOwners", pipeOwners);
                if (isCanAddProperty()) {
                    openActivity(PipePrincipalActivity.class, bundle);
                }

                break;
            //管道责任人查看维护
            case R.id.btn_personChange:
                bundle.putString("pipeOwners", pipeOwners);
                if (isCanAddProperty()) {
                    openActivity(AddPrincipalActivity.class, bundle);
                }
                break;
            //站场或阀室查看
            case R.id.btn_stationCheck:
                bundle.putString("stations", stations);
                if (isCanAddProperty()) {
                    openActivity(PipeStandingActivity.class, bundle);
                }

                break;
            //站场或阀室维护
            case R.id.btn_stationChange:
                bundle.putString("stations", stations);
                if (isCanAddProperty()) {
                    openActivity(AddStandingActivity.class, bundle);
                }
                break;
            //高后果区信息查看
            case R.id.btn_highZoneCheck:
                bundle.putString("tag", "check");
                bundle.putString("pipeName", pipeName);
                bundle.putString("highZoneId", highZoneId);
                if (isCanAddProperty()) {
                    openActivity(PipeHighZoneActivity.class, bundle);
                }
                break;
            //高后果区信息维护
            case R.id.btn_highZoneChange:
                bundle.putString("pipeName", pipeName);
                if (isCanAddProperty()) {
                    bundle.putString("tag", "add");
                    openActivity(PipeHighZoneActivity.class, bundle);
                }
                break;
            //违章建筑信息查看
            case R.id.btn_buildingCheck:
                bundle.putString("tag", "check");
                bundle.putString("buildingId", buildingId);
                bundle.putString("stationId", stationId);
                bundle.putString("pipeName", pipeName);
                bundle.putString("stationName", stationName);
                bundle.putString("pipeId", pipeId);
                if (isCanAddProperty()) {
                    openActivity(PipeBuildingActivity.class, bundle);
                }
                break;
            //违章建筑信息维护
            case R.id.btn_buildingChange:
                if (isCanAddProperty()) {
                    bundle.putString("tag", "add");
                    bundle.putString("buildingId", buildingId);
                    bundle.putString("stationId", stationId);
                    bundle.putString("pipeName", pipeName);
                    bundle.putString("stationName", stationName);
                    bundle.putString("pipeId", pipeId);
                    openActivity(PipeBuildingActivity.class, bundle);
                }
                break;
            //隧道信息查看
            case R.id.btn_tunnelCheck:
                bundle.putString("tag", "check");
                bundle.putString("tunnelId", tunnelId);
                bundle.putString("stationId", stationId);
                bundle.putString("pipeName", pipeName);
                bundle.putString("stationName", stationName);
                bundle.putString("pipeId", pipeId);
                if (isCanAddProperty()) {
                    openActivity(PipeTunnelActivity.class, bundle);
                }
                break;
            //隧道信息维护
            case R.id.btn_tunnelChange:
                if (isCanAddProperty()) {
                    bundle.putString("tag", "add");
                    bundle.putString("tunnelId", tunnelId);
                    bundle.putString("stationId", stationId);
                    bundle.putString("pipeName", pipeName);
                    bundle.putString("stationName", stationName);
                    bundle.putString("pipeId", pipeId);
                    openActivity(PipeTunnelActivity.class, bundle);
                }
                break;
            //风向标信息查看
            case R.id.btn_windVaneCheck:
                bundle.putString("windVanes", windVanes);
                if (isCanAddProperty()) {
                    openActivity(PipeWindVaneActivity.class, bundle);
                }
                break;
            //风向标信息维护
            case R.id.btn_windVaneChange:
                bundle.putString("windVanes", windVanes);
                if (isCanAddProperty()) {
                    openActivity(AddWindVaneActivity.class, bundle);
                }
                break;
            //宣传栏信息查看
            case R.id.btn_advocacyBoardCheck:
                bundle.putString("advocacyBoard", advocacyBoard);
                if (isCanAddProperty()) {
                    openActivity(PipeAdvocacyBoardActivity.class, bundle);
                }
                break;
            //宣传栏信息维护
            case R.id.btn_advocacyBoardChange:
                bundle.putString("advocacyBoard", advocacyBoard);
                if (isCanAddProperty()) {
                    openActivity(AddAdvocacyBoardActivity.class, bundle);
                }
                break;
            //视频监控信息查看
            case R.id.btn_videoCheck:
                bundle.putString("video", viewMonitor);
                if (isCanAddProperty()) {
                    openActivity(PipeVideoActivity.class, bundle);
                }
                break;
            //视频监控信息维护
            case R.id.btn_videoChange:
                bundle.putString("video", viewMonitor);
                if (isCanAddProperty()) {
                    openActivity(AddVideoActivity.class, bundle);
                }
                break;
            //水工保护信息查看
            case R.id.btn_waterCheck:
                bundle.putString("waterProtect", waterProtect);
                if (isCanAddProperty()) {
                    openActivity(PipeWaterActivity.class, bundle);
                }
                break;
            //水工保护信息维护
            case R.id.btn_waterChange:
                bundle.putString("waterProtect", waterProtect);
                if (isCanAddProperty()) {
                    openActivity(AddWaterActivity.class, bundle);
                }
                break;
            //人井(盘缆点)桩
            case R.id.btn_wellCheck:
                bundle.putString("well", well);
                if (isCanAddProperty()) {
                    openActivity(PipeWellActivity.class, bundle);
                }
                break;
            //人井(盘缆点)桩
            case R.id.btn_wellChange:
                bundle.putString("well", well);
                if (isCanAddProperty()) {
                    openActivity(AddWellActivity.class, bundle);
                }
                break;
            //其他(隧道、地震监测等设备设施)
            case R.id.btn_otherCheck:
                bundle.putString("other", other);
                if (isCanAddProperty()) {
                    openActivity(PipeOtherActivity.class, bundle);
                }
                break;
            //其他(隧道、地震监测等设备设施)
            case R.id.btn_otherChange:
                bundle.putString("other", other);
                if (isCanAddProperty()) {
                    openActivity(AddOtherActivity.class, bundle);
                }
                break;
            case R.id.iv_back:
                finish();
                break;

        }
    }

    public boolean isCanAddProperty() {
        if (TextUtils.isEmpty(stationId)) {
            ToastUtil.show("请先选择要维护的桩号");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == SELECT_STATION) {
            stationName = data.getStringExtra("stationName");
            pipeId = data.getStringExtra("pipeId");
            stationId = data.getStringExtra("stationId");
            mainSearchTextView.setText(stationName);
            //获取数据
            searchStation(pipeId, stationId);
        }
    }

    private void searchStation(String pipeId, String stationId) {
        JsonObject params = new JsonObject();
        params.addProperty("pipeid", Integer.valueOf(pipeId));
        params.addProperty("id", Integer.valueOf(stationId));
        Log.i("tag", "params===" + params);
        Net.create(Api.class).searchStation(token, params)
                .enqueue(new NetCallback<List<SearchStationModel>>(this, true) {
                    @Override
                    public void onResponse(List<SearchStationModel> result) {
                        SearchStationModel model = result.get(0);
                        String desc = model.getDesc();
                        highZoneId = model.getHighareasid();
                        buildingId = model.getLegalconstructionid();
                        tunnelId = model.getPipeaccountid();
                        String[] descArr = desc.split(":");
                        departmentName = descArr[0];
                        departmentId = model.getDepartmentid();
                        pipeName = descArr[1];
                        if (!departmentName.equals("null")) {
                            tvArea.setText(departmentName);
                        } else {
                            tvArea.setText("暂无");
                        }
                        if (!pipeName.equals("null")) {
                            tvPipeName.setText(pipeName);
                        } else {
                            tvPipeName.setText("暂无");
                        }
                        tvPipeTag.setText(model.getName());
                        //管道责任人
                        pipeOwners = model.getPipeowners();
                        String[] pipeOwnersArr = pipeOwners.split(";");
                        if (!TextUtils.isEmpty(pipeOwners)) {
                            tvPipePerson.setText("管道责任人(" + pipeOwnersArr.length + ")");
                        } else {
                            tvPipePerson.setText("管道责任人");
                        }
                        //站场或阀室
                        stations = model.getStations();
                        String[] stationArr = stations.split(";");
                        if (!TextUtils.isEmpty(stations)) {
                            tvStation.setText("站场或阀室(" + stationArr.length + ")");
                        } else {
                            tvStation.setText("站场或阀室");
                        }
                        //风向标
                        windVanes = model.getWindvanes();
                        String[] windVanesArr = windVanes.split(";");
                        if (!TextUtils.isEmpty(windVanes)) {
                            tvWindVane.setText("风向标(" + windVanesArr.length + ")");
                        } else {
                            tvWindVane.setText("风向标");
                        }
                        //宣传栏
                        advocacyBoard = model.getPedurail();
                        String[] advocacyBoardArray = advocacyBoard.split(";");
                        if (!TextUtils.isEmpty(advocacyBoard)) {
                            tvAdvocacyBoard.setText("宣传栏(" + advocacyBoardArray.length + ")");
                        } else {
                            tvAdvocacyBoard.setText("宣传栏");
                        }
                        //视频监控
                        viewMonitor = model.getViewmonitor();
                        String[] monitorArr = viewMonitor.split(";");
                        if (!TextUtils.isEmpty(viewMonitor)) {
                            tvVideoMonitoring.setText("视频监控(" + monitorArr.length + ")");
                        } else {
                            tvVideoMonitoring.setText("视频监控");
                        }
                        //水工(含其他)保护形势
                        waterProtect = model.getWaterprotect();
                        String[] waterArr = waterProtect.split(";");
                        if (!TextUtils.isEmpty(waterProtect)) {
                            tvWater.setText("水工(含其他)保护形势(" + waterArr.length + ")");
                        } else {
                            tvWater.setText("水工(含其他)保护形势");
                        }
                        //人井(盘缆点)桩
                        well = model.getManpile();
                        String[] wellArr = well.split(";");
                        if (!TextUtils.isEmpty(well)) {
                            tvWell.setText("人井(盘缆点)桩(" + wellArr.length + ")");
                        } else {
                            tvWell.setText("人井(盘缆点)桩");
                        }
                        //其他(隧道、地震监测等设备设施)
                        other = model.getOthers();
                        String[] otherArr = other.split(";");
                        if (!TextUtils.isEmpty(other)) {
                            tvOther.setText("其他(隧道、地震监测等设备设施)(" + otherArr.length + ")");
                        } else {
                            tvOther.setText("其他(隧道、地震监测等设备设施)");
                        }
                    }
                });
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取数据
            searchStation(pipeId, stationId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }
    }
}
