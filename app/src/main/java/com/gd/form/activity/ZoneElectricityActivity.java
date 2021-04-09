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
import com.gd.form.view.ListDialog;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ZoneElectricityActivity extends BaseActivity {
    private ListDialog dialog;
    private int FILE_REQUEST_CODE = 100;
    private int SELECT_STATION = 101;
    private int SELECT_APPROVER = 102;
    private int SELECT_ADDRESS = 103;
    private String approverName;
    private String approverId;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_weather)
    TextView tvWeather;
    @BindView(R.id.tv_spr)
    TextView tvSpr;
    @BindView(R.id.tv_fileName)
    TextView tvFileName;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.et_no)
    EditText etNo;
    @BindView(R.id.et_position)
    EditText etPosition;
    @BindView(R.id.et_electrical_position)
    EditText etElectricalPosition;
    @BindView(R.id.et_basePosition)
    EditText etBasePosition;
    @BindView(R.id.et_ground)
    EditText etGround;
    @BindView(R.id.et_temperature)
    EditText etTemperature;
    @BindView(R.id.et_person)
    EditText etPerson;
    @BindView(R.id.et_remark)
    EditText etRemark;


    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_zone_electricity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("区域阴保电位测试");
        dialog = new ListDialog(this);
    }

    @OnClick({R.id.iv_back,
            R.id.ll_weather,
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
            case R.id.ll_weather:
                List<String> weatherList = new ArrayList<>();
                weatherList.add("晴");
                weatherList.add("阴");
                weatherList.add("小雪");
                weatherList.add("大雪");
                weatherList.add("雨");
                dialog.setData(weatherList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvWeather.setText(weatherList.get(positionM));
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
        if (TextUtils.isEmpty(etPosition.getText().toString())) {
            ToastUtil.show("请输入测试位置");
            return false;
        }
        if (TextUtils.isEmpty(etElectricalPosition.getText().toString())) {
            ToastUtil.show("请输入管地电位");
            return false;
        }
        if (TextUtils.isEmpty(etBasePosition.getText().toString())) {
            ToastUtil.show("请输入基准电位");
            return false;
        }
        if (TextUtils.isEmpty(etGround.getText().toString())) {
            ToastUtil.show("请输入土壤电阻率");
            return false;
        }
        if (TextUtils.isEmpty(tvWeather.getText().toString())) {
            ToastUtil.show("请选择天气");
            return false;
        }
        if (TextUtils.isEmpty(etTemperature.getText().toString())) {
            ToastUtil.show("请输入温度");
            return false;
        }
        if (TextUtils.isEmpty(etPerson.getText().toString())) {
            ToastUtil.show("请输入测试人");
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
            //选择桩号
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
