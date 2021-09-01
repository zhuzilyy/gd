package com.gd.form.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
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
import com.gd.form.constants.Constant;
import com.gd.form.model.ServerModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.NumberUtil;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.ToastUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class PipeMeasureActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.et_pipeDepth)
    EditText etPipeDepth;
    @BindView(R.id.et_opticalCableDepth)
    EditText etOpticalCableDepth;
    @BindView(R.id.et_depthNotEnough)
    EditText etDepthNotEnough;
    @BindView(R.id.et_method)
    EditText etMethod;
    private TimePickerView pvTime;
    private String token, userId, stationId;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_pipe_measure;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("管道埋深测量");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("记录");
        initTimePicker();
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        if (getIntent() != null) {
            stationId = getIntent().getExtras().getString("stationId");
        }
    }

    @OnClick({
            R.id.iv_back,
            R.id.tv_right,
            R.id.btn_commit,
            R.id.ll_time,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_time:
                pvTime.show(view);
                break;
            case R.id.btn_commit:
                if (paramsComplete()) {
                    commit();
                }
                break;
            case R.id.tv_right:
                Bundle bundle = new Bundle();
                bundle.putString("stationId", stationId);
                openActivity(MeasureRecordActivity.class, bundle);
                break;

        }
    }
    private void commit() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("stakeid", Integer.valueOf(stationId));
        jsonObject.addProperty("measuredate", tvTime.getText().toString());
        jsonObject.addProperty("tester", userId);
        jsonObject.addProperty("pipedeep", etPipeDepth.getText().toString());
        jsonObject.addProperty("cabledeep", etOpticalCableDepth.getText().toString());
        jsonObject.addProperty("lockdeep", etDepthNotEnough.getText().toString());
        jsonObject.addProperty("resolution", etMethod.getText().toString());
        Net.create(Api.class).addMeasureRecords(token, jsonObject)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        ToastUtil.show(result.getMsg());
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            finish();
                        }
                    }
                });
    }

    private void initTimePicker() {
        //Dialog 模式下，在底部弹出
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                tvTime.setText(format.format(date));

            }
        }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
            @Override
            public void onTimeSelectChanged(Date date) {
                Log.i("pvTime", "onTimeSelectChanged");
            }
        }).setType(new boolean[]{true, true, true, false, false, false})
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

    private boolean paramsComplete() {
        if (TextUtils.isEmpty(tvTime.getText().toString())) {
            ToastUtil.show("请选择测量时间");
            return false;
        }

        if (TextUtils.isEmpty(etPipeDepth.getText().toString())) {
            ToastUtil.show("管道埋深不能为空");
            return false;
        }
        if (!NumberUtil.isNumber(etPipeDepth.getText().toString())) {
            ToastUtil.show("管道埋深据格式不正确");
            return false;
        }

        if (TextUtils.isEmpty(etOpticalCableDepth.getText().toString())) {
            ToastUtil.show("光缆埋深不能为空");
            return false;
        }
        if (!NumberUtil.isNumber(etOpticalCableDepth.getText().toString())) {
            ToastUtil.show("光缆埋深数据格式不正确");
            return false;
        }
        if (TextUtils.isEmpty(etMethod.getText().toString())) {
            ToastUtil.show("请输入维护记录");
            return false;
        }
        return true;
    }


}
