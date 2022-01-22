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
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.adapter.StationNoApproveAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.ServerModel;
import com.gd.form.model.StationNoApproveModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.TimeUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class StationWaitingApproveActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    private String token, userId;
    private List<StationNoApproveModel> stationNoApproveModelList;
    private StationNoApproveAdapter approveAdapter;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_station_waiting_approve;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        tvTitle.setText("管道桩台账待审批");
        initData();
        getData();
    }

    private void initData() {
        stationNoApproveModelList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        approveAdapter = new StationNoApproveAdapter(this, stationNoApproveModelList, R.layout.adapter_item_station_waiting_approve);
        recyclerView.setAdapter(approveAdapter);
        approveAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                StationNoApproveModel model = stationNoApproveModelList.get(position);
                switch (v.getId()) {
                    case R.id.btn_detail:
                        Bundle bundle = new Bundle();
                        bundle.putString("stakeId", model.getStakeid() + "");
                        bundle.putString("stakeStatus", model.getStakestatus() + "");
                        bundle.putString("tag", "approve");
                        openActivity(PipeTagActivity.class, bundle);
                        break;
                    case R.id.btn_agree:
                        approveStation(model.getStakeid() + "", model.getStakestatus(),
                                TimeUtil.longToFormatTimeHMS(model.getCreatime().getTime()),
                                1, position);
                        break;
                    case R.id.btn_disagree:
                        approveStation(model.getStakeid() + "", model.getStakestatus(),
                                TimeUtil.longToFormatTimeHMS(model.getCreatime().getTime()),
                                0, position);
                        break;
                }
            }
        });
    }

    private void getData() {
        JsonObject params = new JsonObject();
        params.addProperty("appempid", userId);
        Net.create(Api.class).getNoApproveStation(token, params)
                .enqueue(new NetCallback<List<StationNoApproveModel>>(this, true) {
                    @Override
                    public void onResponse(List<StationNoApproveModel> stationNoApproveModels) {
                        if (stationNoApproveModels != null && stationNoApproveModels.size() > 0) {
                            stationNoApproveModelList.addAll(stationNoApproveModels);
                            approveAdapter.notifyDataSetChanged();
                            refreshLayout.setVisibility(View.VISIBLE);
                            llNoData.setVisibility(View.GONE);
                        } else {
                            refreshLayout.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void approveStation(String id, int stakeStatus, String time, int approvalResult, int index) {
        JsonObject params = new JsonObject();
        params.addProperty("appempid", userId);
        params.addProperty("id", id);
        params.addProperty("stakestatus", stakeStatus);
        params.addProperty("creatime", time);
        params.addProperty("approvalresult", approvalResult);
        params.addProperty("approvaltime", TimeUtil.longToFormatTimeHMS(System.currentTimeMillis()));
        Net.create(Api.class).approveStation(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel serverModel) {
                        if (serverModel.getCode() == Constant.SUCCESS_CODE) {
                            stationNoApproveModelList.remove(index);
                            approveAdapter.notifyDataSetChanged();
                            Intent intent = new Intent();
                            intent.setAction("com.action.update.approve");
                            sendBroadcast(intent);
                            if (stationNoApproveModelList.size() == 0) {
                                refreshLayout.setVisibility(View.GONE);
                                llNoData.setVisibility(View.VISIBLE);
                            }else{
                                refreshLayout.setVisibility(View.VISIBLE);
                                llNoData.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }

    @OnClick({R.id.iv_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
