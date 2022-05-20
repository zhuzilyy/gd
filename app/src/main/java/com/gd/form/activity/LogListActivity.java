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
import com.gd.form.adapter.LogListAdapter;
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.Department;
import com.gd.form.model.LogBean;
import com.gd.form.model.ServerModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.TimeUtil;
import com.gd.form.view.DeleteDialog;
import com.gd.form.view.ListDialog;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class LogListActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_departmentName)
    TextView tvDepartmentName;
    private String token, userId, departmentId, departmentName;
    private List<LogBean> logBeanList;
    private LogListAdapter adapter;
    private DeleteDialog deleteDialog;
    private int deleteIndex;
    private List<Department> departmentList;
    private ListDialog dialog;
    private List<String> areaList;
    private List<Integer> idList;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_log_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = (String) SPUtil.get(LogListActivity.this, "token", "");
        userId = (String) SPUtil.get(LogListActivity.this, "userId", "");
        departmentId = (String) SPUtil.get(LogListActivity.this, "departmentId", "");
        tvTitle.setText("日志记录清单");
        llNoData.setVisibility(View.GONE);
        deleteDialog = new DeleteDialog(this);
        dialog = new ListDialog(this);
        departmentList = new ArrayList<>();
        areaList = new ArrayList<>();
        idList = new ArrayList<>();
        logBeanList = new ArrayList<>();
        initData();
        getLogList();
        pipeDepartmentInfoGetList();
    }

    private void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(LogListActivity.this));
        adapter = new LogListAdapter(LogListActivity.this, logBeanList, R.layout.adapter_log_list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                switch (v.getId()) {
                    case R.id.btn_check:
                        Bundle bundle = new Bundle();
                        LogBean logBean = logBeanList.get(position);
                        String creatorName = logBean.getCreator();
                        int departmentId = logBean.getDepartmentid();
                        String time =TimeUtil.longToFormatTime(logBean.getCreatime().getTime());
                        bundle.putString("creatorName",creatorName);
                        bundle.putInt("departmentId",departmentId);
                        bundle.putString("time",time);
                        bundle.putString("tag","logList");
                        openActivity(LogDetailActivity.class,bundle);
                        break;
                    case R.id.btn_delete:
                        deleteIndex = position;
                        deleteDialog.show();
                        break;
                }
            }
        });
        deleteDialog.setOnClickBottomListener(new DeleteDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick() {
                deleteDialog.dismiss();
                deleteLog();
            }

            @Override
            public void onNegativeClick() {
                deleteDialog.dismiss();
            }
        });
    }

    private void deleteLog() {
        JsonObject params = new JsonObject();
        params.addProperty("departmentid",departmentId);
        params.addProperty("creator",userId);
        params.addProperty("creatime", TimeUtil.longToFormatTime(logBeanList.get(deleteIndex).getCreatime().getTime()));
        Net.create(Api.class).deleteLog(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            logBeanList.remove(deleteIndex);
                            adapter.notifyDataSetChanged();
                            if (logBeanList != null && logBeanList.size() > 0) {
                                llNoData.setVisibility(View.GONE);
                                refreshLayout.setVisibility(View.VISIBLE);
                            } else {
                                llNoData.setVisibility(View.VISIBLE);
                                refreshLayout.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }

    private void getLogList() {
        logBeanList.clear();
        JsonObject params = new JsonObject();
        params.addProperty("departmentid", departmentId);
        params.addProperty("creator", userId);
        Net.create(Api.class).getLogList(token, params)
                .enqueue(new NetCallback<List<LogBean>>(LogListActivity.this, true) {
                    @Override
                    public void onResponse(List<LogBean> list) {
                        if (list != null && list.size() > 0) {
                            logBeanList.addAll(list);
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

    private void pipeDepartmentInfoGetList() {
        JsonObject params = new JsonObject();
        params.addProperty("employid", userId);
        Net.create(Api.class).getProjectDepartmentById(token, params)
                .enqueue(new NetCallback<List<Department>>(this, false) {
                    @Override
                    public void onResponse(List<Department> list) {
                        departmentList = list;
                        if (departmentList != null && departmentList.size() > 0) {
                            for (int i = 0; i < departmentList.size(); i++) {
                                areaList.add(departmentList.get(i).getName());
                                idList.add(departmentList.get(i).getId());
                            }
                            tvDepartmentName.setText(departmentList.get(0).getName());
                            departmentId = departmentList.get(0).getId()+"";
                        }
                    }
                });
    }

    @OnClick({
            R.id.iv_back,
            R.id.ll_selectDepartment,
          })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_selectDepartment:
                if (areaList.size() == 0) {
                    pipeDepartmentInfoGetList();
                }
                dialog.setData(areaList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvDepartmentName.setText(areaList.get(positionM));
                    departmentId = idList.get(positionM) + "";
                    getLogList();
                    dialog.dismiss();
                });
                break;
        }
    }
}
