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
import com.gd.form.adapter.EventHistoryAdapter;
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.EventHistoryModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.TimeUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EventHistoryActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    private String token, userId;
    private List<EventHistoryModel> eventHistoryModelList;
    private EventHistoryAdapter adapter;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_event_history;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("上报事件处理记录");
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        eventHistoryModelList = new ArrayList<>();
        if (getIntent() != null) {
            String eventId = getIntent().getExtras().getString("eventId");
            getEventHistoryList(eventId);
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
        adapter = new EventHistoryAdapter(mContext, eventHistoryModelList, R.layout.adapter_item_event_history);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                EventHistoryModel eventHistoryModel = eventHistoryModelList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("eventId", eventHistoryModel.getEventid());
                bundle.putString("createTime", TimeUtil.longToFormatTimeHMS(eventHistoryModel.getCreatime().getTime()));
                openActivity(EventHistoryDetailActivity.class, bundle);

            }
        });
    }

    private void getEventHistoryList(String eventId) {
        JsonObject params = new JsonObject();
        params.addProperty("eventid", eventId);
        Net.create(Api.class).getEventHistory(token, params)
                .enqueue(new NetCallback<List<EventHistoryModel>>(this, true) {
                    @Override
                    public void onResponse(List<EventHistoryModel> list) {
                        eventHistoryModelList.clear();
                        if (list != null && list.size() > 0) {
                            eventHistoryModelList.addAll(list);
                            adapter.notifyDataSetChanged();
                            refreshLayout.setVisibility(View.VISIBLE);
                            llNoData.setVisibility(View.GONE);
                        } else {
                            refreshLayout.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
    @OnClick({
            R.id.iv_back,
    })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

        }
    }

}
