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
import com.gd.form.adapter.BuildingListAdapter;
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.SearchBuildingModel;
import com.gd.form.utils.SPUtil;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class BuildingListActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private BuildingListAdapter adapter;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    private String token, userId;
    private List<SearchBuildingModel> resultBuildingList;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_building_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("违章违建");
        token = (String) SPUtil.get(BuildingListActivity.this, "token", "");
        userId = (String) SPUtil.get(BuildingListActivity.this, "userId", "");
        if(getIntent()!=null){
            List<SearchBuildingModel> buildingModelList = (List<SearchBuildingModel>)getIntent().getExtras().getSerializable("buildings");
            if(buildingModelList!=null && buildingModelList.size()>0){
                resultBuildingList = buildingModelList;
                llNoData.setVisibility(View.GONE);
            }else{
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
        adapter = new BuildingListAdapter(mContext, resultBuildingList, R.layout.adapter_item_building_list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                SearchBuildingModel searchBuildingModel = resultBuildingList.get(position);
                Intent intent = new Intent();
                intent.putExtra("name",searchBuildingModel.getLlegalname());
                intent.putExtra("buildId",searchBuildingModel.getId()+"");
                intent.putExtra("stakeId",searchBuildingModel.getStakeid()+"");
                setResult(RESULT_OK,intent);
                finish();
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
