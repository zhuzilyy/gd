package com.gd.form.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gd.form.R;
import com.gd.form.adapter.OVerTimeAdapter;
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.OverTimeModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RefuseTaskActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    private OVerTimeAdapter adapter;
    private String token, userId;
    private List<OverTimeModel> refuseList;
    private MyReceiver myReceiver;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_refuse;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("审核未通过记录");
        refuseList = new ArrayList<>();
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        llNoData.setVisibility(View.GONE);
        refreshLayout.setOnRefreshListener(refreshLayout -> {

            refreshLayout.finishRefresh(2000);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {

            refreshLayout.finishLoadMore(2000);
        });
        initData();
        getOverTimeList();
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.action.update.waitingTask");
        registerReceiver(myReceiver,intentFilter);
    }

    private void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OVerTimeAdapter(this, refuseList, R.layout.adapter_item_waiting);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                String formName = refuseList.get(position).getFormname();
                Bundle bundle = new Bundle();
                bundle.putString("tag", "update");
                bundle.putString("formId", refuseList.get(position).getFormid());
                Log.i("tag", "formId==" + refuseList.get(position).getFormid());
                switch (formName) {
                    case "水工保护巡检表":
                        openActivity(ApproveWaterProtectionActivity.class, bundle);
                        break;
                    case "隧道外部检查表":
                        openActivity(ApproveTunnelActivity.class, bundle);
                        break;
                    case "重车碾压调查表":
                        openActivity(ApproveWeightCarActivity.class, bundle);
                        break;
                    case "违章违建处理记录":
                        openActivity(ApproveBuildingActivity.class, bundle);
                        break;
                    case "徒步巡检表（结对子）":
                        openActivity(ApproveHikingActivity.class, bundle);
                        break;
                    case "水工施工检查日志":
                        openActivity(ApproveWaterActivity.class, bundle);
                        break;
                    case "隐蔽工程检查记录":
                        openActivity(ApproveHiddenActivity.class, bundle);
                        break;
                    case "高后果区徒步巡检表":
                        openActivity(ApproveHighZoneActivity.class, bundle);
                        break;
                    case "视频监控查看记录":
                        openActivity(ApproveVideoActivity.class, bundle);
                        break;
                    case "区域阴保电位测试":
                        openActivity(ApproveElectricity.class, bundle);
                        break;
                    case "阀室绝缘件性能测试":
                        openActivity(ApproveInsulationActivity.class, bundle);
                        break;
                    case "去耦合器测试":
                        openActivity(ApproveDeviceActivity.class, bundle);
                        break;
                }
            }
        });

    }

    private void getOverTimeList() {
        JsonObject params = new JsonObject();
        params.addProperty("employid", userId);
        params.addProperty("basecode", "ALL");
        params.addProperty("status", 0);
        Net.create(Api.class).getRefuseList(token, params)
                .enqueue(new NetCallback<List<OverTimeModel>>(this, true) {
                    @Override
                    public void onResponse(List<OverTimeModel> list) {
                        refuseList.clear();
                        if (list != null && list.size() > 0) {
                            refuseList.addAll(list);
                            adapter.notifyDataSetChanged();
                            refreshLayout.setVisibility(View.VISIBLE);
                            llNoData.setVisibility(View.GONE);
                        } else {
                            refreshLayout.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
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
    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent!=null){
                if(intent.getAction().equals("com.action.update.waitingTask")){
                    getOverTimeList();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myReceiver!=null){
            unregisterReceiver(myReceiver);
        }
    }
}
