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
import com.gd.form.adapter.OtherListAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.SearchOtherModel;
import com.gd.form.model.ServerModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.view.DeleteDialog;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class OtherListActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private OtherListAdapter adapter;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    private String token, userId;
    private DeleteDialog deleteDialog;
    private List<SearchOtherModel> resultOtherList;
    private final int ADD_OTHER = 100;
    private int deleteIndex;
    private String pipeId,departmentId;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_other_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("其他");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("添加");
        deleteDialog = new DeleteDialog(this);
        resultOtherList = new ArrayList<>();
        token = (String) SPUtil.get(OtherListActivity.this, "token", "");
        userId = (String) SPUtil.get(OtherListActivity.this, "userId", "");
        if (getIntent() != null) {
            List<SearchOtherModel> otherModelList = (List<SearchOtherModel>) getIntent().getExtras().getSerializable("others");
            if (otherModelList != null && otherModelList.size() > 0) {
                resultOtherList = otherModelList;
                llNoData.setVisibility(View.GONE);
            } else {
                llNoData.setVisibility(View.VISIBLE);
            }
        }
        if(getIntent()!=null){
            pipeId =  getIntent().getExtras().getString("pipeId");
            departmentId =  getIntent().getExtras().getString("departmentId");
            if(TextUtils.isEmpty(pipeId)){
                tvRight.setVisibility(View.GONE);
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
        adapter = new OtherListAdapter(mContext, resultOtherList, R.layout.adapter_item_other_list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                switch (v.getId()){
                    case R.id.btn_delete:
                        deleteIndex = position;
                        deleteDialog.show();
                        break;
                    case R.id.btn_check:
                        Intent intent = new Intent(OtherListActivity.this, SomeOthersActivity.class);
                        intent.putExtra("id",resultOtherList.get(position).getId());
                        intent.putExtra("name","other");
                        startActivity(intent);
                        break;
                }

            }
        });
        deleteDialog.setOnClickBottomListener(new DeleteDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                deleteDialog.dismiss();
                deleteOther();
            }
            @Override
            public void onNegativeClick() {
                deleteDialog.dismiss();
            }
        });
    }
    private void deleteOther() {
        JsonObject params = new JsonObject();
        params.addProperty("stakeid", resultOtherList.get(deleteIndex).getStakeid());
        params.addProperty("id", resultOtherList.get(deleteIndex).getId());
        Net.create(Api.class).deleteSomeOthers(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent();
                            intent.setAction("com.action.update");
                            sendBroadcast(intent);
                            resultOtherList.remove(deleteIndex);
                            adapter.notifyDataSetChanged();
                            if (resultOtherList != null && resultOtherList.size() > 0) {
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
                Intent intent = new Intent(OtherListActivity.this, AddWindVaneActivity.class);
                intent.putExtra("name", "other");
                intent.putExtra("departmentId",departmentId);
                intent.putExtra("pipeId",pipeId);
                startActivityForResult(intent, ADD_OTHER);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == ADD_OTHER) {
            String name = data.getStringExtra("name");
            String otherName = data.getStringExtra("otherName");
            String distance = data.getStringExtra("distance");
            String id = data.getStringExtra("id");
            String otherId = data.getStringExtra("otherId");
            SearchOtherModel searchOtherModel = new SearchOtherModel();
            searchOtherModel.setStakename(name);
            searchOtherModel.setDistance(distance);
            searchOtherModel.setName(otherName);
            searchOtherModel.setId(otherId);
            searchOtherModel.setStakeid(Integer.parseInt(id));
            resultOtherList.add(searchOtherModel);
            adapter.notifyDataSetChanged();
            if (resultOtherList != null && resultOtherList.size() > 0) {
                llNoData.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.VISIBLE);
            } else {
                llNoData.setVisibility(View.VISIBLE);
                refreshLayout.setVisibility(View.GONE);
            }
        }
    }
}
