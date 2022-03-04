package com.gd.form.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gd.form.R;
import com.gd.form.adapter.BuildingApproveAdapter;
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.BuildingApproveModel;
import com.gd.form.model.ServerModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.TimeUtil;
import com.gd.form.utils.ToastUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class BuildingApproveActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    private String token, userId;
    private List<BuildingApproveModel> buildingApproveModelList;
    private BuildingApproveAdapter buildingApproveAdapter;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_building_approve;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("违章违建");
        buildingApproveModelList = new ArrayList<>();
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        llNoData.setVisibility(View.GONE);
        initData();
        getApproveList();
    }

    private void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        buildingApproveAdapter = new BuildingApproveAdapter(this, buildingApproveModelList, R.layout.adapter_item_building_approve);
        recyclerView.setAdapter(buildingApproveAdapter);
        buildingApproveAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                BuildingApproveModel buildingApproveModel = buildingApproveModelList.get(position);
                switch (v.getId()) {
                    case R.id.btn_agree:
                        approveBuilding(buildingApproveModel, 1);
                        break;
                    case R.id.btn_disagree:
                        approveBuilding(buildingApproveModel, 0);
                        break;
                    case R.id.btn_detail:
                        Bundle bundle = new Bundle();
                        bundle.putString("tag","detail");
                        bundle.putString("stakeStatus",buildingApproveModel.getStakestatus()+"");
                        bundle.putString("buildingId",buildingApproveModel.getOtherdevid());
                        openActivity(PipeBuildingActivity.class,bundle);
                        break;
                }
            }
        });
    }

    private void getApproveList() {
        JsonObject params = new JsonObject();
        params.addProperty("appempid", userId);
        params.addProperty("devicetype", 2);
        Net.create(Api.class).buildingWaitingApproveList(token, params)
                .enqueue(new NetCallback<List<BuildingApproveModel>>(this, true) {
                    @Override
                    public void onResponse(List<BuildingApproveModel> list) {
                        if (list != null && list.size() > 0) {
                            buildingApproveModelList.addAll(list);
                            buildingApproveAdapter.notifyDataSetChanged();
                            refreshLayout.setVisibility(View.VISIBLE);
                            llNoData.setVisibility(View.GONE);
                        } else {
                            refreshLayout.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }


    private void approveBuilding(BuildingApproveModel buildingApproveModel, int status) {
        JsonObject params = new JsonObject();
        params.addProperty("appempid", userId);
        params.addProperty("id", buildingApproveModel.getOtherdevid());
        params.addProperty("stakestatus", buildingApproveModel.getStakestatus());
        params.addProperty("creatime", TimeUtil.longToFormatTimeHMS(buildingApproveModel.getCreatime().getTime()));
        params.addProperty("approvalresult", status);
        params.addProperty("approvaltime", TimeUtil.getCurrentTime());
        params.addProperty("stakeid", buildingApproveModel.getStakeid());
        params.addProperty("devicetype", 2);
        Net.create(Api.class).approveBuilding(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel serverModel) {
                        ToastUtil.show("审批成功");
                        if (serverModel.getCode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent();
                            intent.setAction("com.action.approve.building");
                            sendBroadcast(intent);
                            finish();
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
