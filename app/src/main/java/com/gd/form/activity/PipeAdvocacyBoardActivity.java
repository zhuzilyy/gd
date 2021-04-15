package com.gd.form.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.gd.form.adapter.VideoAdapter;
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

public class PipeAdvocacyBoardActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    private VideoAdapter adapter;
    private List<String> advocacyBoardList;
    private String stationId, pipeId,advocacyBoard;
    private String token, userId;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_pipe_advocacy_board;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("宣传栏");
        advocacyBoardList = new ArrayList<>();
        token = (String) SPUtil.get(PipeAdvocacyBoardActivity.this, "token", "");
        userId = (String) SPUtil.get(PipeAdvocacyBoardActivity.this, "userId", "");
        if (getIntent() != null) {
            stationId = getIntent().getExtras().getString("stationId");
            pipeId = getIntent().getExtras().getString("pipeId");
            advocacyBoard = getIntent().getExtras().getString("advocacyBoard");
            Log.i("tag","advocacyBoard==="+advocacyBoard);
        }
        initViews();
        initData();
    }

    private void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new VideoAdapter(mContext, advocacyBoardList, R.layout.adapter_item_video);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                deleteAdvocacyBoard(position);
            }
        });
        if (TextUtils.isEmpty(advocacyBoard)) {
            llNoData.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
        } else {
            llNoData.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
            String[] advocacyBoardArr = advocacyBoard.split(";");
            for (int i = 0; i < advocacyBoardArr.length; i++) {
                advocacyBoardList.add(advocacyBoardArr[i]);
                adapter.notifyDataSetChanged();
            }
        }
    }
    private void deleteAdvocacyBoard(int position) {
        JsonObject params = new JsonObject();
        params.addProperty("stakeid", Integer.valueOf(stationId));
        params.addProperty("pipeid", Integer.valueOf(pipeId));
        advocacyBoardList.remove(position);
        StringBuilder advocacyBoardSb = new StringBuilder();
        if (advocacyBoardList.size() == 0) {
            params.addProperty("pedurail", "");
        } else {
            String combineInfo;
            for (int i = 0; i < advocacyBoardList.size(); i++) {
                String windVanes = advocacyBoardList.get(i);
                if (i != advocacyBoardList.size() - 1) {
                    combineInfo = windVanes + ";";
                } else {
                    combineInfo = windVanes;
                }
                advocacyBoardSb.append(combineInfo);
            }
            params.addProperty("pedurail", advocacyBoardSb.toString());
        }
        Net.create(Api.class).addAdvocacyBoard(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        ToastUtil.show(result.getMsg());
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            adapter.notifyDataSetChanged();
                            Intent intent = new Intent();
                            intent.setAction("com.action.update");
                            sendBroadcast(intent);
                            if (advocacyBoardList.size() == 0) {
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
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
