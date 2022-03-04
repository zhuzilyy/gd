package com.gd.form.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gd.form.R;
import com.gd.form.adapter.ApproveAdapter;
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.Pipemploys;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.ToastUtil;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ApproverActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.lv_users)
    ExpandableListView lv_users;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    private String token, userId;
    private Map<String, List<Pipemploys>> dataMap;
    private ApproveAdapter approveAdapter;
    private List<Pipemploys> employsList;
    private List<Pipemploys> selectedEmploysList;
    private String selectIds;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_approver;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataMap = new HashMap<>();
        employsList = new ArrayList<>();
        selectedEmploysList = new ArrayList<>();
        if(getIntent().getExtras()!=null){
            selectIds =  getIntent().getExtras().getString("ids");
        }
        token = (String) SPUtil.get(ApproverActivity.this, "token", "");
        userId = (String) SPUtil.get(ApproverActivity.this, "userId", "");
        tvTitle.setText("选择人员");
        tvRight.setText("确定");
        tvRight.setVisibility(View.VISIBLE);
        initData();
    }

    private void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        approveAdapter = new ApproveAdapter(mContext, employsList, R.layout.item_expand_users_2);
        recyclerView.setAdapter(approveAdapter);
        approveAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                Pipemploys pipemploys = employsList.get(position);
                pipemploys.setSelected(!pipemploys.isSelected());
                approveAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getListRequest();
    }

    private void getListRequest() {
        Net.create(Api.class).pipemploysGetList(token)
                .enqueue(new NetCallback<List<Pipemploys>>(this, true) {
                    @Override
                    public void onResponse(List<Pipemploys> employs) {
                        if (employs != null && employs.size() > 0) {
                            for (int i = 0; i < employs.size(); i++) {
                                Pipemploys employ = employs.get(i);
                                String departmentName = employ.getDeptname();
                                List<Pipemploys> list = new ArrayList<>();
                                if (dataMap.containsKey(departmentName)) {
                                    list = dataMap.get(departmentName);
                                }
                                list.add(employ);
                                dataMap.put(departmentName, list);
                            }
                        }
                        if (dataMap.size() > 0) {
                            for (String departmentName : dataMap.keySet()) {
                                employsList.addAll(dataMap.get(departmentName));
                            }
                        }
                        if (employsList.size() > 0) {
                            if(!TextUtils.isEmpty(selectIds)){
                                String[] ids = selectIds.split(";");
                                for (int i = 0; i < employsList.size(); i++) {
                                    Pipemploys pipemploys =  employsList.get(i);
                                    for (int j = 0; j <ids.length ; j++) {
                                        if(pipemploys.getId().equals(ids[j])){
                                            pipemploys.setSelected(true);
                                            break;
                                        }
                                    }
                                }
                            }
                            llNoData.setVisibility(View.GONE);
                        } else {
                            llNoData.setVisibility(View.VISIBLE);
                        }
                        approveAdapter.notifyDataSetChanged();
                    }
                });
    }

    @OnClick({
            R.id.iv_back,
            R.id.tv_right,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                for (int i = 0; i < employsList.size(); i++) {
                    Pipemploys pipemploys = employsList.get(i);
                    if (pipemploys.isSelected()) {
                        selectedEmploysList.add(pipemploys);
                    }
                }
                if(selectedEmploysList.size()==0){
                    ToastUtil.show("请选择审核人员");
                    return;
                }
                if(selectedEmploysList.size()>0){
                    Intent intent = new Intent();
                    intent.putExtra("selectedList",(Serializable) selectedEmploysList);
                    setResult(RESULT_OK,intent);
                }
                finish();
                break;

        }
    }


}