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
import com.gd.form.utils.ToastUtil;
import com.jaeger.library.StatusBarUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class MonitoringRepairActivity extends BaseActivity {
    private int FILE_REQUEST_CODE = 100;
    private int SELECT_STATION = 101;
    private int SELECT_APPROVER = 102;
    private int SELECT_ADDRESS = 103;
    private String approverName;
    private String approverId;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_spr)
    TextView tvSpr;
    @BindView(R.id.tv_fileName)
    TextView tvFileName;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.et_partName)
    EditText etPartName;
    @BindView(R.id.et_status)
    EditText etStatus;
    @BindView(R.id.et_question)
    EditText etQuestion;
    @BindView(R.id.et_result)
    EditText etResult;
    @BindView(R.id.et_remark)
    EditText etRemark;


    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_monitoring_repair;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("视频监控维修记录");
    }

    @OnClick({R.id.iv_back,
            R.id.ll_location,
            R.id.ll_scfj,
            R.id.ll_spr,
            R.id.btn_commit,
    })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_commit:
                paramsComplete();
                break;
            case R.id.ll_location:
                Intent intent = new Intent(this, MapActivity.class);
                startActivityForResult(intent, SELECT_ADDRESS);
                break;
            case R.id.ll_spr:
                Intent intentApprover = new Intent(this, ApproverActivity.class);
                startActivityForResult(intentApprover, SELECT_APPROVER);
                break;
            case R.id.ll_scfj:
//                Intent intentAddress = new Intent(this, SelectFileActivity.class);
//                startActivityForResult(intentAddress, FILE_REQUEST_CODE);
                Intent intentAddress = new Intent(Intent.ACTION_GET_CONTENT);
                intentAddress.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intentAddress.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intentAddress,FILE_REQUEST_CODE);
                break;
        }
    }

    private boolean paramsComplete() {
        if (TextUtils.isEmpty(etPartName.getText().toString())) {
            ToastUtil.show("请输入部件名称");
            return false;
        }
        if (TextUtils.isEmpty(etStatus.getText().toString())) {
            ToastUtil.show("请输入运行状况");
            return false;
        }
        if (TextUtils.isEmpty(etQuestion.getText().toString())) {
            ToastUtil.show("请输入存在问题");
            return false;
        }
        if (TextUtils.isEmpty(etResult.getText().toString())) {
            ToastUtil.show("请输入处理结果");
            return false;
        }
        if (TextUtils.isEmpty(etRemark.getText().toString())) {
            ToastUtil.show("请输入备注");
            return false;
        }
        if (TextUtils.isEmpty(tvLocation.getText().toString())) {
            ToastUtil.show("请输入坐标");
            return false;
        }
        if (TextUtils.isEmpty(tvSpr.getText().toString())) {
            ToastUtil.show("请选择审批人");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == FILE_REQUEST_CODE) {
            String name = data.getStringExtra("fileName");
            tvFileName.setText(name);
        } else if (requestCode == SELECT_ADDRESS) {
            String latitude = data.getStringExtra("latitude");
            String longitude = data.getStringExtra("longitude");
            if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                tvLocation.setText("经度:" + longitude + "   纬度:" + latitude);
            }
        } else if (requestCode == SELECT_APPROVER) {
            approverName = data.getStringExtra("name");
            approverId = data.getStringExtra("id");
            if (!TextUtils.isEmpty(approverName)) {
                tvSpr.setText(approverName);
            }
        }

    }
}
