package com.gd.form.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gd.form.R;
import com.gd.form.adapter.FormAdapter;
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.FormModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.gd.form.utils.Util.activityList;

public class FormActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    private String token, userId;
    private FormAdapter formAdapter;
    private List<FormModel> formModelList;
    private JsonObject params;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_form;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityList.add(this);
        tvTitle.setText("表单查询结果");
        token = (String) SPUtil.get(FormActivity.this, "token", "");
        userId = (String) SPUtil.get(FormActivity.this, "userId", "");
        formModelList = new ArrayList<>();
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            params = new JsonObject();
            params.addProperty("departmentid", bundle.getInt("departmentid"));
            params.addProperty("profeid", bundle.getInt("profeid"));
            params.addProperty("employname", bundle.getString("employid"));
            params.addProperty("startime", bundle.getString("startime"));
            params.addProperty("endtime", bundle.getString("endtime"));
            params.addProperty("basecode", bundle.getString("basecode"));
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
        formAdapter = new FormAdapter(mContext, formModelList, R.layout.adapter_item_form);
        recyclerView.setAdapter(formAdapter);
        formAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                String formName = formModelList.get(position).getFormname();
                Bundle bundle = new Bundle();
                bundle.putString("tag", "detail");
                bundle.putString("formId", formModelList.get(position).getFormid());
                switch (formName) {
                    case "水工保护巡检表":
                        openActivity(ApproveWaterProtectionActivity.class, bundle);
                        break;
                    case "隧道外部检查表":
                        openActivity(ApproveTunnelActivity.class, bundle);
                        break;
                    case "重车碾压调查表":
                        openActivity(ApproveWeightCarActivity.class, bundle);
                        break;
                    case "违章违建处理记录":
                        openActivity(ApproveBuildingActivity.class, bundle);
                        break;
                    case "徒步巡检表（结对子）":
                        openActivity(ApproveHikingActivity.class, bundle);
                        break;
                    case "水工施工检查日志":
                        openActivity(ApproveWaterActivity.class, bundle);
                        break;
                    case "隐蔽工程检查记录":
                        openActivity(ApproveHiddenActivity.class, bundle);
                        break;
                    case "高后果区徒步巡检表":
                        openActivity(ApproveHighZoneActivity.class, bundle);
                        break;
                    case "视频监控查看记录":
                        openActivity(ApproveVideoActivity.class, bundle);
                        break;
                    case "区域阴保电位测试":
                        openActivity(ApproveElectricity.class, bundle);
                        break;
                    case "阀室绝缘件性能测试":
                        openActivity(ApproveInsulationActivity.class, bundle);
                        break;
                    case "去耦合器测试":
                        openActivity(ApproveDeviceActivity.class, bundle);
                        break;
                }
            }
        });
        Log.i("tag", "params====" + params);
        Net.create(Api.class).getAllForms(token, params)
                .enqueue(new NetCallback<List<FormModel>>(this, true) {
                    @Override
                    public void onResponse(List<FormModel> result) {
                        if (result != null && result.size() > 0) {
                            formModelList.addAll(result);
                            formAdapter.notifyDataSetChanged();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityList.remove(this);
    }
}
