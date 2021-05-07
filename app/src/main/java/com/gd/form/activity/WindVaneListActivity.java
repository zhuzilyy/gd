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
import com.gd.form.adapter.WindVaneListAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.ServerModel;
import com.gd.form.model.WindModel;
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

public class WindVaneListActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private WindVaneListAdapter adapter;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    private String token, userId;
    private  DeleteDialog deleteDialog;
    private List<WindModel> resultWindList;
    private final int ADD_WIND = 100;
    private int deleteIndex;
    private String pipeId,departmentId;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_wind_vane_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("风向标");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("添加");
        deleteDialog = new DeleteDialog(this);
        resultWindList = new ArrayList<>();
        token = (String) SPUtil.get(WindVaneListActivity.this, "token", "");
        userId = (String) SPUtil.get(WindVaneListActivity.this, "userId", "");
        if (getIntent() != null) {
            List<WindModel> windModelList = (List<WindModel>) getIntent().getExtras().getSerializable("winds");
            if (windModelList != null && windModelList.size() > 0) {
                resultWindList = windModelList;
                llNoData.setVisibility(View.GONE);
            } else {
                llNoData.setVisibility(View.VISIBLE);
            }
        }
        if(getIntent()!=null){
            pipeId =  getIntent().getExtras().getString("pipeId");
            departmentId =  getIntent().getExtras().getString("departmentId");
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
        adapter = new WindVaneListAdapter(mContext, resultWindList, R.layout.adapter_item_wind_vane_list);
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
                deleteWind();
                deleteDialog.dismiss();
            }
            @Override
            public void onNegativeClick() {
                deleteDialog.dismiss();
            }
        });
    }
    private void deleteWind() {
        JsonObject params = new JsonObject();
        params.addProperty("stakeid", resultWindList.get(deleteIndex).getStakeid());
        params.addProperty("windvanes", resultWindList.get(deleteIndex).getDistance());
        Net.create(Api.class).deleteWindVane(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        ToastUtil.show(result.getMsg());
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent();
                            intent.setAction("com.action.update");
                            sendBroadcast(intent);
                            resultWindList.remove(deleteIndex);
                            adapter.notifyDataSetChanged();
                            if (resultWindList != null && resultWindList.size() > 0) {
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
                Intent intent = new Intent(WindVaneListActivity.this,AddWindVaneActivity.class);
                intent.putExtra("name","windVane");
                intent.putExtra("departmentId",departmentId);
                intent.putExtra("pipeId",pipeId);
                startActivityForResult(intent,ADD_WIND);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null){
            return;
        }
        if(requestCode == ADD_WIND){
           String name =  data.getStringExtra("name");
           String distance =  data.getStringExtra("distance");
           String id =  data.getStringExtra("id");
            WindModel windModel = new WindModel();
            windModel.setStakename(name);
            windModel.setDistance(distance);
            windModel.setStakeid(Integer.parseInt(id));
            resultWindList.add(windModel);
            adapter.notifyDataSetChanged();
            if (resultWindList != null && resultWindList.size() > 0) {
                llNoData.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.VISIBLE);
            } else {
                llNoData.setVisibility(View.VISIBLE);
                refreshLayout.setVisibility(View.GONE);
            }
        }
    }
}
