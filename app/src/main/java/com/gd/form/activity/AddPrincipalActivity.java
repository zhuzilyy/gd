package com.gd.form.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
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
    }

    @OnClick({
            R.id.iv_back,
            R.id.tv_right,
            R.id.ll_userName,
            R.id.ll_departmentName,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_userName:
            case R.id.ll_departmentName:
                Intent intentApprover = new Intent(this, ApproverActivity.class);
                startActivityForResult(intentApprover, SELECT_APPROVER);
                break;
        }
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

