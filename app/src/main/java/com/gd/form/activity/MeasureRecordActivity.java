package com.gd.form.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gd.form.R;
import com.gd.form.adapter.MeasureRecordAdapter;
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.MeasureModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MeasureRecordActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recordRecycler)
    RecyclerView recordRecycler;
    @BindView(R.id.stationRefreshLayout)
    SmartRefreshLayout stationRefreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    private MeasureRecordAdapter adapter;
    private String token, userId, stationId;
    private List<MeasureModel> measureModelList;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_measure_record;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("测量记录");
        measureModelList = new ArrayList<>();
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        if (getIntent() != null) {
            stationId = getIntent().getExtras().getString("stationId");
        }
        initViews();
        initData();

    }

    private void initData() {
        recordRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MeasureRecordAdapter(mContext, measureModelList, R.layout.adapter_item_measure_record);
        recordRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("stationId", stationId);
                bundle.putString("time", measureModelList.get(position).getMeasuredate().getTime() + "");
                openActivity(PipeMeasureDetailActivity.class, bundle);
            }
        });
        //获取测量数据
        JsonObject params = new JsonObject();
        params.addProperty("stakeid", Integer.parseInt(stationId));
        Net.create(Api.class).getMeasureRecords(token, params)
                .enqueue(new NetCallback<List<MeasureModel>>(this, true) {
                    @Override
                    public void onResponse(List<MeasureModel> list) {
                        if (list != null && list.size() > 0) {
                            llNoData.setVisibility(View.GONE);
                            stationRefreshLayout.setVisibility(View.VISIBLE);
                            measureModelList.addAll(list);
                            adapter.notifyDataSetChanged();
                        } else {
                            llNoData.setVisibility(View.VISIBLE);
                            stationRefreshLayout.setVisibility(View.GONE);
                        }

                    }
                });
    }

    private void initViews() {
        stationRefreshLayout.setOnRefreshListener(refreshLayout -> {

            refreshLayout.finishRefresh(2000);
        });
        stationRefreshLayout.setOnLoadMoreListener(refreshLayout -> {

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
        }
    }
}
