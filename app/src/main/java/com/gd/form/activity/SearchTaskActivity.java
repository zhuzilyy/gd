package com.gd.form.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.SearchArea;
import com.gd.form.model.SearchPerson;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.ToastUtil;
import com.gd.form.view.ListDialog;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchTaskActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_startTime)
    TextView tvStartTime;
    @BindView(R.id.tv_endTime)
    TextView tvEndTime;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_taskStatus)
    TextView tvTaskStatus;
    private TimePickerView pvTime;
    private String token, userId;
    private ListDialog dialog;
    private long longStartTime, longEndTime;
    private List<String> areaList, personNameList, personIdList, taskStatusList;
    private List<Integer> areaIdList;
    private int selectAreaId;
    private String selectPersonId, taskStatus;
    private int selectPosition = 0;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_search_task;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("任务查询");
        initTimePicker();
        token = (String) SPUtil.get(SearchTaskActivity.this, "token", "");
        userId = (String) SPUtil.get(SearchTaskActivity.this, "userId", "");
        dialog = new ListDialog(this);
        areaList = new ArrayList<>();
        areaIdList = new ArrayList<>();
        personNameList = new ArrayList<>();
        personIdList = new ArrayList<>();
        taskStatusList = new ArrayList<>();
        taskStatusList.add("待办任务");
        taskStatusList.add("超期任务");
        taskStatusList.add("超期未批");
        taskStatusList.add("审核未通过");
        taskStatusList.add("下发任务待完成");
        taskStatusList.add("下发任务已完成");
        taskStatusList.add("下发任务超期未完成");
        taskStatusList.add("下发任务超期完成");
    }

    @OnClick({
            R.id.iv_back,
            R.id.ll_area,
            R.id.ll_name,
            R.id.ll_startTime,
            R.id.ll_endTime,
            R.id.btn_search,
            R.id.ll_taskStatus,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_taskStatus:
                dialog.setData(taskStatusList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvTaskStatus.setText(taskStatusList.get(positionM));
                    taskStatus = (positionM - 3) + "";
                    dialog.dismiss();
                    selectPosition = positionM;
                });
                break;
            case R.id.ll_name:
                if (TextUtils.isEmpty(tvArea.getText().toString())) {
                    ToastUtil.show("请先选择作业区");
                    return;
                }
                getPersonName();
                break;
            case R.id.ll_area:
                getArea();
                break;
            case R.id.ll_startTime:
            case R.id.ll_endTime:
                pvTime.show(view);
                break;
            case R.id.btn_search:
                if (paramsComplete()) {
                    if (selectPosition < 4) {
                        Bundle bundle = new Bundle();
                        bundle.putString("employId",selectPersonId);
                        if (selectPosition == 0) {
                            openActivity(WaitingActivity.class,bundle);
                        } else if (selectPosition == 1) {
                            openActivity(OverTimeTaskActivity.class,bundle);
                        }else if(selectPosition == 2){
                            openActivity(NoApproveActivity.class,bundle);
                        }else if(selectPosition == 3){
                            openActivity(RefuseTaskActivity.class,bundle);
                        }
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putInt("departmentId", selectAreaId);
                        bundle.putString("employId", selectPersonId);
                        bundle.putString("startTime", tvStartTime.getText().toString());
                        bundle.putString("endTime", tvEndTime.getText().toString());
                        bundle.putString("taskStatus", taskStatus);
                        openActivity(SearchTaskResultActivity.class, bundle);
                    }

                }
                break;
        }
    }

    private boolean paramsComplete() {
        if (TextUtils.isEmpty(tvArea.getText().toString())) {
            ToastUtil.show("请选择作业区有");
            return false;
        }
        if (TextUtils.isEmpty(tvName.getText().toString())) {
            ToastUtil.show("请选择人员名称");
            return false;
        }
        if (TextUtils.isEmpty(tvStartTime.getText().toString())) {
            ToastUtil.show("请选择开始时间");
            return false;
        }
        if (TextUtils.isEmpty(tvEndTime.getText().toString())) {
            ToastUtil.show("请选择结束时间");
            return false;
        }
        if (longEndTime < longStartTime) {
            ToastUtil.show("结束时间不能早于开始时间");
            return false;
        }
        if (TextUtils.isEmpty(tvTaskStatus.getText().toString())) {
            ToastUtil.show("请选择任务状态");
            return false;
        }
        return true;
    }

    //获取人员名称
    private void getPersonName() {
        JsonObject params = new JsonObject();
        params.addProperty("departmentid", selectAreaId);
        Net.create(Api.class).getSearchPerson(token, params)
                .enqueue(new NetCallback<List<SearchPerson>>(this, true) {
                    @Override
                    public void onResponse(List<SearchPerson> result) {
                        personIdList.clear();
                        personNameList.clear();
                        if (result.size() > 0) {
                            for (int i = 0; i < result.size(); i++) {
                                personNameList.add(result.get(i).getName());
                                personIdList.add(result.get(i).getId());
                            }
                            personNameList.add("All");
                            personIdList.add("All");
                            dialog.setData(personNameList);
                            dialog.show();
                            dialog.setListItemClick(positionM -> {
                                tvName.setText(personNameList.get(positionM));
                                selectPersonId = personIdList.get(positionM);
                                dialog.dismiss();
                            });
                        }

                    }
                });
    }

    //获取作业区
    private void getArea() {
        JsonObject params = new JsonObject();
        params.addProperty("employid", Long.valueOf(userId));
        Net.create(Api.class).getSearchArea(token, params)
                .enqueue(new NetCallback<List<SearchArea>>(this, true) {
                    @Override
                    public void onResponse(List<SearchArea> result) {
                        areaList.clear();
                        areaIdList.clear();
                        if (result.size() > 0) {
                            for (int i = 0; i < result.size(); i++) {
                                areaList.add(result.get(i).getName());
                                areaIdList.add(result.get(i).getId());
                            }
                            dialog.setData(areaList);
                            dialog.show();
                            dialog.setListItemClick(positionM -> {
                                tvArea.setText(areaList.get(positionM));
                                selectAreaId = areaIdList.get(positionM);
                                dialog.dismiss();
                            });
                        }

                    }
                });
    }

    private void initTimePicker() {
        //Dialog 模式下，在底部弹出
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (v.getId() == R.id.ll_startTime) {
                    tvStartTime.setText(getTime(date));
                    longStartTime = date.getTime();
                } else if (v.getId() == R.id.ll_endTime) {
                    tvEndTime.setText(getTime(date));
                    longEndTime = date.getTime();
                }
            }
        }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
            @Override
            public void onTimeSelectChanged(Date date) {
                Log.i("pvTime", "onTimeSelectChanged");
            }
        }).setType(new boolean[]{true, true, true, false, false, false}).isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("pvTime", "onCancelClickListener");
                    }
                })
                .setItemVisibleCount(5) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
                .setLineSpacingMultiplier(2.0f)
                .isAlphaGradient(true)
                .build();

        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f);
            }
        }
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}
