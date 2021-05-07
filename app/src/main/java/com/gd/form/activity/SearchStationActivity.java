package com.gd.form.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.Department;
import com.gd.form.model.Pipelineinfo;
import com.gd.form.model.Pipemploys;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.ToastUtil;
import com.gd.form.view.ListDialog;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchStationActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_stationNo)
    TextView tvStationNo;
    @BindView(R.id.view_stationNo)
    View viewStationNo;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.view_area)
    View viewArea;
    @BindView(R.id.tv_person)
    TextView tvPerson;
    @BindView(R.id.view_person)
    View viewPerson;
    @BindView(R.id.ll_searchByStationNo)
    LinearLayout llSearchByStationNo;
    @BindView(R.id.ll_searchByDepartmentName)
    LinearLayout llSearchByDepartmentName;
    @BindView(R.id.ll_searchByPerson)
    LinearLayout llSearchByPerson;
    @BindView(R.id.tv_startStationNo)
    TextView tvStartStationNo;
    @BindView(R.id.tv_endStationNo)
    TextView tvEndStationNo;
    @BindView(R.id.tv_departmentName)
    TextView tvDepartmentName;
    @BindView(R.id.tv_pipeName)
    TextView tvPipeName;
    @BindView(R.id.tv_manager)
    TextView tvManager;
    private String token, userId;
    private String startStationId, startPipeId, endStationId, endPipeId;
    private List<Pipelineinfo> pipeLineInfoList;
    private List<Department> departmentList;
    private List<Pipemploys> managerList;
    private int SELECT_STATION = 101;
    private int SELECT_APPROVER = 102;
    private ListDialog dialog;
    private int departmentId;
    private String approverName, approverId;
    private int selectPipeId;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_search_station;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        pipeLineInfoList = new ArrayList<>();
        departmentList = new ArrayList<>();
        managerList = new ArrayList<>();
        tvTitle.setText("台账信息维护");
        dialog = new ListDialog(this);
    }

    @OnClick({
            R.id.iv_back,
            R.id.ll_manager,
            R.id.ll_startNo,
            R.id.ll_endNo,
            R.id.ll_departmentName,
            R.id.ll_pipeName,
            R.id.btn_searchStaionNo,
            R.id.btn_searchManager,
            R.id.btn_searchDepartment,
            R.id.ll_stationNo,
            R.id.ll_area,
            R.id.ll_person})
    public void click(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_manager:
                getPipeManagerByUserId();
//                Intent intentApprover = new Intent(SearchStationActivity.this, ApproverActivity.class);
//                startActivityForResult(intentApprover, SELECT_APPROVER);
                break;
            case R.id.ll_departmentName:
                pipeDepartmentInfoGetList();
                break;
            case R.id.ll_endNo:
                if (TextUtils.isEmpty(tvStartStationNo.getText().toString().trim())) {
                    ToastUtil.show("请先选择开始桩号名称");
                    return;
                }
                Intent intentEndStation = new Intent(this, StationByIdActivity.class);
                intentEndStation.putExtra("tag", "end");
                intentEndStation.putExtra("pipeId", selectPipeId+"");
                startActivityForResult(intentEndStation, SELECT_STATION);
                break;
            case R.id.ll_startNo:
                if (TextUtils.isEmpty(tvPipeName.getText().toString().trim())) {
                    ToastUtil.show("请先选择线路");
                    return;
                }
                Intent intentStartStation = new Intent(this, StationByIdActivity.class);
                intentStartStation.putExtra("tag", "start");
                intentStartStation.putExtra("pipeId", selectPipeId+"");
                startActivityForResult(intentStartStation, SELECT_STATION);
                break;
            case R.id.ll_pipeName:
                getPipelineInfoListRequest();
                break;
            case R.id.btn_searchStaionNo:
                bundle.putString("pipeid",selectPipeId+"");
                bundle.putString("stakeid",startStationId);
                bundle.putString("estakeid",endStationId);
                openActivity(StandingBookActivity.class,bundle);
                break;
            case R.id.btn_searchManager:
                if(TextUtils.isEmpty(tvManager.getText().toString())){
                    ToastUtil.show("请选择负责人");
                    return;
                }
                bundle.putString("dptid","");
                bundle.putString("pipeid","");
                bundle.putString("empid",approverId);
                openActivity(StandingBookActivity.class,bundle);
                break;
            case R.id.btn_searchDepartment:
                if(TextUtils.isEmpty(tvDepartmentName.getText().toString())){
                    ToastUtil.show("请选择作业区");
                    return;
                }
                bundle.putString("dptid",departmentId+"");
                bundle.putString("pipeid","");
                bundle.putString("empid","");
                openActivity(StandingBookActivity.class,bundle);
                break;
            case R.id.ll_stationNo:
                tvStationNo.setTextColor(Color.parseColor("#FF52A7F9"));
                viewStationNo.setVisibility(View.VISIBLE);
                tvArea.setTextColor(Color.parseColor("#000000"));
                viewArea.setVisibility(View.INVISIBLE);
                tvPerson.setTextColor(Color.parseColor("#000000"));
                viewPerson.setVisibility(View.INVISIBLE);
                llSearchByStationNo.setVisibility(View.VISIBLE);
                llSearchByDepartmentName.setVisibility(View.GONE);
                llSearchByPerson.setVisibility(View.GONE);
                break;
            case R.id.ll_area:
                tvArea.setTextColor(Color.parseColor("#FF52A7F9"));
                viewArea.setVisibility(View.VISIBLE);
                tvStationNo.setTextColor(Color.parseColor("#000000"));
                viewStationNo.setVisibility(View.INVISIBLE);
                tvPerson.setTextColor(Color.parseColor("#000000"));
                viewPerson.setVisibility(View.INVISIBLE);
                llSearchByStationNo.setVisibility(View.GONE);
                llSearchByDepartmentName.setVisibility(View.VISIBLE);
                llSearchByPerson.setVisibility(View.GONE);
                break;
            case R.id.ll_person:
                tvPerson.setTextColor(Color.parseColor("#FF52A7F9"));
                viewPerson.setVisibility(View.VISIBLE);
                tvStationNo.setTextColor(Color.parseColor("#000000"));
                viewStationNo.setVisibility(View.INVISIBLE);
                tvArea.setTextColor(Color.parseColor("#000000"));
                viewArea.setVisibility(View.INVISIBLE);
                llSearchByStationNo.setVisibility(View.GONE);
                llSearchByDepartmentName.setVisibility(View.GONE);
                llSearchByPerson.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void getPipelineInfoListRequest() {
        JsonObject params = new JsonObject();
        params.addProperty("id", userId);
        Net.create(Api.class).getLinesByUserId(token, params)
                .enqueue(new NetCallback<List<Pipelineinfo>>(this, false) {
                    @Override
                    public void onResponse(List<Pipelineinfo> list) {
                        pipeLineInfoList = list;
                        List<String> pipeNameList = new ArrayList<>();
                        List<Integer> pipeIdList = new ArrayList<>();
                        if (pipeLineInfoList != null && pipeLineInfoList.size() > 0) {
                            for (int i = 0; i < pipeLineInfoList.size(); i++) {
                                pipeNameList.add(pipeLineInfoList.get(i).getName());
                                pipeIdList.add(pipeLineInfoList.get(i).getId());
                            }
                        }
                        dialog.setData(pipeNameList);
                        dialog.show();
                        dialog.setListItemClick(positionM -> {
                            tvPipeName.setText(pipeNameList.get(positionM));
                            selectPipeId = pipeIdList.get(positionM);
                            dialog.dismiss();
                        });
                    }
                });
    }

    private void pipeDepartmentInfoGetList() {
        JsonObject params = new JsonObject();
        params.addProperty("employid", userId);
        Net.create(Api.class).getDepartmentById(token, params)
                .enqueue(new NetCallback<List<Department>>(this, false) {
                    @Override
                    public void onResponse(List<Department> list) {
                        departmentList = list;
                        List<String> areaList = new ArrayList<>();
                        List<Integer> idList = new ArrayList<>();
                        if (departmentList != null && departmentList.size() > 0) {
                            for (int i = 0; i < departmentList.size(); i++) {
                                areaList.add(departmentList.get(i).getName());
                                idList.add(departmentList.get(i).getId());
                            }
                        }
                        dialog.setData(areaList);
                        dialog.show();
                        dialog.setListItemClick(positionM -> {
                            tvDepartmentName.setText(areaList.get(positionM));
                            departmentId = idList.get(positionM);
                            Log.i("tag", "departmentId===" + departmentId);
                            dialog.dismiss();
                        });
                    }
                });
    }
    private void getPipeManagerByUserId() {
        JsonObject params = new JsonObject();
        params.addProperty("empid", userId);
        Net.create(Api.class).getPipeManagerByUserId(token, params)
                .enqueue(new NetCallback<List<Pipemploys>>(this, false) {
                    @Override
                    public void onResponse(List<Pipemploys> list) {
                        managerList = list;
                        List<String> nameList = new ArrayList<>();
                        List<String> idList = new ArrayList<>();
                        if (managerList != null && managerList.size() > 0) {
                            for (int i = 0; i < managerList.size(); i++) {
                                nameList.add(managerList.get(i).getName());
                                idList.add(managerList.get(i).getId());
                            }
                        }
                        dialog.setData(nameList);
                        dialog.show();
                        dialog.setListItemClick(positionM -> {
                            tvManager.setText(nameList.get(positionM));
                            approverId = idList.get(positionM);
                            dialog.dismiss();
                        });
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
            String selectTag = data.getStringExtra("selectTag");
            String stationName = data.getStringExtra("stationName");

            if (!TextUtils.isEmpty(selectTag)) {
                if (selectTag.equals("start")) {
                    tvStartStationNo.setText(stationName);
                    startStationId = data.getStringExtra("stationId");
                    startPipeId = data.getStringExtra("pipeId");
                } else {
                    tvEndStationNo.setText(stationName);
                    endStationId = data.getStringExtra("stationId");
                    endPipeId = data.getStringExtra("pipeId");

                }
            }

        } else if (requestCode == SELECT_APPROVER) {
            approverName = data.getStringExtra("name");
            approverId = data.getStringExtra("id");
            if (!TextUtils.isEmpty(approverName)) {
                tvManager.setText(approverName);
            }
        }
    }
}
