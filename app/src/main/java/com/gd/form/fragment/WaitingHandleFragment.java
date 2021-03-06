package com.gd.form.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gd.form.R;
import com.gd.form.activity.ConcealedWorkActivity;
import com.gd.form.activity.DeviceActivity;
import com.gd.form.activity.EndorsementActivity;
import com.gd.form.activity.HighZoneActivity;
import com.gd.form.activity.HikingCheckActivity;
import com.gd.form.activity.InsulatingPropertyActivity;
import com.gd.form.activity.SdwbActivity;
import com.gd.form.activity.SgbhActivity;
import com.gd.form.activity.VideoMonitoringActivity;
import com.gd.form.activity.WaterInsuranceActivity;
import com.gd.form.activity.WeightCarActivity;
import com.gd.form.activity.ZoneElectricityActivity;
import com.gd.form.adapter.OVerTimeAdapter;
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.base.BaseFragment;
import com.gd.form.model.OverTimeModel;
import com.gd.form.model.SearchForm;
import com.gd.form.model.TaskCountModel;
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

public class WaitingHandleFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.tv_formType)
    TextView tvFormType;
    private OVerTimeAdapter adapter;
    private String token, userId;
    private List<String> formBaseCodeList, formNameList;
    private ListDialog dialog;
    private List<OverTimeModel> waitingHandleList;
    private MyReceiver myReceiver;
    private String employId;

    @Override
    protected void initView(Bundle bundle) {
        waitingHandleList = new ArrayList<>();
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
        if (getActivity().getIntent() != null) {
            if (getActivity().getIntent().getExtras() != null) {
                employId = getActivity().getIntent().getExtras().getString("employId");
            }
        }
        getOverTimeList();
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.action.update.waitingTask");
        getActivity().registerReceiver(myReceiver, intentFilter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_waiting_handle;
    }

    private void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new OVerTimeAdapter(getActivity(), waitingHandleList, R.layout.adapter_item_waiting);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                String formName = waitingHandleList.get(position).getFormname();
                switch (formName) {
                    case "?????????????????????":
                        openActivity(SgbhActivity.class);
                        break;
                    case "?????????????????????":
                        openActivity(SdwbActivity.class);
                        break;
                    case "?????????????????????":
                        openActivity(WeightCarActivity.class);
                        break;
                    case "????????????????????????":
                        openActivity(EndorsementActivity.class);
                        break;
                    case "??????????????????????????????":
                        openActivity(HikingCheckActivity.class);
                        break;
                    case "????????????????????????":
                        openActivity(WaterInsuranceActivity.class);
                        break;
                    case "????????????????????????":
                        openActivity(ConcealedWorkActivity.class);
                        break;
                    case "???????????????????????????":
                        openActivity(HighZoneActivity.class);
                        break;
                    case "????????????????????????":
                        openActivity(VideoMonitoringActivity.class);
                        break;
                    case "????????????????????????":
                        openActivity(ZoneElectricityActivity.class);
                        break;
                    case "?????????????????????":
                        openActivity(InsulatingPropertyActivity.class);
                        break;
                    case "??????????????????":
                        openActivity(DeviceActivity.class);
                        break;
                }
            }
        });

    }

    private void getOverTimeList() {
        JsonObject params = new JsonObject();
        if (!TextUtils.isEmpty(employId)) {
            params.addProperty("empid", employId);
        } else {
            params.addProperty("empid", userId);
        }

        Net.create(Api.class).getTaskTotal(token, params)
                .enqueue(new NetCallback<TaskCountModel>(getActivity(), true) {
                    @Override
                    public void onResponse(TaskCountModel taskCountModel) {
                        waitingHandleList.clear();
                        List<OverTimeModel> waitTaskList = taskCountModel.getWaitTaskList();
                        if (waitTaskList != null && waitTaskList.size() > 0) {
                            waitingHandleList.addAll(waitTaskList);
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

    @OnClick({R.id.ll_selectDepartment})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.ll_selectDepartment:
                getFormType();
                break;
        }
    }

    //??????????????????
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
                                dialog.dismiss();
                            });
                        }
                    }
                });
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.action.update.waitingTask")) {
                getOverTimeList();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myReceiver != null) {
            getActivity().unregisterReceiver(myReceiver);
        }
    }
}
