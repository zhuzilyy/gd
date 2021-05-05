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
import com.gd.form.adapter.VideoListAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.SearchVideoModel;
import com.gd.form.model.ServerModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.ToastUtil;
import com.gd.form.view.DeleteDialog;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class VideoMonitorListActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private VideoListAdapter adapter;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    private String token, userId;
    private DeleteDialog deleteDialog;
    private List<SearchVideoModel> resultVideoList;
    private final int ADD_VIDEO = 100;
    private int deleteIndex;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_video_monitor_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("视频监控");
        tvRight.setText("添加");
        tvRight.setVisibility(View.VISIBLE);
        deleteDialog = new DeleteDialog(this);
        resultVideoList = new ArrayList<>();
        token = (String) SPUtil.get(VideoMonitorListActivity.this, "token", "");
        userId = (String) SPUtil.get(VideoMonitorListActivity.this, "userId", "");
        if (getIntent() != null) {
            List<SearchVideoModel> videoModelList = (List<SearchVideoModel>) getIntent().getExtras().getSerializable("videos");
            if (videoModelList != null && videoModelList.size() > 0) {
                resultVideoList = videoModelList;
                llNoData.setVisibility(View.GONE);
            } else {
                llNoData.setVisibility(View.VISIBLE);
            }
        }
        initViews();
        initData();
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
        adapter = new VideoListAdapter(mContext, resultVideoList, R.layout.adapter_item_wind_vane_list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                deleteIndex = position;
                deleteDialog.show();
            }
        });
        deleteDialog.setOnClickBottomListener(new DeleteDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                deleteDialog.dismiss();
                deleteVideo();
            }

            @Override
            public void onNegativeClick() {
                deleteDialog.dismiss();
            }
        });
    }

    private void deleteVideo() {
        JsonObject params = new JsonObject();
        params.addProperty("stakeid", resultVideoList.get(deleteIndex).getStakeid());
        params.addProperty("viewmonitor", resultVideoList.get(deleteIndex).getDistance());
        Net.create(Api.class).deleteVideo(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        ToastUtil.show(result.getMsg());
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent();
                            intent.setAction("com.action.update");
                            sendBroadcast(intent);
                            resultVideoList.remove(deleteIndex);
                            adapter.notifyDataSetChanged();
                            if (resultVideoList != null && resultVideoList.size() > 0) {
                                llNoData.setVisibility(View.GONE);
                                refreshLayout.setVisibility(View.VISIBLE);
                            } else {
                                llNoData.setVisibility(View.VISIBLE);
                                refreshLayout.setVisibility(View.GONE);
                            }
                        }
                    }
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
                Intent intent = new Intent(VideoMonitorListActivity.this, AddWindVaneActivity.class);
                intent.putExtra("name", "videoMonitoring");
                startActivityForResult(intent, ADD_VIDEO);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == ADD_VIDEO) {
            String name = data.getStringExtra("name");
            String distance = data.getStringExtra("distance");
            String id = data.getStringExtra("id");
            SearchVideoModel searchVideoModel = new SearchVideoModel();
            searchVideoModel.setStakename(name);
            searchVideoModel.setDistance(distance);
            searchVideoModel.setStakeid(Integer.parseInt(id));
            resultVideoList.add(searchVideoModel);
            adapter.notifyDataSetChanged();
            if (resultVideoList != null && resultVideoList.size() > 0) {
                llNoData.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.VISIBLE);
            } else {
                llNoData.setVisibility(View.VISIBLE);
                refreshLayout.setVisibility(View.GONE);
            }
        }
    }
}
