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
import com.gd.form.adapter.StationAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.StationNoModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.ToastUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class StationActivity extends BaseActivity {
    @BindView(R.id.stationRecycler)
    RecyclerView stationRecycler;
    @BindView(R.id.mainSearchEditText)
    EditText mainSearchEditText;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.stationRefreshLayout)
    SmartRefreshLayout stationRefreshLayout;
    private List<StationNoModel> stationNoModelList;
    private StationAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initData();
    }

    private void initData() {
        stationNoModelList = new ArrayList<>();
        stationRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new StationAdapter(mContext, stationNoModelList, R.layout.adapter_item_station);
        stationRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                Intent intent = new Intent();
                intent.putExtra("stationName", stationNoModelList.get(position).getName());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void getData(String keyWord) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pipeid", 0);
        jsonObject.addProperty("name", keyWord);
        Net.create(Api.class).pipestakeinfoget(jsonObject)
                .enqueue(new NetCallback<List<StationNoModel>>(this, true) {
                    @Override
                    public void onResponse(List<StationNoModel> list) {
                        if (list != null && list.size() > 0) {
                            stationRefreshLayout.setVisibility(View.VISIBLE);
                            llNoData.setVisibility(View.GONE);
                            stationNoModelList.addAll(list);
                            adapter.notifyDataSetChanged();
                        } else {
                            stationRefreshLayout.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
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
        mainSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (TextUtils.isEmpty(v.getText().toString())) {
                    ToastUtil.show("请输入搜索内容");
                }
                getData(v.getText().toString().trim());
                return false;
            }
        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_station;
    }
}
