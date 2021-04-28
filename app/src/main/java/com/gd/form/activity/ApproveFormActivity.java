package com.gd.form.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.ServerModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.TimeUtil;
import com.gd.form.utils.ToastUtil;
import com.gd.form.utils.Util;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class ApproveFormActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_advice)
    EditText etAdvice;
    private String formId;
    private String token, userId;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_approve_task;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("审批");
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        if (getIntent() != null) {
            formId = getIntent().getExtras().getString("formid");
        }
        Util.activityList.add(this);
    }

    @OnClick({
            R.id.iv_back,
            R.id.btn_agree,
            R.id.btn_refuse,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_refuse:
                if (TextUtils.isEmpty(etAdvice.getText().toString())) {
                    ToastUtil.show("请输入审批意见");
                    return;
                }
                approve(etAdvice.getText().toString());
                break;
            case R.id.btn_agree:
                if (TextUtils.isEmpty(etAdvice.getText().toString())) {
                    ToastUtil.show("请输入审批意见");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("formId",formId);
                bundle.putString("advice",etAdvice.getText().toString());
                openActivity(SignActivity.class,bundle);
                break;

        }
    }
    private void approve(String advice) {
        JsonObject params = new JsonObject();
        params.addProperty("formid", formId);
        params.addProperty("approvalresult", 0);
        params.addProperty("approvalcomment", advice);
        params.addProperty("signfilepath", "00");
        params.addProperty("creatime", TimeUtil.longToFormatTimeHMS(System.currentTimeMillis()));
        Net.create(Api.class).approve(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        if(result.getCode() == Constant.SUCCESS_CODE){
                            Intent intent = new Intent();
                            intent.setAction("com.action.updateApprove");
                            sendBroadcast(intent);
                            Util.finishAll();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Util.activityList.remove(this);
    }
}
