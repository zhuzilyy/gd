package com.gd.form.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gd.form.R;
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.adapter.WindVaneAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.ServerModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.ToastUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PipeWindVaneActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.windVaneRecycler)
    RecyclerView windVaneRecycler;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private WindVaneAdapter adapter;
    private List<String> windVaneList;
    private String stationId, pipeId,windVane;
    private String token, userId;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_pipe_wind_vane;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("风向标");
        initViews();
        token = (String) SPUtil.get(PipeWindVaneActivity.this, "token", "");
        userId = (String) SPUtil.get(PipeWindVaneActivity.this, "userId", "");
        if (getIntent() != null) {
            stationId = getIntent().getExtras().getString("stationId");
            pipeId = getIntent().getExtras().getString("pipeId");
            windVane = getIntent().getExtras().getString("windVanes");
        }
        initData();
    }
    private void initData() {
        windVaneList = new ArrayList<>();
        windVaneRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new WindVaneAdapter(mContext, windVaneList, R.layout.adapter_item_wind_vane);
        windVaneRecycler.setAdapter(adapter);
        if (TextUtils.isEmpty(windVane)) {
            llNoData.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
        } else {
            llNoData.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
            String[] windVaneArr = windVane.split(";");
            for (int i = 0; i < windVaneArr.length; i++) {
                windVaneList.add(windVaneArr[i]);
                adapter.notifyDataSetChanged();
            }
        }
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                deleteWindVanes(position);
            }
        });
    }

    private void deleteWindVanes(int position) {
        JsonObject params = new JsonObject();
        params.addProperty("stakeid", Integer.valueOf(stationId));
        params.addProperty("pipeid", Integer.valueOf(pipeId));
        windVaneList.remove(position);
        StringBuilder windVaneSb = new StringBuilder();
        if (windVaneList.size() == 0) {
            params.addProperty("windvanes", "");
        } else {
            String combineInfo;
            for (int i = 0; i < windVaneList.size(); i++) {
                String windVanes = windVaneList.get(i);
                if (i != windVaneList.size() - 1) {
                    combineInfo = windVanes + ";";
                } else {
                    combineInfo = windVanes;
                }
                windVaneSb.append(combineInfo);
            }
            params.addProperty("windvanes", windVaneSb.toString());
        }
        Net.create(Api.class).addWindVane(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        ToastUtil.show(result.getMsg());
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            adapter.notifyDataSetChanged();
                            Intent intent = new Intent();
                            intent.setAction("com.action.update");
                            sendBroadcast(intent);
                            if (windVaneList.size() == 0) {
                                llNoData.setVisibility(View.VISIBLE);
                                refreshLayout.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }

    private void initViews() {
        refreshLayout.setOnRefreshListener(refreshLayout -> {

            refreshLayout.finishRefresh(2000);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {

            refreshLayout.finishLoadMore(2000);
        });
    }

    @OnClick({
            R.id.iv_back,
            R.id.tv_right,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                openActivity(AddWindVaneActivity.class);
                break;
        }
    }
}
