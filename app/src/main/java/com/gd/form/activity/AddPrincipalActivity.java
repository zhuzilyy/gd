package com.gd.form.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.gd.form.utils.ToastUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class AddPrincipalActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_userName)
    TextView tvUserName;
    @BindView(R.id.tv_departmentName)
    TextView tvDepartmentName;
    private int SELECT_APPROVER = 100;
    private String approverName, approverId, departmentId, departmentName;
    private String stationId, pipeId, pipeOwners;
    private String token, userId;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_add_principal;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("添加负责人");
        token = (String) SPUtil.get(AddPrincipalActivity.this, "token", "");
        userId = (String) SPUtil.get(AddPrincipalActivity.this, "userId", "");
        if (getIntent() != null) {
            stationId = getIntent().getExtras().getString("stationId");
            pipeId = getIntent().getExtras().getString("pipeId");
            pipeOwners = getIntent().getExtras().getString("pipeOwners");
        }
    }

    @OnClick({
            R.id.iv_back,
            R.id.tv_right,
            R.id.ll_userName,
            R.id.ll_departmentName,
            R.id.btn_commit,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_commit:
                if (TextUtils.isEmpty(tvUserName.getText().toString())) {
                    ToastUtil.show("请选择用户");
                    return;
                }
                addPrincipal();
                break;
            case R.id.ll_userName:
            case R.id.ll_departmentName:
                Intent intentApprover = new Intent(this, ApproverActivity.class);
                startActivityForResult(intentApprover, SELECT_APPROVER);
                break;
        }
    }

    //添加责任人
    private void addPrincipal() {
        JsonObject params = new JsonObject();
        params.addProperty("stakeid", Integer.valueOf(stationId));
        params.addProperty("pipeid", Integer.valueOf(pipeId));
        if (!TextUtils.isEmpty(pipeOwners)) {
            params.addProperty("pipeowners", pipeOwners + ";" + departmentName + ":" + approverName);
        } else {
            params.addProperty("pipeowners", departmentName + ":" + approverName);
        }
        Log.i("tag","params===2222"+params.toString());
        Net.create(Api.class).addPrincipal(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        Log.i("tag",result.getCode()+"=44==");
                        Log.i("tag",result.getMsg()+"==44=");
                        ToastUtil.show(result.getMsg());
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent();
                            intent.setAction("com.action.update");
                            sendBroadcast(intent);
                            finish();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == SELECT_APPROVER) {
            approverName = data.getStringExtra("name");
            approverId = data.getStringExtra("id");
            departmentId = data.getStringExtra("departmentId");
            departmentName = data.getStringExtra("departmentName");
            tvUserName.setText(approverName);
            tvDepartmentName.setText(departmentName);
        }
    }
}

