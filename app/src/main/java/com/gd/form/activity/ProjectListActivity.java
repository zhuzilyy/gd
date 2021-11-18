package com.gd.form.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gd.form.R;
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.adapter.ProjectAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.Department;
import com.gd.form.model.ProjectModel;
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

public class ProjectListActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_departmentName)
    TextView tvDepartmentName;
    @BindView(R.id.tv_relativeProject)
    TextView tvRelativeProject;
    @BindView(R.id.view_relativeProject)
    View viewRelativeProject;
    @BindView(R.id.tv_planProject)
    TextView tvPlanProject;
    @BindView(R.id.view_planProject)
    View viewPlanProject;
    private ProjectAdapter adapter;
    private String token, userId, departmentId;
    private List<ProjectModel> projectModelList;
    private List<Department> departmentList;
    private ListDialog dialog;
    private List<String> areaList;
    private List<Integer> idList;
    private MyReceiver myReceiver;
    private String type = "相关工程";
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_project_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
//        departmentId = (String) SPUtil.get(this, "departmentId", "");
        projectModelList = new ArrayList<>();
        departmentList = new ArrayList<>();
        areaList = new ArrayList<>();
        idList = new ArrayList<>();
        dialog = new ListDialog(this);
        pipeDepartmentInfoGetList();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        adapter = new ProjectAdapter(this, projectModelList, R.layout.adapter_project);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("tag", "detail");
                bundle.putString("projectId", projectModelList.get(position).getProjectid());
                openActivity(AddProjectActivity.class, bundle);
            }
        });

        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.action.status.change");
        registerReceiver(myReceiver, intentFilter);


        IntentFilter intentFilterAddSuccess = new IntentFilter();
        intentFilterAddSuccess.addAction("com.action.add.success");
        registerReceiver(myReceiver, intentFilterAddSuccess);
    }

    private void getData() {
        JsonObject params = new JsonObject();
        params.addProperty("departmentid", departmentId);
        params.addProperty("projecttype", type);
        Log.i("tag","params===="+params);
        Net.create(Api.class).getProjectList(token, params)
                .enqueue(new NetCallback<List<ProjectModel>>(this, true) {
                    @Override
                    public void onResponse(List<ProjectModel> list) {
                        projectModelList.clear();
                        if (list != null && list.size() > 0) {
                            projectModelList.addAll(list);
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

    private void pipeDepartmentInfoGetList() {
        JsonObject params = new JsonObject();
        params.addProperty("employid", userId);
        Log.i("tag","params===="+params);
        Net.create(Api.class).getDepartmentById(token, params)
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
                            getData();
                        }
                    }
                });
    }

    @OnClick({
            R.id.iv_back,
            R.id.ll_departmentName,
            R.id.tv_right,
            R.id.ll_relativeProject,
            R.id.ll_planProject})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_relativeProject:
                if(type.equals("相关工程")){
                    return;
                }
                type = "相关工程";
                tvRelativeProject.setTextColor(Color.parseColor("#FF52A7F9"));
                viewRelativeProject.setVisibility(View.VISIBLE);
                tvPlanProject.setTextColor(Color.parseColor("#000000"));
                viewPlanProject.setVisibility(View.INVISIBLE);
                getData();
                break;
            case R.id.ll_planProject:
                if(type.equals("计划项目")){
                    return;
                }
                type = "计划项目";
                getData();
                tvPlanProject.setTextColor(Color.parseColor("#FF52A7F9"));
                viewPlanProject.setVisibility(View.VISIBLE);
                tvRelativeProject.setTextColor(Color.parseColor("#000000"));
                viewRelativeProject.setVisibility(View.INVISIBLE);
                break;
            case R.id.tv_right:
                Bundle bundle = new Bundle();
                bundle.putString("tag", "add");
                openActivity(AddProjectActivity.class, bundle);
                break;
            case R.id.ll_departmentName:
                if (areaList.size() == 0) {
                    pipeDepartmentInfoGetList();
                }
                dialog.setData(areaList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvDepartmentName.setText(areaList.get(positionM));
                    departmentId = idList.get(positionM) + "";
                    getData();
                    dialog.dismiss();
                });
                break;
        }
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.action.status.change") ||
                    intent.getAction().equals("com.action.add.success")) {
                projectModelList.clear();
                getData();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
        }
    }
}
