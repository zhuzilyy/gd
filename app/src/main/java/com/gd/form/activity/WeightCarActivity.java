package com.gd.form.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.model.Pipelineinfo;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.ToastUtil;
import com.gd.form.view.ListDialog;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class WeightCarActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_stationNo)
    TextView tvStationNo;
    @BindView(R.id.tv_spr)
    TextView tvSpr;
    @BindView(R.id.tv_fileName)
    TextView tvFileName;
    @BindView(R.id.tv_pipeName)
    TextView tvPipeName;
    @BindView(R.id.tv_departmentName)
    TextView tvDepartmentName;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_rate)
    TextView tvRate;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.et_roadName)
    EditText etRoadName;
    @BindView(R.id.et_roadWidth)
    EditText etRoadWidth;
    @BindView(R.id.et_pipeDiameter)
    EditText etPipeDiameter;
    @BindView(R.id.et_depth)
    EditText etDepth;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.rg_isProtect)
    RadioGroup rgProtect;

    private ListDialog dialog;
    private List<Pipelineinfo> pipeLineinfoList;
    private int FILE_REQUEST_CODE = 100;
    private int SELECT_STATION = 101;
    private int SELECT_APPROVER = 102;
    private int SELECT_ADDRESS = 103;
    private int SELECT_AREA = 104;
    private String approverName;
    private String approverId;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_weight_car;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("重车碾压调查表");
        if (dialog == null) {
            dialog = new ListDialog(mContext);
        }
        pipeLineinfoList = new ArrayList<>();
        getPipelineInfoListRequest();
        initListener();
    }

    private void initListener() {
        rgProtect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_yes:
                        break;
                    case R.id.rb_no:
                        break;

                }
            }
        });
    }

    private void getPipelineInfoListRequest() {

        Net.create(Api.class).pipelineinfosget()
                .enqueue(new NetCallback<List<Pipelineinfo>>(this, false) {
                    @Override
                    public void onResponse(List<Pipelineinfo> list) {
                        pipeLineinfoList = list;
                    }
                });
    }

    @OnClick({R.id.iv_back,
            R.id.ll_departmentName,
            R.id.ll_pipeName,
            R.id.ll_stationNo,
            R.id.ll_spr,
            R.id.ll_scfj,
            R.id.ll_rate,
            R.id.btn_commit,
            R.id.ll_address,
            R.id.ll_location})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_address:
                Intent intentArea = new Intent(this, MapActivity.class);
                startActivityForResult(intentArea, SELECT_AREA);
                break;
            case R.id.btn_commit:
                paramsComplete();
                break;
            case R.id.ll_rate:
                List<String> rateList = new ArrayList<>();
                rateList.add("0-20t");
                rateList.add("20-40t");
                rateList.add("40t-60t");
                rateList.add("60t以上");
                dialog.setData(rateList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvRate.setText(rateList.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_location:
                Intent intent = new Intent(WeightCarActivity.this, MapActivity.class);
                startActivityForResult(intent, SELECT_ADDRESS);
                break;
            case R.id.ll_scfj:
                Intent intentAddress = new Intent(WeightCarActivity.this, SelectFileActivity.class);
                startActivityForResult(intentAddress, FILE_REQUEST_CODE);
                break;
            case R.id.ll_spr:
                Intent intentApprover = new Intent(WeightCarActivity.this, ApproverActivity.class);
                startActivityForResult(intentApprover, SELECT_APPROVER);
                break;
            case R.id.ll_stationNo:
                Intent intentStation = new Intent(this, StationActivity.class);
                startActivityForResult(intentStation, SELECT_STATION);
                break;
            case R.id.ll_departmentName:
                List<String> nameList = new ArrayList<>();
                nameList.add("涞源管道维护站");
                nameList.add("紫荆关管道维护站");
                nameList.add("琉璃河管道维护站");
                nameList.add("石景山管道维护站");
                nameList.add("固安管道维护站");
                nameList.add("通州管道维护站");
                nameList.add("采育管道维护站");
                nameList.add("门头沟管道维护站");
                nameList.add("怀柔管道维护站");
                nameList.add("密云管道维护站");

                dialog.setData(nameList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvDepartmentName.setText(nameList.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_pipeName:
                List<String> pipeNameList = new ArrayList<>();
                if (pipeLineinfoList != null && pipeLineinfoList.size() > 0) {
                    for (int i = 0; i < pipeLineinfoList.size(); i++) {
                        pipeNameList.add(pipeLineinfoList.get(i).getName());
                    }
                }
                dialog.setData(pipeNameList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvPipeName.setText(pipeNameList.get(positionM));
                    dialog.dismiss();
                });
                break;
        }
    }

    private boolean paramsComplete() {
        if (TextUtils.isEmpty(tvDepartmentName.getText().toString())) {
            ToastUtil.show("请选择调查单位");
            return false;
        }
        if (TextUtils.isEmpty(tvPipeName.getText().toString())) {
            ToastUtil.show("请选择管道单位");
            return false;
        }
        if (TextUtils.isEmpty(tvStationNo.getText().toString())) {
            ToastUtil.show("请选择桩号");
            return false;
        }
        if (TextUtils.isEmpty(tvAddress.getText().toString())) {
            ToastUtil.show("请选择行政位置");
            return false;
        }
        if (TextUtils.isEmpty(etRoadName.getText().toString())) {
            ToastUtil.show("请输入道路名称");
            return false;
        }
        if (TextUtils.isEmpty(etRoadWidth.getText().toString())) {
            ToastUtil.show("请输入道路宽度");
            return false;
        }
        if (TextUtils.isEmpty(etPipeDiameter.getText().toString())) {
            ToastUtil.show("请输入管径");
            return false;
        }
        if (TextUtils.isEmpty(etDepth.getText().toString())) {
            ToastUtil.show("请输入埋深");
            return false;
        }
        if (TextUtils.isEmpty(tvRate.getText().toString())) {
            ToastUtil.show("请选择碾压频次");
            return false;
        }
        if (TextUtils.isEmpty(etRemark.getText().toString())) {
            ToastUtil.show("请输入备注");
            return false;
        }
        if (TextUtils.isEmpty(tvLocation.getText().toString())) {
            ToastUtil.show("请选择坐标");
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
        if (requestCode == SELECT_STATION) {
            String stationName = data.getStringExtra("stationName");
            tvStationNo.setText(stationName);
        } else if (requestCode == SELECT_APPROVER) {
            approverName = data.getStringExtra("name");
            approverId = data.getStringExtra("id");
            if (!TextUtils.isEmpty(approverName)) {
                tvSpr.setText(approverName);
            }
        } else if (requestCode == FILE_REQUEST_CODE) {
            String name = data.getStringExtra("fileName");
            tvFileName.setText(name);
        } else if (requestCode == SELECT_ADDRESS) {
            String latitude = data.getStringExtra("latitude");
            String longitude = data.getStringExtra("longitude");
            if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                tvLocation.setText("经度:" + longitude + "   纬度:" + latitude);
            }
        } else if (requestCode == SELECT_AREA) {
            String area = data.getStringExtra("area");
            tvAddress.setText(area);
        }

    }
}
