package com.gd.form.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.ServerModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.NumberUtil;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.TimeUtil;
import com.gd.form.utils.ToastUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class AddProjectRecordActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.et_progress)
    TextView etProgress;
    @BindView(R.id.et_progressDetail)
    TextView etProgressDetail;
    private String projectId,token,userId;
    private TimePickerView pvTime;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_add_project_record;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("进度新增");
        projectId = getIntent().getExtras().getString("projectId");
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        initTimePicker();
    }

    @OnClick({R.id.iv_back, R.id.ll_time, R.id.btn_commit})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_time:
                pvTime.show(view);
                break;
            case R.id.btn_commit:
                if(!infoIsEmpty()){
                    commit();
                }
                break;

        }
    }

    private void commit() {
        JsonObject params = new JsonObject();
        params.addProperty("projectid", projectId);
        params.addProperty("recorddate", tvTime.getText().toString());
        params.addProperty("constructionprocess", etProgress.getText().toString());
        params.addProperty("processdesc", etProgressDetail.getText().toString());
        params.addProperty("creator",userId);
        params.addProperty("creatime", TimeUtil.getCurrentTime());
        Log.i("tag","params==="+params);
        Net.create(Api.class).addProjectRecord(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            ToastUtil.show("增加成功");
                            Intent intent = new Intent();
                            intent.setAction("com.action.add.success");
                            sendBroadcast(intent);
                            finish();
                        }else{
                            ToastUtil.show(result.getMsg());
                        }
                    }
                });
    }

    private boolean infoIsEmpty() {
        if (TextUtils.isEmpty(tvTime.getText().toString())) {
            ToastUtil.show("请选择开工时间");
            return true;
        }
        if (TextUtils.isEmpty(etProgress.getText().toString())) {
            ToastUtil.show("请输入进度");
            return true;
        }
        if (!NumberUtil.isNumber(etProgress.getText().toString())) {
            ToastUtil.show("进度格式输入不正确");
            return true;
        }
        if (TextUtils.isEmpty(etProgressDetail.getText().toString())) {
            ToastUtil.show("请输入具体描述");
            return true;
        }
        return false;
    }
    private void initTimePicker() {//Dialog 模式下，在底部弹出
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tvTime.setText(getTime(date));
            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        Log.i("pvTime", "onTimeSelectChanged");
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
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
        Log.d("getTime()", "choice date millis: " + date.getTime());
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}
