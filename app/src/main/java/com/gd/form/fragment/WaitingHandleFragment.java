package com.gd.form.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gd.form.R;
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.adapter.WaitingAdapter;
import com.gd.form.base.BaseFragment;
import com.gd.form.model.SearchForm;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.view.ListDialog;
import com.google.gson.JsonObject;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class WaitingHandleFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.tv_formType)
    TextView tvFormType;
    private WaitingAdapter adapter;
    private String stationId, pipeId;
    private String token, userId;
    private List<String> formBaseCodeList,formNameList;
    private ListDialog dialog;
    private String formBaseCode;
    @Override
    protected void initView(Bundle bundle) {
        token = (String) SPUtil.get(getActivity(), "token", "");
        userId = (String) SPUtil.get(getActivity(), "userId", "");
        llNoData.setVisibility(View.GONE);
        dialog = new ListDialog(getActivity());
        formBaseCodeList = new ArrayList<>();
        formNameList = new ArrayList<>();
        refreshLayout.setOnRefreshListener(refreshLayout -> {

            refreshLayout.finishRefresh(2000);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {

            refreshLayout.finishLoadMore(2000);
        });
        initData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_waiting_handle;
    }

    private void initData() {
        List<String> values = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            values.add("");
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new WaitingAdapter(getActivity(), null, R.layout.adapter_item_waiting);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {

            }
        });

    }

    @OnClick({R.id.ll_selectDepartment})
    public void click(View view) {
        switch (view.getId()) {
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
                .enqueue(new NetCallback<List<SearchForm>>(getActivity(), true) {
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
                                formBaseCode = formBaseCodeList.get(positionM);
                                dialog.dismiss();
                            });
                        }
                    }
                });
    }
}
