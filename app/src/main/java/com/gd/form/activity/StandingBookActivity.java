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
import com.gd.form.model.Pipelineinfo;
import com.gd.form.model.SearchPipeModel;
import com.gd.form.model.SearchStationModel;
import com.gd.form.model.StakeModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.ToastUtil;
import com.gd.form.view.ListDialog;
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
    private String newPipeTagId,stakeType;
    private SearchPipeModel resultSearchPipeModel;
    private ListDialog dialog;
    private List<Pipelineinfo> pipeList;
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
        tvTitle.setText("?????????????????????");
        dialog = new ListDialog(this);
        pipeList = new ArrayList<>();
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
            stakeType = bundle.getString("staketype");
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
            params.addProperty("staketype", stakeType);
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
        Log.i("tag","params==="+params);
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
        Log.i("tag","params=="+params);
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
            departmentId = searchPipeModel.getDptList().get(0).getId()+"";
        }
        if (searchPipeModel.getLineCount() > 0 && TextUtils.isEmpty(tvPipeName.getText().toString())) {
            tvPipeName.setText(searchPipeModel.getLineList().get(0).getName());
            pipeList = searchPipeModel.getLineList();
            pipeId = searchPipeModel.getLineList().get(0).getId()+"";
        }
        if (searchPipeModel.getStakeCount() > 0) {
            tvPipeTagBaseInfo.setText("????????????????????????(" + searchPipeModel.getStakeCount() + ")");
        }else{
            tvPipeTagBaseInfo.setText("????????????????????????");
        }
        if (searchPipeModel.getOwnerCount() > 0) {
            tvPipePerson.setText("???????????????(" + searchPipeModel.getOwnerCount() + ")");
        }else{
            tvPipePerson.setText("???????????????");
        }
        if (searchPipeModel.getStationCount() > 0) {
            tvStation.setText("????????????(" + searchPipeModel.getStationCount() + ")");
        }else{
            tvStation.setText("????????????");
        }
        if (searchPipeModel.getHigharesCount() > 0) {
            tvHighZone.setText("????????????(" + searchPipeModel.getHigharesCount() + ")");
        }else{
            tvHighZone.setText("????????????");
        }
        if (searchPipeModel.getLlegalCount() > 0) {
            tvBuilding.setText("????????????(" + searchPipeModel.getLlegalCount() + ")");
        }else{
            tvBuilding.setText("????????????");
        }
        if (searchPipeModel.getPipeCount() > 0) {
            tvTunnel.setText("??????(" + searchPipeModel.getPipeCount() + ")");
        }else{
            tvTunnel.setText("??????");
        }
        if (searchPipeModel.getWaterprjCount() > 0) {
            tvWater.setText("????????????(" + searchPipeModel.getWaterprjCount() + ")");
        }else{
            tvWater.setText("????????????");
        }
        if (searchPipeModel.getWindCount() > 0) {
            tvWindVane.setText("?????????(" + searchPipeModel.getWindCount() + ")");
        }else{
            tvWindVane.setText("?????????");
        }
        if (searchPipeModel.getPeduralCount() > 0) {
            tvAdvocacyBoard.setText("?????????(" + searchPipeModel.getPeduralCount() + ")");
        }else{
            tvAdvocacyBoard.setText("?????????");
        }
        if (searchPipeModel.getViewCount() > 0) {
            tvVideoMonitoring.setText("????????????(" + searchPipeModel.getViewCount() + ")");
        }else{
            tvVideoMonitoring.setText("????????????");
        }
        if (searchPipeModel.getOtherCount() > 0) {
            tvOther.setText("??????(???????????????????????????)(" + searchPipeModel.getOtherCount() + ")");
        }else{
            tvOther.setText("??????(???????????????????????????)");
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
                bundle.putString("departmentId",departmentId);
                bundle.putString("pipeId",pipeId);
                openActivity(WindVaneListActivity.class, bundle);
                break;
            case R.id.ll_other:
                if (resultSearchPipeModel.getOtherCount() > 0) {
                    bundle.putSerializable("others", (Serializable) resultSearchPipeModel.getOtherList());
                }
                bundle.putString("departmentId",departmentId);
                bundle.putString("pipeId",pipeId);
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
                bundle.putString("departmentId",departmentId);
                bundle.putString("pipeId",pipeId);
                openActivity(VideoMonitorListActivity.class, bundle);
                break;
            case R.id.ll_advocacy_board:
                if (resultSearchPipeModel.getPeduralCount() > 0) {
                    bundle.putSerializable("advocacyBoards", (Serializable) resultSearchPipeModel.getPreList());
                }
                bundle.putString("departmentId",departmentId);
                bundle.putString("pipeId",pipeId);
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
                if(pipeList.size()==0 ){
                    ToastUtil.show("????????????");
                    return;
                }
                //??????????????????
                List<String>  pipeNameList= new ArrayList<>();
                List<String>  pipeIdList= new ArrayList<>();
                if(pipeList!=null && pipeList.size()>0){
                    for (int i = 0; i <pipeList.size() ; i++) {
                        pipeNameList.add(pipeList.get(i).getName());
                        pipeIdList.add(pipeList.get(i).getId()+"");
                    }
                }
                dialog.setData(pipeNameList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvPipeName.setText(pipeNameList.get(positionM));
                    JsonObject params = new JsonObject();
                    params.addProperty("appempid", userId);
                    params.addProperty("dptid", departmentId);
                    params.addProperty("pipeid", pipeIdList.get(positionM));
                    params.addProperty("empid", employId);
                    getPipeInfo(params);
                    dialog.dismiss();
                });
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
            //??????????????????????????????
            case R.id.ll_search:
                Intent intentStation = new Intent(this, StationActivity.class);
                startActivityForResult(intentStation, SELECT_STATION);
                break;
            //??????????????????????????????
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
            //??????????????????????????????
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
            //?????????????????????
            case R.id.btn_personCheck:
                bundle.putString("pipeOwners", pipeOwners);
                if (isCanAddProperty()) {
                    openActivity(PipePrincipalActivity.class, bundle);
                }

                break;
            //???????????????????????????
            case R.id.btn_personChange:
                bundle.putString("pipeOwners", pipeOwners);
                if (isCanAddProperty()) {
                    openActivity(AddPrincipalActivity.class, bundle);
                }
                break;
            //?????????????????????
            case R.id.btn_stationCheck:
                bundle.putString("stations", stations);
                if (isCanAddProperty()) {
                    openActivity(PipeStandingActivity.class, bundle);
                }

                break;
            //?????????????????????
            case R.id.btn_stationChange:
                bundle.putString("stations", stations);
                if (isCanAddProperty()) {
                    openActivity(AddStandingActivity.class, bundle);
                }
                break;
            //????????????????????????
            case R.id.btn_highZoneCheck:
                bundle.putString("tag", "check");
                bundle.putString("pipeName", pipeName);
                bundle.putString("highZoneId", highZoneId);
                if (isCanAddProperty()) {
                    openActivity(PipeHighZoneActivity.class, bundle);
                }
                break;
            //????????????????????????
            case R.id.btn_highZoneChange:
                bundle.putString("pipeName", pipeName);
                bundle.putString("highZoneId", highZoneId);
                if (isCanAddProperty()) {
                    bundle.putString("tag", "add");
                    openActivity(PipeHighZoneActivity.class, bundle);
                }
                break;
            //????????????????????????
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
            //????????????????????????
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
            //??????????????????
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
            //??????????????????
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
            //?????????????????????
            case R.id.btn_windVaneCheck:
                bundle.putString("windVanes", windVanes);
                if (isCanAddProperty()) {
                    openActivity(PipeWindVaneActivity.class, bundle);
                }
                break;
            //?????????????????????
            case R.id.btn_windVaneChange:
                bundle.putString("windVanes", windVanes);
                if (isCanAddProperty()) {
                    openActivity(AddWindVaneActivity.class, bundle);
                }
                break;
            //?????????????????????
            case R.id.btn_advocacyBoardCheck:
                bundle.putString("advocacyBoard", advocacyBoard);
                if (isCanAddProperty()) {
                    openActivity(PipeAdvocacyBoardActivity.class, bundle);
                }
                break;
            //?????????????????????
            case R.id.btn_advocacyBoardChange:
                bundle.putString("advocacyBoard", advocacyBoard);
                if (isCanAddProperty()) {
                    openActivity(AddAdvocacyBoardActivity.class, bundle);
                }
                break;
            //????????????????????????
            case R.id.btn_videoCheck:
                bundle.putString("video", viewMonitor);
                if (isCanAddProperty()) {
                    openActivity(PipeVideoActivity.class, bundle);
                }
                break;
            //????????????????????????
            case R.id.btn_videoChange:
                bundle.putString("video", viewMonitor);
                if (isCanAddProperty()) {
                    openActivity(AddVideoActivity.class, bundle);
                }
                break;
            //????????????????????????
            case R.id.btn_waterCheck:
                bundle.putString("waterProtect", waterProtect);
                if (isCanAddProperty()) {
                    openActivity(PipeWaterActivity.class, bundle);
                }
                break;
            //????????????????????????
            case R.id.btn_waterChange:
                bundle.putString("waterProtect", waterProtect);
                if (isCanAddProperty()) {
                    openActivity(AddWaterActivity.class, bundle);
                }
                break;
            //??????(?????????)???
            case R.id.btn_wellCheck:
                bundle.putString("well", well);
                if (isCanAddProperty()) {
                    openActivity(PipeWellActivity.class, bundle);
                }
                break;
            //??????(?????????)???
            case R.id.btn_wellChange:
                bundle.putString("well", well);
                if (isCanAddProperty()) {
                    openActivity(AddWellActivity.class, bundle);
                }
                break;
            //??????(????????????????????????????????????)
            case R.id.btn_otherCheck:
                bundle.putString("other", other);
                if (isCanAddProperty()) {
                    openActivity(PipeOtherActivity.class, bundle);
                }
                break;
            //??????(????????????????????????????????????)
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
            ToastUtil.show("??????????????????????????????");
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
            //????????????
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
                                tvArea.setText("??????");
                            }
                            if (!pipeName.equals("null")) {
                                tvPipeName.setText(pipeName);
                            } else {
                                tvPipeName.setText("??????");
                            }
                            tvPipeTag.setText(model.getName());
                            //???????????????
                            pipeOwners = model.getPipeowners();
                            if (!TextUtils.isEmpty(pipeOwners)) {
                                String[] pipeOwnersArr = pipeOwners.split(";");
                                tvPipePerson.setText("???????????????(" + pipeOwnersArr.length + ")");
                            } else {
                                tvPipePerson.setText("???????????????");
                            }
                            //???????????????
                            stations = model.getStations();
                            if (!TextUtils.isEmpty(stations)) {
                                String[] stationArr = stations.split(";");
                                tvStation.setText("???????????????(" + stationArr.length + ")");
                            } else {
                                tvStation.setText("???????????????");
                            }
                            //?????????
                            windVanes = model.getWindvanes();
                            if (!TextUtils.isEmpty(windVanes)) {
                                String[] windVanesArr = windVanes.split(";");
                                tvWindVane.setText("?????????(" + windVanesArr.length + ")");
                            } else {
                                tvWindVane.setText("?????????");
                            }
                            //?????????
                            advocacyBoard = model.getPedurail();
                            if (!TextUtils.isEmpty(advocacyBoard)) {
                                String[] advocacyBoardArray = advocacyBoard.split(";");
                                tvAdvocacyBoard.setText("?????????(" + advocacyBoardArray.length + ")");
                            } else {
                                tvAdvocacyBoard.setText("?????????");
                            }
                            //????????????
                            viewMonitor = model.getViewmonitor();
                            if (!TextUtils.isEmpty(viewMonitor)) {
                                String[] monitorArr = viewMonitor.split(";");
                                tvVideoMonitoring.setText("????????????(" + monitorArr.length + ")");
                            } else {
                                tvVideoMonitoring.setText("????????????");
                            }
                            //??????(?????????)????????????
                            waterProtect = model.getWaterprotect();
                            if (!TextUtils.isEmpty(waterProtect)) {
                                String[] waterArr = waterProtect.split(";");
                                tvWater.setText("??????(?????????)????????????(" + waterArr.length + ")");
                            } else {
                                tvWater.setText("??????(?????????)????????????");
                            }
                            //??????(?????????)???
                            well = model.getManpile();
                            if (!TextUtils.isEmpty(well)) {
                                String[] wellArr = well.split(";");
                                tvWell.setText("??????(?????????)???(" + wellArr.length + ")");
                            } else {
                                tvWell.setText("??????(?????????)???");
                            }
                            //??????(????????????????????????????????????)
                            other = model.getOthers();
                            if (!TextUtils.isEmpty(other)) {
                                String[] otherArr = other.split(";");
                                tvOther.setText("??????(????????????????????????????????????)(" + otherArr.length + ")");
                            } else {
                                tvOther.setText("??????(????????????????????????????????????)");
                            }
                            //????????????
                            if (TextUtils.isEmpty(model.getHighareasid())) {
                                tvHighZone.setText("????????????(???)");
                            } else {
                                tvHighZone.setText("????????????(???)");
                            }
                            //??????
                            if (TextUtils.isEmpty(model.getPipeaccountid())) {
                                tvTunnel.setText("??????(???)");
                            } else {
                                tvTunnel.setText("??????(???)");
                            }
                            //????????????
                            if (TextUtils.isEmpty(model.getLegalconstructionid())) {
                                tvBuilding.setText("????????????(???)");
                            } else {
                                tvBuilding.setText("????????????(???)");
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
                //????????????
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
