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
import com.gd.form.adapter.WaitingHandleTaskAdapter;
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

public class WaitingHandleTaskActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private WaitingHandleTaskAdapter adapter;
    private String token, userId;
    private List<WaitingHandleTaskModel> waitingTaskModelList;
    private MyReceiver myReceiver;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_waiting_handle_task;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("下发任务待处理");
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
        getTask();
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.action.update.task");
        registerReceiver(myReceiver,intentFilter);
    }

    private void initData() {
        waitingTaskModelList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WaitingHandleTaskAdapter(this, waitingTaskModelList, R.layout.adapter_item_waiting_handle_task);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("taskId",waitingTaskModelList.get(position).getId()+"");
                bundle.putString("tag","unFinish");
                openActivity(TaskDetailActivity.class,bundle);
            }
        });

    }

    private void getTask() {
        JsonObject params = new JsonObject();
        params.addProperty("employname", userId);
        params.addProperty("taskstatus", 1);
        Net.create(Api.class).getWaitingHandleTask(token, params)
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
    class  MyReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent==null){
                return;
            }
            if(intent.getAction().equals("com.action.update.task")){
                getTask();
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
