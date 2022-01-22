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
import com.gd.form.adapter.FileAdapter;
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.StandardFileModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class StandardFileActivity extends BaseActivity {
    @BindView(R.id.rv_file)
    RecyclerView rvFile;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    private String token, userId;
    private List<StandardFileModel> standardFileModels;
    private FileAdapter fileAdapter;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_standard_file;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("标准化文件");
        standardFileModels = new ArrayList<>();
        token = (String) SPUtil.get(StandardFileActivity.this, "token", "");
        userId = (String) SPUtil.get(StandardFileActivity.this, "userId", "");
        getData();
        initViews();
    }

    private void getData() {
        JsonObject params = new JsonObject();
        params.addProperty("appemp", userId);
        Net.create(Api.class).getStandardFiles(token, params)
                .enqueue(new NetCallback<List<StandardFileModel>>(this, true) {
                    @Override
                    public void onResponse(List<StandardFileModel> result) {
                        if (result != null && result.size() > 0) {
                            standardFileModels.addAll(result);
                            fileAdapter.notifyDataSetChanged();
                            rvFile.setVisibility(View.VISIBLE);
                            llNoData.setVisibility(View.GONE);
                        } else {
                            rvFile.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void initViews() {
        rvFile.setLayoutManager(new LinearLayoutManager(mContext));
        fileAdapter = new FileAdapter(mContext, standardFileModels, R.layout.adapter_item_standrad_file);
        rvFile.setAdapter(fileAdapter);
        fileAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("name", standardFileModels.get(position).getName());
                openActivity(StandardFileListActivity.class, bundle);
            }
        });
    }

    @OnClick({R.id.iv_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
