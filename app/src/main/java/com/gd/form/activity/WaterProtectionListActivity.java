package com.gd.form.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gd.form.R;
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.adapter.WaterProtectionListAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.WaterModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.ToastUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class WaterProtectionListActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.mainSearchEditText)
    EditText mainSearchEditText;
    private WaterProtectionListAdapter adapter;
    private String token, userId;
    private List<WaterModel> resultWaterList;
    private String activityName;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_water_protection_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("水工保护");
        token = (String) SPUtil.get(WaterProtectionListActivity.this, "token", "");
        userId = (String) SPUtil.get(WaterProtectionListActivity.this, "userId", "");
        if (getIntent() != null) {
            List<WaterModel> waterModelList = (List<WaterModel>) getIntent().getExtras().getSerializable("waters");
            activityName = getIntent().getExtras().getString("activityName");
            if (waterModelList != null && waterModelList.size() > 0) {
                resultWaterList = waterModelList;
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
        adapter = new WaterProtectionListAdapter(mContext, resultWaterList, R.layout.adapter_item_water_protection_list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                if ("select".equals(resultWaterList.get(position).getType())) {
                    Intent intent = new Intent();
                    intent.putExtra("name", resultWaterList.get(position).getStakename());
                    intent.putExtra("waterId", resultWaterList.get(position).getId() + "");
                    intent.putExtra("stakeId", resultWaterList.get(position).getStakeid() + "");
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @OnClick({
            R.id.iv_back,
            R.id.btn_search,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_search:
                if (TextUtils.isEmpty(mainSearchEditText.getText().toString().trim())) {
                    ToastUtil.show("请输入搜索内容");
                    return;
                }
                getData(mainSearchEditText.getText().toString().trim());
                break;
        }
    }
    private void getData(String keyWord) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("empid", userId);
        jsonObject.addProperty("key", keyWord);
        Net.create(Api.class).getWaterStation(token,jsonObject)
                .enqueue(new NetCallback<List<WaterModel>>(this, true) {
                    @Override
                    public void onResponse(List<WaterModel> list) {
                        resultWaterList.clear();
                        for (int i = 0; i < list.size(); i++) {
                            WaterModel waterModel = list.get(i);
                            if("approveWater".equals(activityName)){
                                waterModel.setType("select");
                            }else if("selectWater".equals(activityName)){
                                waterModel.setType("select");
                            }
                            resultWaterList.add(waterModel);
                        }
                        if (list != null && list.size() > 0) {
                            adapter.notifyDataSetChanged();
                            llNoData.setVisibility(View.GONE);
                        } else {
                            refreshLayout.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}
