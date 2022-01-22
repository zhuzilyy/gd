package com.gd.form.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gd.form.R;
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.adapter.ProjectRecordAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.ProgressModel;
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

public class ProjectRecordListActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    private ProjectRecordAdapter adapter;
    private String token,projectId;
    private List<ProgressModel> progressModelList;
    private MyReceiver myReceiver;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_project_record_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("进度记录");
        progressModelList = new ArrayList<>();
        projectId = getIntent().getExtras().getString("projectId");
        token = (String) SPUtil.get(this, "token", "");
        getData(projectId);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new ProjectRecordAdapter(this, progressModelList, R.layout.adapter_project_record);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                if(v.getId() == R.id.ll_upload){
                    Uri uri = Uri.parse(progressModelList.get(position).getUploadfile());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });

        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.action.add.success");
        registerReceiver(myReceiver,intentFilter);
    }

    private void getData(String projectId) {
        JsonObject params = new JsonObject();
        params.addProperty("projectid", projectId);
        Net.create(Api.class).getProjectProgressList(token, params)
                .enqueue(new NetCallback<List<ProgressModel>>(this, true) {
                    @Override
                    public void onResponse(List<ProgressModel> list) {
                        if (list != null && list.size() > 0) {
                            progressModelList.addAll(list);
                            adapter.notifyDataSetChanged();
                            llNoData.setVisibility(View.GONE);
                            refreshLayout.setVisibility(View.VISIBLE);
                        } else {
                            llNoData.setVisibility(View.VISIBLE);
                            refreshLayout.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @OnClick({R.id.iv_back, R.id.tv_right})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                Bundle bundle = new Bundle();
                bundle.putString("projectId",projectId);
                openActivity(AddProjectRecordActivity.class,bundle);
                break;
        }
    }
    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.action.add.success")){
                progressModelList.clear();
                getData(projectId);
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
