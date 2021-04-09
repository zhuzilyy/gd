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
import com.gd.form.model.Department;
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

public class InsulatingPropertyActivity extends BaseActivity {
    private List<Department> departmentList;
    private ListDialog dialog;
    private int FILE_REQUEST_CODE = 100;
    private int SELECT_STATION = 101;
    private int SELECT_APPROVER = 102;
    private int SELECT_ADDRESS = 103;
    private String approverName;
    private String approverId;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.tv_spr)
    TextView tvSpr;
    @BindView(R.id.tv_fileName)
    TextView tvFileName;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.et_no)
    EditText etNo;
    @BindView(R.id.et_room)
    EditText etRoom;
    @BindView(R.id.et_lineElectricity)
    EditText etLineElectricity;
    @BindView(R.id.et_distraction_electricity)
    EditText etDistractionElectricity;
    @BindView(R.id.et_position)
    EditText etPosition;
    @BindView(R.id.et_ground)
    EditText etGround;
    @BindView(R.id.et_blank_position)
    EditText etBlankPosition;
    @BindView(R.id.et_lightning_protection)
    EditText etLightningProtection;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.rg_isProperty)
    RadioGroup rg_isProperty;
    @BindView(R.id.rg_isBury)
    RadioGroup rg_isBury;


    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_insulating_property;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("阀室绝缘件性能测试");
        dialog = new ListDialog(this);
        pipeDepartmentInfoGetList();
        initListener();
    }

    private void initListener() {
        rg_isProperty.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesProperty:
                        break;
                    case R.id.rb_noProperty:
                        break;
                }
            }
        });
        rg_isBury.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesBury:
                        break;
                    case R.id.rb_noBury:
                        break;
                }
            }
        });
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

    @OnClick({R.id.iv_back,
            R.id.ll_area,
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
        if (TextUtils.isEmpty(etRoom.getText().toString())) {
            ToastUtil.show("请输入阀室");
            return false;
        }
        if (TextUtils.isEmpty(etLineElectricity.getText().toString())) {
            ToastUtil.show("请输入管道电位");
            return false;
        }
        if (TextUtils.isEmpty(etDistractionElectricity.getText().toString())) {
            ToastUtil.show("请输入管道干扰电压");
            return false;
        }
        if (TextUtils.isEmpty(etPosition.getText().toString())) {
            ToastUtil.show("请输入接地侧电位");
            return false;
        }
        if (TextUtils.isEmpty(etGround.getText().toString())) {
            ToastUtil.show("请输入接地侧干扰电压");
            return false;
        }
        if (TextUtils.isEmpty(etBlankPosition.getText().toString())) {
            ToastUtil.show("请输入放空侧电位");
            return false;
        }
        if (TextUtils.isEmpty(etLightningProtection.getText().toString())) {
            ToastUtil.show("请输入管道避雷器电压");
            return false;
        }
        if (TextUtils.isEmpty(tvArea.getText().toString())) {
            ToastUtil.show("请选择作业区");
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
