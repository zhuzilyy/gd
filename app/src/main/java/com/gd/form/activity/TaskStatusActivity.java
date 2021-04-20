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
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.adapter.TaskAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.utils.SPUtil;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TaskStatusActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    private String token, userId;
    private TaskAdapter taskAdapter;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }
    @Override
    protected int getActLayoutId() {
        return R.layout.activity_task_status;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("待办任务");
        token = (String) SPUtil.get(TaskStatusActivity.this, "token", "");
        userId = (String) SPUtil.get(TaskStatusActivity.this, "userId", "");
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
        List<String> value = new ArrayList<>();
        for (int i = 0; i <5 ; i++) {
            value.add(i+"");
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        taskAdapter = new TaskAdapter(mContext, value, R.layout.adapter_item_task);
        recyclerView.setAdapter(taskAdapter);
        taskAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                openActivity(ApproveWaterProtectionActivity.class);
            }
        });
//        if (TextUtils.isEmpty(other)) {
//            llNoData.setVisibility(View.VISIBLE);
//            refreshLayout.setVisibility(View.GONE);
//        } else {
//            llNoData.setVisibility(View.GONE);
//            refreshLayout.setVisibility(View.VISIBLE);
//            String[] stationsArr = other.split(";");
//            for (int i = 0; i < stationsArr.length; i++) {
//                otherList.add(stationsArr[i]);
//                adapter.notifyDataSetChanged();
//            }
//        }
    }
    @OnClick({R.id.iv_back})
    public void click(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
