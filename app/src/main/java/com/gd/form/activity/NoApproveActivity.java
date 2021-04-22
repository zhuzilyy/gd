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
import com.gd.form.adapter.NoApproveAdapter;
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.NoApproveModel;
import com.gd.form.model.SearchForm;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.view.ListDialog;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NoApproveActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.tv_formType)
    TextView tvFormType;
    private NoApproveAdapter adapter;
    private String token, userId;
    private List<String> formBaseCodeList, formNameList;
    private ListDialog dialog;
    private List<NoApproveModel> noApproveModelList;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_no_approve;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("超期未批");
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        llNoData.setVisibility(View.GONE);
        dialog = new ListDialog(this);
        formBaseCodeList = new ArrayList<>();
        formNameList = new ArrayList<>();
        noApproveModelList = new ArrayList<>();
        refreshLayout.setOnRefreshListener(refreshLayout -> {

            refreshLayout.finishRefresh(2000);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {

            refreshLayout.finishLoadMore(2000);
        });
        initData();
        getNoApproveList("all");
    }

    private void getNoApproveList(String formBaseCode) {
        JsonObject params = new JsonObject();
        params.addProperty("employid", userId);
        params.addProperty("basecode", formBaseCode);
        params.addProperty("approvaltype", 2);
        params.addProperty("status", 3);
        Net.create(Api.class).noApproveList(token, params)
                .enqueue(new NetCallback<List<NoApproveModel>>(this, true) {
                    @Override
                    public void onResponse(List<NoApproveModel> list) {
                        noApproveModelList.clear();
                        if (list != null && list.size() > 0) {
                            noApproveModelList.addAll(list);
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

    private void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoApproveAdapter(this, noApproveModelList, R.layout.adapter_item_waiting);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {

            }
        });

    }

    @OnClick({R.id.ll_selectDepartment, R.id.iv_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_selectDepartment:
                getFormType();
                break;
        }
    }

    //获取工单类型
    private void getFormType() {
        JsonObject params = new JsonObject();
        params.addProperty("formid", "1");
        Net.create(Api.class).getSearchForm(token, params)
                .enqueue(new NetCallback<List<SearchForm>>(this, true) {
                    @Override
                    public void onResponse(List<SearchForm> result) {
                        formBaseCodeList.clear();
                        formNameList.clear();
                        if (result.size() > 0) {
                            for (int i = 0; i < result.size(); i++) {
                                formNameList.add(result.get(i).getName());
                                formBaseCodeList.add(result.get(i).getBasecode());
                            }
                            formNameList.add("ALL");
                            formBaseCodeList.add("ALL");
                            dialog.setData(formNameList);
                            dialog.show();
                            dialog.setListItemClick(positionM -> {
                                tvFormType.setText(formNameList.get(positionM));
                                String formBaseCode = formBaseCodeList.get(positionM);
                                getNoApproveList(formBaseCode);
                                dialog.dismiss();
                            });
                        }
                    }
                });
    }
}
