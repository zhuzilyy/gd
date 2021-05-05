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
import com.gd.form.model.SearchPipeModel;
import com.gd.form.model.SearchStationModel;
import com.gd.form.model.StakeModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.ToastUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.io.Serializable;
import java.util.ArrayList;
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
    private String pipeId, stationId, employId, departmentId;
    private String token, userId;
    private String departmentName, pipeName;
    private String pipeOwners, stations, windVanes, advocacyBoard, viewMonitor, waterProtect, well, other, tunnelId;
    private MyReceiver myReceiver;
    private String highZoneId, buildingId, stationName, startStationId, endStationId;
    private SearchStationModel searchStationModel;
    private String newPipeTagId;
    private SearchPipeModel resultSearchPipeModel;

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
        tvTitle.setText("管道及附属设施");
        token = (String) SPUtil.get(StandingBookActivity.this, "token", "");
        userId = (String) SPUtil.get(StandingBookActivity.this, "userId", "");
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.action.update");
        registerReceiver(myReceiver, intentFilter);

        IntentFilter intentFilterAdd = new IntentFilter();
        intentFilter.addAction("com.action.add");
        registerReceiver(myReceiver, intentFilterAdd);
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            departmentId = bundle.getString("dptid");
            pipeId = bundle.getString("pipeid");
            employId = bundle.getString("empid");
            startStationId = bundle.getString("stakeid");
            endStationId = bundle.getString("estakeid");
        }
        getData();
    }

    private void getData() {
        JsonObject params = new JsonObject();
        if (!TextUtils.isEmpty(startStationId)) {
            params.addProperty("appempid", userId);
            params.addProperty("pipeid", pipeId);
            params.addProperty("stakeid", startStationId);
            params.addProperty("estakeid", endStationId);
            getPipeInfoByStationId(params);
        } else {
            params.addProperty("appempid", userId);
            params.addProperty("dptid", departmentId);
            params.addProperty("pipeid", pipeId);
            params.addProperty("empid", employId);
            getPipeInfo(params);
        }
    }

    private void getPipeInfoByStationId(JsonObject params) {
        Net.create(Api.class).getPipeInfoByStationId(token, params)
                .enqueue(new NetCallback<SearchPipeModel>(this, true) {
                    @Override
                    public void onResponse(SearchPipeModel searchPipeModel) {
                        resultSearchPipeModel = searchPipeModel;
                        setData(searchPipeModel);
                    }
                });
    }

    private void getPipeInfo(JsonObject params) {
        Net.create(Api.class).getPipeInfo(token, params)
                .enqueue(new NetCallback<SearchPipeModel>(this, true) {
                    @Override
                    public void onResponse(SearchPipeModel searchPipeModel) {
                        resultSearchPipeModel = searchPipeModel;
                        setData(searchPipeModel);

                    }
                });
    }

    private void setData(SearchPipeModel searchPipeModel) {
        if (searchPipeModel.getDeptCount() > 0) {
            tvArea.setText(searchPipeModel.getDptList().get(0).getName());
        }
        if (searchPipeModel.getLineCount() > 0 && TextUtils.isEmpty(tvPipeName.getText().toString())) {
            tvPipeName.setText(searchPipeModel.getLineList().get(0).getName());
        }
        if (searchPipeModel.getStakeCount() > 0) {
            tvPipeTagBaseInfo.setText("管道标识基础信息(" + searchPipeModel.getStakeCount() + ")");
        }
        if (searchPipeModel.getOwnerCount() > 0) {
            tvPipePerson.setText("管道责任人(" + searchPipeModel.getOwnerCount() + ")");
        }
        if (searchPipeModel.getStationCount() > 0) {
            tvStation.setText("场站名称(" + searchPipeModel.getStationCount() + ")");
        }
        if (searchPipeModel.getHigharesCount() > 0) {
            tvHighZone.setText("高后果区(" + searchPipeModel.getHigharesCount() + ")");
        }
        if (searchPipeModel.getLlegalCount() > 0) {
            tvBuilding.setText("违章违建(" + searchPipeModel.getLlegalCount() + ")");
        }
        if (searchPipeModel.getPipeCount() > 0) {
            tvTunnel.setText("隧道(" + searchPipeModel.getPipeCount() + ")");
        }
        if (searchPipeModel.getWaterprjCount() > 0) {
            tvWater.setText("水保工程(" + searchPipeModel.getWaterprjCount() + ")");
        }
        if (searchPipeModel.getWindCount() > 0) {
            tvWindVane.setText("风向标(" + searchPipeModel.getWindCount() + ")");
        }else{
            tvWindVane.setText("风向标");
        }
        if (searchPipeModel.getPeduralCount() > 0) {
            tvAdvocacyBoard.setText("宣教栏(" + searchPipeModel.getPeduralCount() + ")");
        }else{
            tvAdvocacyBoard.setText("宣教栏");
        }
        if (searchPipeModel.getViewCount() > 0) {
            tvVideoMonitoring.setText("视频监控(" + searchPipeModel.getViewCount() + ")");
        }else{
            tvVideoMonitoring.setText("视频监控");
        }
        if (searchPipeModel.getOtherCount() > 0) {
            tvOther.setText("其他(地震监测等设备设施)(" + searchPipeModel.getOtherCount() + ")");
        }else{
            tvOther.setText("其他(地震监测等设备设施)");
        }

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
            R.id.ll_area,
            R.id.ll_pipeName,
            R.id.ll_pipeTagBaseInfo,
            R.id.ll_pipePerson,
            R.id.ll_station,
            R.id.ll_highZone,
            R.id.ll_illegalBuilding,
            R.id.ll_tunnel,
            R.id.ll_wind_vane,
            R.id.ll_advocacy_board,
            R.id.ll_video_monitoring,
            R.id.ll_water,
            R.id.ll_other,
    })
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.ll_wind_vane:
                if (resultSearchPipeModel.getWindCount() > 0) {
                    bundle.putSerializable("winds", (Serializable) resultSearchPipeModel.getWindList());
                }
                openActivity(WindVaneListActivity.class, bundle);
                break;
            case R.id.ll_other:
                if (resultSearchPipeModel.getOtherCount() > 0) {
                    bundle.putSerializable("others", (Serializable) resultSearchPipeModel.getOtherList());
                }
                openActivity(OtherListActivity.class, bundle);
                break;
            case R.id.ll_water:
                if (resultSearchPipeModel.getWaterprjCount() > 0) {
                    bundle.putSerializable("waters", (Serializable) resultSearchPipeModel.getWaterLsit());
                }
                openActivity(WaterProtectionListActivity.class, bundle);
                break;
            case R.id.ll_video_monitoring:
                if (resultSearchPipeModel.getViewCount() > 0) {
                    bundle.putSerializable("videos", (Serializable) resultSearchPipeModel.getViewList());
                }
                openActivity(VideoMonitorListActivity.class, bundle);
                break;
            case R.id.ll_advocacy_board:
                if (resultSearchPipeModel.getPeduralCount() > 0) {
                    bundle.putSerializable("advocacyBoards", (Serializable) resultSearchPipeModel.getPreList());
                }
                openActivity(AdvocacyBoardListActivity.class, bundle);
                break;
            case R.id.ll_area:

                break;
            case R.id.ll_tunnel:
                if (resultSearchPipeModel.getPipeCount() > 0) {
                    bundle.putSerializable("tunnels", (Serializable) resultSearchPipeModel.getPipeList());
                }
                openActivity(TunnelListActivity.class, bundle);
                break;
            case R.id.ll_illegalBuilding:
                if (resultSearchPipeModel.getLlegalCount() > 0) {
                    bundle.putSerializable("buildings", (Serializable) resultSearchPipeModel.getLlegaList());
                }
                openActivity(BuildingListActivity.class, bundle);
                break;
            case R.id.ll_highZone:
                if (resultSearchPipeModel.getHigharesCount() > 0) {
                    bundle.putSerializable("highZones", (Serializable) resultSearchPipeModel.getHighList());
                }
                openActivity(HighZoneListActivity.class, bundle);
                break;
            case R.id.ll_station:
                if (resultSearchPipeModel.getStationCount() > 0) {
                    bundle.putStringArrayList("stations", (ArrayList<String>) resultSearchPipeModel.getStationList());
                }
                openActivity(StationNameActivity.class, bundle);
                break;
            case R.id.ll_pipePerson:
                if (resultSearchPipeModel.getOwnerCount() > 0) {
                    bundle.putSerializable("owners", (Serializable) resultSearchPipeModel.getOwnerList());
                }
                openActivity(PipeManagerActivity.class, bundle);
                break;
            case R.id.ll_pipeName:

                break;
            case R.id.ll_pipeTagBaseInfo:
                if (resultSearchPipeModel.getStakeCount() > 0) {
                    if (resultSearchPipeModel.getStakeCount() > 50) {
                        List<StakeModel> stakeList = new ArrayList<>(resultSearchPipeModel.getStakeList().subList(0, 49));
                        bundle.putSerializable("stakes", (Serializable) (stakeList));
                    } else {
                        bundle.putSerializable("stakes", (Serializable) resultSearchPipeModel.getStakeList());
                    }
                }
                openActivity(PipeBaseInfoActivity.class, bundle);
                break;
            //管道标识基础信息查看
            case R.id.ll_search:
                Intent intentStation = new Intent(this, StationActivity.class);
                startActivityForResult(intentStation, SELECT_STATION);
                break;
            //管道标识基础信息查看
            case R.id.btn_infoCheck:
                bundle.putString("tag", "check");
                bundle.putString("pipeName", pipeName);
                bundle.putString("departmentId", departmentId + "");
                bundle.putString("stationName", stationName);
                bundle.putString("departmentName", departmentName);
                bundle.putString("newPipeTagId", newPipeTagId);
                bundle.putSerializable("searchStationModel", searchStationModel);
                bundle.putSerializable("stationId", stationId);
                if (isCanAddProperty()) {
                    openActivity(PipeTagActivity.class, bundle);
                }
                break;
            //管道标识基础信息维护
            case R.id.btn_infoChange:
                bundle.putString("tag", "add");
                bundle.putString("pipeName", pipeName);
                bundle.putString("departmentId", departmentId + "");
                bundle.putString("stationName", stationName);
                bundle.putString("pipeId", pipeId);
                bundle.putString("departmentName", departmentName);
                bundle.putString("stationId", stationId);
                bundle.putString("newPipeTagId", newPipeTagId);
                bundle.putSerializable("searchStationModel", searchStationModel);
                if (isCanAddProperty()) {
                    openActivity(PipeTagActivity.class, bundle);
                }
                break;
            case R.id.iv_right:
                bundle.putString("tag", "new");
                bundle.putString("pipeName", pipeName);
                bundle.putString("departmentId", departmentId + "");
                bundle.putString("stationName", stationName);
                bundle.putString("departmentName", departmentName);
                bundle.putString("pipeId", pipeId);
                bundle.putSerializable("searchStationModel", searchStationModel);
                openActivity(PipeTagActivity.class, bundle);
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
                bundle.putString("highZoneId", highZoneId);
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
        Log.i("tag", "params==" + params);
        Net.create(Api.class).searchStation(token, params)
                .enqueue(new NetCallback<List<SearchStationModel>>(this, true) {
                    @Override
                    public void onResponse(List<SearchStationModel> result) {
                        if (result != null && result.size() > 0) {
                            SearchStationModel model = result.get(0);
                            searchStationModel = model;
                            stationName = model.getName();
                            String desc = model.getDesc();
                            highZoneId = model.getHighareasid();
                            buildingId = model.getLegalconstructionid();
                            tunnelId = model.getPipeaccountid();
                            String[] descArr = desc.split(":");
                            departmentName = descArr[0];
                            departmentId = model.getDepartmentid() + "";
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
                            if (!TextUtils.isEmpty(pipeOwners)) {
                                String[] pipeOwnersArr = pipeOwners.split(";");
                                tvPipePerson.setText("管道责任人(" + pipeOwnersArr.length + ")");
                            } else {
                                tvPipePerson.setText("管道责任人");
                            }
                            //站场或阀室
                            stations = model.getStations();
                            if (!TextUtils.isEmpty(stations)) {
                                String[] stationArr = stations.split(";");
                                tvStation.setText("站场或阀室(" + stationArr.length + ")");
                            } else {
                                tvStation.setText("站场或阀室");
                            }
                            //风向标
                            windVanes = model.getWindvanes();
                            if (!TextUtils.isEmpty(windVanes)) {
                                String[] windVanesArr = windVanes.split(";");
                                tvWindVane.setText("风向标(" + windVanesArr.length + ")");
                            } else {
                                tvWindVane.setText("风向标");
                            }
                            //宣传栏
                            advocacyBoard = model.getPedurail();
                            if (!TextUtils.isEmpty(advocacyBoard)) {
                                String[] advocacyBoardArray = advocacyBoard.split(";");
                                tvAdvocacyBoard.setText("宣教栏(" + advocacyBoardArray.length + ")");
                            } else {
                                tvAdvocacyBoard.setText("宣教栏");
                            }
                            //视频监控
                            viewMonitor = model.getViewmonitor();
                            if (!TextUtils.isEmpty(viewMonitor)) {
                                String[] monitorArr = viewMonitor.split(";");
                                tvVideoMonitoring.setText("视频监控(" + monitorArr.length + ")");
                            } else {
                                tvVideoMonitoring.setText("视频监控");
                            }
                            //水工(含其他)保护形势
                            waterProtect = model.getWaterprotect();
                            if (!TextUtils.isEmpty(waterProtect)) {
                                String[] waterArr = waterProtect.split(";");
                                tvWater.setText("水工(含其他)保护形势(" + waterArr.length + ")");
                            } else {
                                tvWater.setText("水工(含其他)保护形势");
                            }
                            //人井(盘缆点)桩
                            well = model.getManpile();
                            if (!TextUtils.isEmpty(well)) {
                                String[] wellArr = well.split(";");
                                tvWell.setText("人井(盘缆点)桩(" + wellArr.length + ")");
                            } else {
                                tvWell.setText("人井(盘缆点)桩");
                            }
                            //其他(隧道、地震监测等设备设施)
                            other = model.getOthers();
                            if (!TextUtils.isEmpty(other)) {
                                String[] otherArr = other.split(";");
                                tvOther.setText("其他(隧道、地震监测等设备设施)(" + otherArr.length + ")");
                            } else {
                                tvOther.setText("其他(隧道、地震监测等设备设施)");
                            }
                            //高后果区
                            if (TextUtils.isEmpty(model.getHighareasid())) {
                                tvHighZone.setText("高后果区(无)");
                            } else {
                                tvHighZone.setText("高后果区(有)");
                            }
                            //隧道
                            if (TextUtils.isEmpty(model.getPipeaccountid())) {
                                tvTunnel.setText("隧道(无)");
                            } else {
                                tvTunnel.setText("隧道(有)");
                            }
                            //违章建筑
                            if (TextUtils.isEmpty(model.getLegalconstructionid())) {
                                tvBuilding.setText("违章建筑(无)");
                            } else {
                                tvBuilding.setText("违章建筑(有)");
                            }
                        }
                    }
                });
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            if (intent.getAction().equals("com.action.update")) {
                //获取数据
                getData();
            } else if (intent.getAction().equals("com.action.add")) {
                newPipeTagId = intent.getStringExtra("id");
            }

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
