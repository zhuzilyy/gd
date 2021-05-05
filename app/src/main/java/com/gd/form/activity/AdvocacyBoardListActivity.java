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
import com.gd.form.adapter.AdvocacyBoardListAdapter;
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.PreModel;
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

public class AdvocacyBoardListActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private AdvocacyBoardListAdapter adapter;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    private String token, userId;
    private DeleteDialog deleteDialog;
    private List<PreModel> resultPreList;
    private final int ADD_BOARD = 100;
    private int deleteIndex;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_advocacy_board_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("宣教栏");
        tvRight.setText("添加");
        tvRight.setVisibility(View.VISIBLE);
        deleteDialog = new DeleteDialog(this);
        resultPreList = new ArrayList<>();
        token = (String) SPUtil.get(AdvocacyBoardListActivity.this, "token", "");
        userId = (String) SPUtil.get(AdvocacyBoardListActivity.this, "userId", "");
        if (getIntent() != null) {
            List<PreModel> preModelList = (List<PreModel>) getIntent().getExtras().getSerializable("advocacyBoards");
            if (preModelList != null && preModelList.size() > 0) {
                resultPreList = preModelList;
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
        adapter = new AdvocacyBoardListAdapter(mContext, resultPreList, R.layout.adapter_item_advocacy_board_list);
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
                deleteBoard();
            }
            @Override
            public void onNegativeClick() {
                deleteDialog.dismiss();
            }
        });
    }
    private void deleteBoard() {
        JsonObject params = new JsonObject();
        params.addProperty("stakeid", resultPreList.get(deleteIndex).getStakeid());
        params.addProperty("pedurail", resultPreList.get(deleteIndex).getDistance());
        Net.create(Api.class).deleteBoard(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        ToastUtil.show(result.getMsg());
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent();
                            intent.setAction("com.action.update");
                            sendBroadcast(intent);
                            resultPreList.remove(deleteIndex);
                            adapter.notifyDataSetChanged();
                            if (resultPreList != null && resultPreList.size() > 0) {
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
                Intent intent = new Intent(AdvocacyBoardListActivity.this,AddWindVaneActivity.class);
                intent.putExtra("name","advocacyBoard");
                startActivityForResult(intent,ADD_BOARD);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null){
            return;
        }
        if(requestCode == ADD_BOARD){
            String name =  data.getStringExtra("name");
            String distance =  data.getStringExtra("distance");
            String id =  data.getStringExtra("id");
            PreModel preModel = new PreModel();
            preModel.setStakename(name);
            preModel.setDistance(distance);
            preModel.setStakeid(Integer.parseInt(id));
            resultPreList.add(preModel);
            adapter.notifyDataSetChanged();
            if (resultPreList != null && resultPreList.size() > 0) {
                llNoData.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.VISIBLE);
            } else {
                llNoData.setVisibility(View.VISIBLE);
                refreshLayout.setVisibility(View.GONE);
            }
        }
    }
}
