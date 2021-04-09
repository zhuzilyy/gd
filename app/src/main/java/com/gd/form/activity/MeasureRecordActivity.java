package com.gd.form.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gd.form.R;
import com.gd.form.adapter.MeasuerRecordAdapter;
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.base.BaseActivity;
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
    private MeasuerRecordAdapter adapter;

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
        initViews();
        initData();

    }

    private void initData() {
        List<String> values = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            values.add("测量记录" + i);
        }
        recordRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new MeasuerRecordAdapter(mContext, values, R.layout.adapter_item_measure_record);
        recordRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                openActivity(PipeMeasureDetailActivity.class);
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
