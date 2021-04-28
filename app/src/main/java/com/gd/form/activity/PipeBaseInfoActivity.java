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
import com.gd.form.adapter.PipeBaseInfoAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.utils.SPUtil;
import com.gd.form.view.DeleteDialog;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
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
    private  DeleteDialog deleteDialog;
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
        token = (String) SPUtil.get(PipeBaseInfoActivity.this, "token", "");
        userId = (String) SPUtil.get(PipeBaseInfoActivity.this, "userId", "");
        llNoData.setVisibility(View.GONE);
        deleteDialog = new DeleteDialog(PipeBaseInfoActivity.this);
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
        List<String> list = new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            list.add(i+"");
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new PipeBaseInfoAdapter(mContext, list, R.layout.adapter_item_pipe_base_info);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                deleteDialog.show();
            }
        });
        deleteDialog.setOnClickBottomListener(new DeleteDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                deleteDialog.dismiss();
            }
            @Override
            public void onNegativeClick() {
                deleteDialog.dismiss();
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
