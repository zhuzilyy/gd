package com.gd.form.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.gd.form.adapter.SearchTaskAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.WaitingHandleTaskModel;
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

public class SearchTaskResultActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private SearchTaskAdapter adapter;
    private String token, userId;
    private List<WaitingHandleTaskModel> waitingTaskModelList;
    private MyReceiver myReceiver;
    private String employId,startTime,endTime;
    private int departmentId,taskStatus;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_search_task_result;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("任务查询结果");
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        llNoData.setVisibility(View.GONE);
        refreshLayout.setOnRefreshListener(refreshLayout -> {

            refreshLayout.finishRefresh(2000);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {

            refreshLayout.finishLoadMore(2000);
        });
        if (getIntent() != null) {
            Bundle extras = getIntent().getExtras();
            departmentId = extras.getInt("departmentId");
            employId = extras.getString("employId");
            startTime = extras.getString("startTime");
            endTime = extras.getString("endTime");
            taskStatus = Integer.parseInt(extras.getString("taskStatus"));
            getTask(departmentId, employId, startTime, endTime, taskStatus);
        }
        initData();
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.action.update.task");
        registerReceiver(myReceiver, intentFilter);
    }

    private void initData() {
        waitingTaskModelList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchTaskAdapter(this, waitingTaskModelList, R.layout.adapter_item_waiting_handle_task);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                WaitingHandleTaskModel waitingHandleTaskModel = waitingTaskModelList.get(position);
                int taskStatus = waitingHandleTaskModel.getTaskstatus();
                Bundle bundle = new Bundle();
                bundle.putString("taskId", waitingHandleTaskModel.getId() + "");
                if(taskStatus == 1 || taskStatus == 3){
                    bundle.putString("tag", "unFinish");
                }else{
                    bundle.putString("tag", "finished");
                }
                openActivity(TaskDetailActivity.class, bundle);
            }
        });

    }
    private void getTask(int departmentId, String employId, String startTime, String endTime, int taskStatus) {
        JsonObject params = new JsonObject();
        params.addProperty("departmentid", departmentId);
        params.addProperty("employname", employId);
        params.addProperty("startime", startTime);
        params.addProperty("endtime", endTime);
        params.addProperty("taskstatus", taskStatus);
        Net.create(Api.class).workTaskList(token, params)
                .enqueue(new NetCallback<List<WaitingHandleTaskModel>>(this, true) {
                    @Override
                    public void onResponse(List<WaitingHandleTaskModel> list) {
                        waitingTaskModelList.clear();
                        if (list != null && list.size() > 0) {
                            waitingTaskModelList.addAll(list);
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

    @OnClick({
            R.id.iv_back,

    })
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
            if (intent == null) {
                return;
            }
            if (intent.getAction().equals("com.action.update.task")) {
                getTask(departmentId, employId, startTime, endTime, taskStatus);
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
