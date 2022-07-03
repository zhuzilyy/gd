package com.gd.form.activity;

import android.content.Intent;
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
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.adapter.UploadEventAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.ServerModel;
import com.gd.form.model.UploadEventModel;
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

public class UploadEventListActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    private String token, userId;
    private List<UploadEventModel> uploadEventModelList;
    private UploadEventAdapter adapter;
    private String employId = "ALL";
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_upload_event_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("上报事件清单");
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        uploadEventModelList = new ArrayList<>();
        initViews();
        initData();
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            String departmentId = bundle.getString("departmentId");
//            employId = bundle.getString("employId");
            String startTime = bundle.getString("startTime");
            String endTime = bundle.getString("endTime");
            String eventStatus = bundle.getString("eventStatus");
            getEventList(departmentId, employId, startTime, endTime, eventStatus);
        }

    }

    private void initViews() {
        refreshLayout.setOnRefreshListener(refreshLayout -> {

            refreshLayout.finishRefresh(2000);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {

            refreshLayout.finishLoadMore(2000);
        });
    }

    private void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new UploadEventAdapter(mContext, uploadEventModelList, R.layout.adapter_item_upload_event);
        recyclerView.setAdapter(adapter);
        adapter.setOnFinishListener(new UploadEventAdapter.OnFinishListener() {
            @Override
            public void onFinishClickListener(int position) {
                finishEvent(position);
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                if (uploadEventModelList.get(position).getStatus() == 1) {
                    Intent intent = new Intent(UploadEventListActivity.this, UploadEventActivity.class);
                    intent.putExtra("formId", uploadEventModelList.get(position).getFormid());
                    intent.putExtra("tag", "detail");
                    startActivity(intent);

                }
            }
        });
    }

    private void finishEvent(int position) {
        JsonObject params = new JsonObject();
        params.addProperty("eventid", uploadEventModelList.get(position).getFormid());
        params.addProperty("eventstatus", 1);
        Log.i("tag", "params==" + params);
        Net.create(Api.class).finishEvent(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            uploadEventModelList.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void getEventList(String departmentId, String employId, String startTime, String endTime, String eventStatus) {
        JsonObject params = new JsonObject();
        params.addProperty("departmentid", Integer.parseInt(departmentId));
        params.addProperty("employname", employId);
        params.addProperty("startime", startTime);
        params.addProperty("endtime", endTime);
        params.addProperty("eventstatus", Integer.parseInt(eventStatus));
        Log.i("tag","params====="+params);
        Net.create(Api.class).getEventList(token, params)
                .enqueue(new NetCallback<List<UploadEventModel>>(this, true) {
                    @Override
                    public void onResponse(List<UploadEventModel> list) {
                        uploadEventModelList.clear();
                        if (list != null && list.size() > 0) {
                            uploadEventModelList.addAll(list);
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
}
