package com.gd.form.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
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
import com.gd.form.adapter.TunnelAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.TunnelModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.ToastUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchTunnelActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.mainSearchEditText)
    EditText mainSearchEditText;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private List<TunnelModel> tunnelModelList;
    private TunnelAdapter adapter;
    private String token, userId;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_search_building;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tunnelModelList = new ArrayList<>();
        tvTitle.setText("搜索隧道");
        token = (String) SPUtil.get(SearchTunnelActivity.this, "token", "");
        userId = (String) SPUtil.get(SearchTunnelActivity.this, "userId", "");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new TunnelAdapter(mContext, tunnelModelList, R.layout.adapter_item_tunnel);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                Intent intent = new Intent();
                intent.putExtra("tunnel", tunnelModelList.get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        mainSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (TextUtils.isEmpty(v.getText().toString())) {
                    ToastUtil.show("请输入搜索内容");
                }
                searchTunnel(v.getText().toString().trim());
                return false;
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
                if (TextUtils.isEmpty(mainSearchEditText.getText().toString())) {
                    ToastUtil.show("请输入搜索关键字");
                    return;
                }
                searchTunnel(mainSearchEditText.getText().toString());
                break;
        }
    }

    private void searchTunnel(String keyWord) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", keyWord);
        Net.create(Api.class).searchTunnel(token, jsonObject)
                .enqueue(new NetCallback<List<TunnelModel>>(this, true) {
                    @Override
                    public void onResponse(List<TunnelModel> list) {
                        if (list != null && list.size() > 0) {
                            refreshLayout.setVisibility(View.VISIBLE);
                            llNoData.setVisibility(View.GONE);
                            tunnelModelList.addAll(list);
                            adapter.notifyDataSetChanged();

                        } else {
                            refreshLayout.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}
