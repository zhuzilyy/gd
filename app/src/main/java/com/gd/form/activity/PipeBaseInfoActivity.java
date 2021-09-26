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
import com.gd.form.adapter.PipeBaseInfoAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.ServerModel;
import com.gd.form.model.StakeModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.view.DeleteDialog;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PipeBaseInfoActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private PipeBaseInfoAdapter adapter;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    private String token, userId;
    private DeleteDialog deleteDialog;
    private List<StakeModel> resultModelList;
    private int deletePosition = -1;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_pipe_base_info;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("管道标识信息");
        deleteDialog = new DeleteDialog(this);
        token = (String) SPUtil.get(PipeBaseInfoActivity.this, "token", "");
        userId = (String) SPUtil.get(PipeBaseInfoActivity.this, "userId", "");
        if (getIntent() != null) {
            List<StakeModel> stakeModelList = (List<StakeModel>) getIntent().getExtras().getSerializable("stakes");
            if (stakeModelList != null && stakeModelList.size() > 0) {
                resultModelList = stakeModelList;
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
        adapter = new PipeBaseInfoAdapter(mContext, resultModelList, R.layout.adapter_item_pipe_base_info);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                StakeModel stakeModel = resultModelList.get(position);
                switch (v.getId()) {
                    case R.id.btn_delete:
                        deletePosition = position;
                        deleteDialog.show();
                        break;
                    default:
                        Intent intent = new Intent();
                        intent.putExtra("name", stakeModel.getName());
                        intent.putExtra("id", stakeModel.getId() + "");
                        intent.putExtra("lineId", stakeModel.getLineid());
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                }


            }
        });
        deleteDialog.setOnClickBottomListener(new DeleteDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                deleteDialog.dismiss();
                delete(resultModelList.get(deletePosition).getId(),deletePosition);
            }
            @Override
            public void onNegativeClick() {
                deleteDialog.dismiss();
            }
        });
    }
    private void delete(int id,int position) {
        JsonObject params = new JsonObject();
        params.addProperty("id", id);
        Net.create(Api.class).deletePipe(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel serverModel) {
                        if (serverModel.getCode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent();
                            intent.setAction("com.action.update");
                            sendBroadcast(intent);
                            resultModelList.remove(position);
                            adapter.notifyDataSetChanged();
                            if (resultModelList != null && resultModelList.size() > 0) {
                                llNoData.setVisibility(View.GONE);
                            } else {
                                llNoData.setVisibility(View.VISIBLE);
                            }
                        }
                    }
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
