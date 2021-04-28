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
import com.gd.form.adapter.OVerTimeAdapter;
import com.gd.form.adapter.OnItemClickListener;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.OverTimeModel;
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

public class OverTimeTaskActivity extends BaseActivity {
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
    private OVerTimeAdapter adapter;
    private String token, userId;
    private List<String> formBaseCodeList, formNameList;
    private ListDialog dialog;
    private List<OverTimeModel> overTimeModelList;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_over_time;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("超期任务");
        overTimeModelList = new ArrayList<>();
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        llNoData.setVisibility(View.GONE);
        dialog = new ListDialog(this);
        formBaseCodeList = new ArrayList<>();
        formNameList = new ArrayList<>();
        refreshLayout.setOnRefreshListener(refreshLayout -> {

            refreshLayout.finishRefresh(2000);
        });
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {

            refreshLayout.finishLoadMore(2000);
        });
        initData();
        getOverTimeList();
    }

    private void initData() {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OVerTimeAdapter(this, overTimeModelList, R.layout.adapter_item_waiting);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                String formName = overTimeModelList.get(position).getFormname();
                switch (formName) {
                    case "水工保护巡检表":
                        openActivity(SgbhActivity.class);
                        break;
                    case "隧道外部检查表":
                        openActivity(SdwbActivity.class);
                        break;
                    case "重车碾压调查表":
                        openActivity(WeightCarActivity.class);
                        break;
                    case "违章违建处理记录":
                        openActivity(EndorsementActivity.class);
                        break;
                    case "徒步巡检表（结对子）":
                        openActivity(HikingCheckActivity.class);
                        break;
                    case "水工施工检查日志":
                        openActivity(WaterInsuranceActivity.class);
                        break;
                    case "隐蔽工程检查记录":
                        openActivity(ConcealedWorkActivity.class);
                        break;
                    case "高后果区徒步巡检表":
                        openActivity(HighZoneActivity.class);
                        break;
                    case "视频监控查看记录":
                        openActivity(VideoMonitoringActivity.class);
                        break;
                    case "区域阴保电位测试":
                        openActivity(ZoneElectricityActivity.class);
                        break;
                    case "阀室绝缘件性能测试":
                        openActivity(InsulatingPropertyActivity.class);
                        break;
                    case "去耦合器测试":
                        openActivity(DeviceActivity.class);
                        break;
                }
            }
        });

    }
    private void getOverTimeList() {
        JsonObject params = new JsonObject();
        params.addProperty("employid", userId);
        Net.create(Api.class).overTimeList(token, params)
                .enqueue(new NetCallback<List<OverTimeModel>>(this, true) {
                    @Override
                    public void onResponse(List<OverTimeModel> list) {
                        overTimeModelList.clear();
                        if (list != null && list.size() > 0) {
                            overTimeModelList.addAll(list);
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
    @OnClick({R.id.ll_selectDepartment,R.id.iv_back})
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
                                dialog.dismiss();
                            });
                        }
                    }
                });
    }
}
