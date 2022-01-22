package com.gd.form.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gd.form.R;
import com.gd.form.adapter.KpiAdapter;
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.KpiModel;
import com.gd.form.model.ServerModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.view.DeleteDialog;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class KpiListActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_kpi)
    RecyclerView rvKpi;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    private KpiAdapter kpiAdapter;
    private List<KpiModel> kpiModelList;
    private String token, userId;
    private String seasonNo,departmentId;
    private DeleteDialog deleteDialog;
    private int deleteIndex;
    private MyReceiver myReceiver;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }
    @Override
    protected int getActLayoutId() {
        return R.layout.activity_kpi_detail;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("考核列表");
        if(getIntent()!=null){
            seasonNo = getIntent().getStringExtra("seasonNo");
            departmentId = getIntent().getStringExtra("departmentId");
        }
        token = (String) SPUtil.get(KpiListActivity.this, "token", "");
        userId = (String) SPUtil.get(KpiListActivity.this, "userId", "");
        deleteDialog = new DeleteDialog(this);
        kpiModelList = new ArrayList<>();
        initViews();
        getData();

        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.action.update.kpi.success");
        registerReceiver(myReceiver,intentFilter);

    }
    private void getData() {
        JsonObject params = new JsonObject();
        params.addProperty("seqNo", Integer.parseInt(seasonNo));
        params.addProperty("departmentid", departmentId);
        params.addProperty("appempid", userId);
        Net.create(Api.class).getKpiList(token, params)
                .enqueue(new NetCallback<List<KpiModel>>(this, true) {
                    @Override
                    public void onResponse(List<KpiModel> result) {
                        if (result != null && result.size() > 0) {
                            kpiModelList.addAll(result);
                            kpiAdapter.notifyDataSetChanged();
                            rvKpi.setVisibility(View.VISIBLE);
                            llNoData.setVisibility(View.GONE);
                        } else {
                            rvKpi.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
    private void initViews() {
        rvKpi.setLayoutManager(new LinearLayoutManager(mContext));
        kpiAdapter = new KpiAdapter(mContext, kpiModelList, R.layout.adapter_item_kpi);
        rvKpi.setAdapter(kpiAdapter);
        kpiAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                switch (v.getId()){
                    case R.id.btn_delete:
                        deleteIndex = position;
                        deleteDialog.show();
                        break;
                    case R.id.btn_check:
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("kpiModel",kpiModelList.get(position));
                        openActivity(KpiUpdateActivity.class,bundle);
                        break;
                }
            }
        });
        deleteDialog.setOnClickBottomListener(new DeleteDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                deleteDialog.dismiss();
                delete();
            }

            @Override
            public void onNegativeClick() {
                deleteDialog.dismiss();
            }
        });
    }
    private void delete() {
        JsonObject params = new JsonObject();
        params.addProperty("id", kpiModelList.get(deleteIndex).getId());
        Net.create(Api.class).deleteKpi(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            kpiModelList.remove(deleteIndex);
                            kpiAdapter.notifyDataSetChanged();
                            Intent intent = new Intent();
                            intent.setAction("com.action.delete.kpi.success");
                            sendBroadcast(intent);
                            if (kpiModelList != null && kpiModelList.size() > 0) {
                                llNoData.setVisibility(View.GONE);
                                rvKpi.setVisibility(View.VISIBLE);
                            } else {
                                llNoData.setVisibility(View.VISIBLE);
                                rvKpi.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }
    @OnClick({R.id.iv_back})
    public void click(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.action.update.kpi.success")){
                kpiModelList.clear();
                getData();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myReceiver!=null){
            unregisterReceiver(myReceiver);
        }
    }
}
