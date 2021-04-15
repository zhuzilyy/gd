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
import com.gd.form.adapter.StandingAdapter;
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

public class PipeStandingActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.standingRecycler)
    RecyclerView standingRecycler;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    private StandingAdapter adapter;
    private String stationId,pipeId,stations;
    private String token, userId;
    private List<String> stationList;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_pipe_standing;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("站场或阀室");
        stationList = new ArrayList<>();
        token = (String) SPUtil.get(PipeStandingActivity.this, "token", "");
        userId = (String) SPUtil.get(PipeStandingActivity.this, "userId", "");
        if (getIntent() != null) {
            stationId = getIntent().getExtras().getString("stationId");
            pipeId = getIntent().getExtras().getString("pipeId");
            stations = getIntent().getExtras().getString("stations");
        }
        initViews();
        initData();
    }

    private void initData() {
        standingRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new StandingAdapter(mContext, stationList, R.layout.adapter_item_standing);
        standingRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                deleteStation(position);
            }
        });
        if (TextUtils.isEmpty(stations)) {
            llNoData.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
        } else {
            llNoData.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
            String[] stationsArr = stations.split(";");
            for (int i = 0; i < stationsArr.length; i++) {
                stationList.add(stationsArr[i]);
                adapter.notifyDataSetChanged();
            }
        }

    }
    private void deleteStation(int position) {
        JsonObject params = new JsonObject();
        params.addProperty("stakeid", Integer.valueOf(stationId));
        params.addProperty("pipeid", Integer.valueOf(pipeId));
        stationList.remove(position);
        StringBuilder stationSb = new StringBuilder();
        if (stationList.size() == 0) {
            params.addProperty("stations", "");
        } else {
            String combineInfo;
            for (int i = 0; i < stationList.size(); i++) {
                String windVanes = stationList.get(i);
                if (i != stationList.size() - 1) {
                    combineInfo = windVanes + ";";
                } else {
                    combineInfo = windVanes;
                }
                stationSb.append(combineInfo);
            }
            params.addProperty("stations", stationSb.toString());
        }
        Net.create(Api.class).addStation(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        ToastUtil.show(result.getMsg());
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            adapter.notifyDataSetChanged();
                            Intent intent = new Intent();
                            intent.setAction("com.action.update");
                            sendBroadcast(intent);
                            if (stationList.size() == 0) {
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
                openActivity(AddStandingActivity.class);
                break;
        }
    }
}
