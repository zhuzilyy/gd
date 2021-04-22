package com.gd.form.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gd.form.R;
import com.gd.form.activity.ApproveBuildingActivity;
import com.gd.form.activity.ApproveDeviceActivity;
import com.gd.form.activity.ApproveElectricity;
import com.gd.form.activity.ApproveHiddenActivity;
import com.gd.form.activity.ApproveHighZoneActivity;
import com.gd.form.activity.ApproveHikingActivity;
import com.gd.form.activity.ApproveInsulationActivity;
import com.gd.form.activity.ApproveTunnelActivity;
import com.gd.form.activity.ApproveVideoActivity;
import com.gd.form.activity.ApproveWaterActivity;
import com.gd.form.activity.ApproveWaterProtectionActivity;
import com.gd.form.activity.ApproveWeightCarActivity;
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.adapter.WaitingAdapter;
import com.gd.form.base.BaseFragment;
import com.gd.form.model.SearchForm;
import com.gd.form.model.WaitingApproveModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.view.ListDialog;
import com.google.gson.JsonObject;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class WaitingApproveFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.tv_formType)
    TextView tvFormType;
    private WaitingAdapter adapter;
    private String token, userId;
    private List<String> formBaseCodeList, formNameList;
    private ListDialog dialog;
    private List<WaitingApproveModel> waitingApproveModelList;
    private String formBaseCode;
    private MyReceiver myReceiver;
    @Override
    protected void initView(Bundle bundle) {
        token = (String) SPUtil.get(getActivity(), "token", "");
        userId = (String) SPUtil.get(getActivity(), "userId", "");
        llNoData.setVisibility(View.GONE);
        dialog = new ListDialog(getActivity());
        formBaseCodeList = new ArrayList<>();
        formNameList = new ArrayList<>();
        refreshLayout.setOnRefreshListener(refreshLayout -> {

            refreshLayout.finishRefresh(2000);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {

            refreshLayout.finishLoadMore(2000);
        });
        initData();
        formBaseCode = "all";
        getApproveList();

        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.action.updateApprove");
        getActivity().registerReceiver(myReceiver,intentFilter);
    }

    private void getApproveList() {
        JsonObject params = new JsonObject();
        params.addProperty("employid", userId);
        params.addProperty("basecode", formBaseCode);
        params.addProperty("approvaltype", 1);
        params.addProperty("status", 3);
        Net.create(Api.class).waitingApproveList(token, params)
                .enqueue(new NetCallback<List<WaitingApproveModel>>(getActivity(), true) {
                    @Override
                    public void onResponse(List<WaitingApproveModel> list) {
                        waitingApproveModelList.clear();
                        if (list != null && list.size() > 0) {
                            waitingApproveModelList.addAll(list);
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

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_waiting_approve;
    }

    private void initData() {
        waitingApproveModelList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new WaitingAdapter(getActivity(), waitingApproveModelList, R.layout.adapter_item_waiting);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("tag", "approve");
                bundle.putString("formId", waitingApproveModelList.get(position).getFormid());
                String formName = waitingApproveModelList.get(position).getFormname();
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

    @OnClick({R.id.ll_selectDepartment})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.ll_selectDepartment:
                getFormType();
                break;
        }
    }

    //获取工单类型
    private void getFormType() {
        JsonObject params = new JsonObject();
        params.addProperty("formid", "1");
        Net.create(Api.class).getSearchForm(token, params)
                .enqueue(new NetCallback<List<SearchForm>>(getActivity(), true) {
                    @Override
                    public void onResponse(List<SearchForm> result) {
                        formBaseCodeList.clear();
                        formNameList.clear();
                        if (result.size() > 0) {
                            for (int i = 0; i < result.size(); i++) {
                                formNameList.add(result.get(i).getName());
                                formBaseCodeList.add(result.get(i).getBasecode());
                            }
                            formNameList.add("ALL");
                            formBaseCodeList.add("ALL");
                            dialog.setData(formNameList);
                            dialog.show();
                            dialog.setListItemClick(positionM -> {
                                tvFormType.setText(formNameList.get(positionM));
                                formBaseCode = formBaseCodeList.get(positionM);
                                getApproveList();
                                dialog.dismiss();
                            });
                        }
                    }
                });
    }
    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.action.updateApprove")) {
                getApproveList();
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(myReceiver!=null){
            getActivity().unregisterReceiver(myReceiver);
        }
    }
}
