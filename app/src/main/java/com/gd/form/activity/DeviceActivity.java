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
import com.gd.form.model.Department;
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

public class DeviceActivity extends BaseActivity {
    private List<Department> departmentList;
    private List<Pipelineinfo> pipelineinfoList;
    private ListDialog dialog;
    private int FILE_REQUEST_CODE = 100;
    private int SELECT_APPROVER = 102;
    private int SELECT_ADDRESS = 103;
    private int SELECT_AREA = 104;
    private String approverName;
    private String approverId;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_pipeName)
    TextView tvPipeName;
    @BindView(R.id.tv_spr)
    TextView tvSpr;
    @BindView(R.id.tv_fileName)
    TextView tvFileName;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.et_no)
    EditText etNo;
    @BindView(R.id.et_deviceNo)
    EditText etDeviceNo;
    @BindView(R.id.et_position)
    EditText etPosition;
    @BindView(R.id.et_disturbance_voltage)
    EditText etDisturbanceVoltage;
    @BindView(R.id.et_ground_position)
    EditText etGroundPosition;
    @BindView(R.id.et_j_disturbance_voltage)
    EditText etJDisturbanceVoltage;
    @BindView(R.id.et_alternating_current)
    EditText etAlternatingCurrent;
    @BindView(R.id.et_dc)
    EditText etDc;
    @BindView(R.id.et_ground_material)
    EditText etGroundMaterial;
    @BindView(R.id.et_ground_resistance)
    EditText etGroundResistance;


    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_device;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("去耦合器测试");
        dialog = new ListDialog(this);
        pipeDepartmentInfoGetList();
        getPipelineInfoListRequest();
    }

    private void pipeDepartmentInfoGetList() {
        Net.create(Api.class).pipedepartmentinfoGetList()
                .enqueue(new NetCallback<List<Department>>(this, false) {
                    @Override
                    public void onResponse(List<Department> list) {
                        departmentList = list;
                    }
                });
    }

    private void getPipelineInfoListRequest() {

        Net.create(Api.class).pipelineinfosget()
                .enqueue(new NetCallback<List<Pipelineinfo>>(this, false) {
                    @Override
                    public void onResponse(List<Pipelineinfo> list) {
                        pipelineinfoList = list;
                    }
                });
    }

    @OnClick({R.id.iv_back,
            R.id.ll_area,
            R.id.ll_pipeName,
            R.id.ll_location,
            R.id.ll_scfj,
            R.id.ll_spr,
            R.id.ll_address,
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
            case R.id.ll_address:
                Intent intentArea = new Intent(this, MapActivity.class);
                startActivityForResult(intentArea, SELECT_AREA);
                break;
            case R.id.ll_pipeName:
                List<String> pipeList = new ArrayList<>();
                if (pipelineinfoList != null && pipelineinfoList.size() > 0) {
                    for (int i = 0; i < pipelineinfoList.size(); i++) {
                        pipeList.add(pipelineinfoList.get(i).getName());
                    }
                }
                dialog.setData(pipeList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvPipeName.setText(pipeList.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_area:
                List<String> areaList = new ArrayList<>();
                if (departmentList != null && departmentList.size() > 0) {
                    for (int i = 0; i < departmentList.size(); i++) {
                        areaList.add(departmentList.get(i).getName());
                    }
                }
                dialog.setData(areaList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvArea.setText(areaList.get(positionM));
                    dialog.dismiss();
                });
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
                Intent intentAddress = new Intent(this, SelectFileActivity.class);
                startActivityForResult(intentAddress, FILE_REQUEST_CODE);
                break;

        }
    }

    private boolean paramsComplete() {
        if (TextUtils.isEmpty(etNo.getText().toString())) {
            ToastUtil.show("请输入序号");
            return false;
        }
        if (TextUtils.isEmpty(tvArea.getText().toString())) {
            ToastUtil.show("请选择作业区");
            return false;
        }
        if (TextUtils.isEmpty(tvPipeName.getText().toString())) {
            ToastUtil.show("请选择管道名称");
            return false;
        }
        if (TextUtils.isEmpty(etDeviceNo.getText().toString())) {
            ToastUtil.show("请输入去耦合器桩号");
            return false;
        }
        if (TextUtils.isEmpty(etPosition.getText().toString())) {
            ToastUtil.show("请输入管地电位");
            return false;
        }
        if (TextUtils.isEmpty(etDisturbanceVoltage.getText().toString())) {
            ToastUtil.show("请输入交流干扰电压");
            return false;
        }
        if (TextUtils.isEmpty(etGroundPosition.getText().toString())) {
            ToastUtil.show("请输入对地电位");
            return false;
        }
        if (TextUtils.isEmpty(etJDisturbanceVoltage.getText().toString())) {
            ToastUtil.show("请输入交流干扰电压");
            return false;
        }
        if (TextUtils.isEmpty(etAlternatingCurrent.getText().toString())) {
            ToastUtil.show("请输入交流电流");
            return false;
        }
        if (TextUtils.isEmpty(etDc.getText().toString())) {
            ToastUtil.show("请输入直流电流");
            return false;
        }
        if (TextUtils.isEmpty(etGroundMaterial.getText().toString())) {
            ToastUtil.show("请输入接地材料");
            return false;
        }
        if (TextUtils.isEmpty(etGroundResistance.getText().toString())) {
            ToastUtil.show("请输入接地电阻");
            return false;
        }
        if (TextUtils.isEmpty(tvAddress.getText().toString())) {
            ToastUtil.show("请输入当前位置");
            return false;
        }
        if (TextUtils.isEmpty(tvLocation.getText().toString())) {
            ToastUtil.show("请输入当前坐标");
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
        } else if (requestCode == SELECT_AREA) {
            String area = data.getStringExtra("area");
            tvAddress.setText(area);
        }

    }
}
