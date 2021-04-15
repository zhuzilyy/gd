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
import com.gd.form.adapter.PrincipalAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.PipePrincial;
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

public class PipePrincipalActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.principalRecycler)
    RecyclerView principalRecycler;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    private PrincipalAdapter adapter;
    private String pipeOwners;
    private List<PipePrincial> pipeOwnerList;
    private String stationId, pipeId;
    private String token, userId;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_pipe_princpal;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("管道负责人");
        token = (String) SPUtil.get(PipePrincipalActivity.this, "token", "");
        userId = (String) SPUtil.get(PipePrincipalActivity.this, "userId", "");
        pipeOwnerList = new ArrayList<>();
        initViews();
        initData();
    }

    private void initData() {
        principalRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new PrincipalAdapter(mContext, pipeOwnerList, R.layout.adapter_item_principal);
        principalRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                deletePrincipal(position);
            }
        });
        if (getIntent() != null) {
            pipeOwners = getIntent().getExtras().getString("pipeOwners");
            stationId = getIntent().getExtras().getString("stationId");
            pipeId = getIntent().getExtras().getString("pipeId");
            if (!TextUtils.isEmpty(pipeOwners)) {
                String[] pipeOwnerArr = pipeOwners.split(";");
                for (int i = 0; i < pipeOwnerArr.length; i++) {
                    String[] strArr = pipeOwnerArr[i].split(":");
                    PipePrincial pipePrincial = new PipePrincial();
                    pipePrincial.setDepartmentName(strArr[0]);
                    pipePrincial.setUserName(strArr[1]);
                    pipeOwnerList.add(pipePrincial);
                }
                if (pipeOwnerList.size() > 0) {
                    llNoData.setVisibility(View.GONE);
                    refreshLayout.setVisibility(View.VISIBLE);
                } else {
                    llNoData.setVisibility(View.VISIBLE);
                    refreshLayout.setVisibility(View.GONE);
                }
            }
        }


    }

    private void deletePrincipal(int position) {
        JsonObject params = new JsonObject();
        params.addProperty("stakeid", Integer.valueOf(stationId));
        params.addProperty("pipeid", Integer.valueOf(pipeId));
        pipeOwnerList.remove(position);
        StringBuilder ownersSb = new StringBuilder();
        if (pipeOwnerList.size() == 0) {
            params.addProperty("pipeowners", "");
        } else {
            String combineInfo;
            for (int i = 0; i < pipeOwnerList.size(); i++) {
                PipePrincial pipePrincial = pipeOwnerList.get(i);
                if (i != pipeOwnerList.size() - 1) {
                    combineInfo = pipePrincial.getDepartmentName() + ":" + pipePrincial.getUserName() + ";";
                } else {
                    combineInfo = pipePrincial.getDepartmentName() + ":" + pipePrincial.getUserName();
                }
                ownersSb.append(combineInfo);
            }
            params.addProperty("pipeowners", ownersSb.toString());
        }
        Net.create(Api.class).addPrincipal(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        ToastUtil.show(result.getMsg());
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            adapter.notifyDataSetChanged();
                            Intent intent = new Intent();
                            intent.setAction("com.action.update");
                            sendBroadcast(intent);
                            if (pipeOwnerList.size() == 0) {
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
