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
import com.gd.form.adapter.WaterProtectionListAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.WaterModel;
import com.gd.form.utils.SPUtil;
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
    private WaterProtectionListAdapter adapter;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    private String token, userId;
    private List<WaterModel> resultWaterList;
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
                if("select".equals(resultWaterList.get(position).getType())){
                    Intent intent = new Intent();
                    intent.putExtra("name",resultWaterList.get(position).getStakename());
                    intent.putExtra("waterId",resultWaterList.get(position).getId()+"");
                    intent.putExtra("stakeId",resultWaterList.get(position).getStakeid()+"");
                    setResult(RESULT_OK,intent);
                    finish();
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
