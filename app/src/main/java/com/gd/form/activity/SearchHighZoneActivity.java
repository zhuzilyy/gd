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
import com.gd.form.adapter.HighZoneAdapter;
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.HighZoneModel;
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

public class SearchHighZoneActivity extends BaseActivity {
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
    private List<HighZoneModel> highZoneModelList;
    private HighZoneAdapter adapter;
    private String token, userId;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_search_high_zone;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        highZoneModelList = new ArrayList<>();
        tvTitle.setText("搜索高后果区");
        token = (String) SPUtil.get(SearchHighZoneActivity.this, "token", "");
        userId = (String) SPUtil.get(SearchHighZoneActivity.this, "userId", "");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new HighZoneAdapter(mContext, highZoneModelList, R.layout.adapter_item_high_zone);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                Intent intent = new Intent();
                intent.putExtra("highZone", highZoneModelList.get(position));
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
                searchHighZone(v.getText().toString().trim());
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
                searchHighZone(mainSearchEditText.getText().toString());
                break;
        }
    }

    private void searchHighZone(String keyWord) {
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("name", keyWord);
//        Net.create(Api.class).searchHighZone(token, jsonObject)
//                .enqueue(new NetCallback<List<HighZoneModel>>(this, true) {
//                    @Override
//                    public void onResponse(List<HighZoneModel> list) {
//                        highZoneModelList.clear();
//                        if (list != null && list.size() > 0) {
//                            refreshLayout.setVisibility(View.VISIBLE);
//                            llNoData.setVisibility(View.GONE);
//                            highZoneModelList.addAll(list);
//                            adapter.notifyDataSetChanged();
//
//                        } else {
//                            refreshLayout.setVisibility(View.GONE);
//                            llNoData.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });


        JsonObject params = new JsonObject();
        params.addProperty("name", keyWord);
        Net.create(Api.class).searchHighZone(token, params)
                .enqueue(new NetCallback<List<HighZoneModel>>(this, true) {
                    @Override
                    public void onResponse(List<HighZoneModel> list) {
                        if (list != null && list.size() > 0) {
                            refreshLayout.setVisibility(View.VISIBLE);
                            llNoData.setVisibility(View.GONE);
                            highZoneModelList.addAll(list);
                            adapter.notifyDataSetChanged();
                        } else {
                            refreshLayout.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });


    }


}
