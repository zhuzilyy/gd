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
import com.gd.form.model.DepartmentPerson;
import com.gd.form.model.ServerModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.NumberUtil;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.TimeUtil;
import com.gd.form.utils.ToastUtil;
import com.gd.form.view.ListDialog;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ElectricityRecordActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_stationNo)
    TextView tvStationNo;
    @BindView(R.id.et_power)
    EditText etPower;
    @BindView(R.id.et_blackout)
    EditText etBlackout;
    @BindView(R.id.et_base)
    EditText etBase;
    @BindView(R.id.et_ac)
    EditText etAc;
    @BindView(R.id.et_resistance)
    EditText etResistance;
    @BindView(R.id.et_weather)
    EditText etWeather;
    @BindView(R.id.et_temperature)
    EditText etTemperature;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_spr)
    TextView tvSpr;
    private int SELECT_STATION = 103;
    private int SELECT_AREA = 104;
    private String token, userId, approverId;
    private ListDialog dialog;
    private String lineId, stationId;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_electricity_record;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("阴保电位测试记录");
        token = (String) SPUtil.get(ElectricityRecordActivity.this, "token", "");
        userId = (String) SPUtil.get(ElectricityRecordActivity.this, "userId", "");
    }
    @OnClick({
            R.id.iv_back,
            R.id.ll_stationNo,
            R.id.btn_commit,
            R.id.ll_spr,
            R.id.ll_address,

    })
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_spr:
                getDefaultManager();
                break;
            case R.id.ll_address:
                Intent intentArea = new Intent(this, MapActivity.class);
                startActivityForResult(intentArea, SELECT_AREA);
                break;
            case R.id.btn_commit:
                if (paramsComplete()) {
                    commit();
                }
                break;
            case R.id.ll_stationNo:
                Intent intentStartStation = new Intent(this, StationStakeActivity.class);
                startActivityForResult(intentStartStation, SELECT_STATION);
                break;
        }
    }

    private void getDefaultManager() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("empid", userId);
        Net.create(Api.class).getTunnelDefaultManager(token, jsonObject)
                .enqueue(new NetCallback<List<DepartmentPerson>>(this, true) {
                    @Override
                    public void onResponse(List<DepartmentPerson> list) {
                        List<String> nameList = new ArrayList<>();
                        List<String> idList = new ArrayList<>();
                        if (list != null && list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                DepartmentPerson departmentPerson = list.get(i);
                                nameList.add(departmentPerson.getName());
                                idList.add(departmentPerson.getId());
                            }
                            if (dialog == null) {
                                dialog = new ListDialog(mContext);
                            }
                            dialog.setData(nameList);
                            dialog.show();
                            dialog.setListItemClick(positionM -> {
                                tvSpr.setText(nameList.get(positionM));
                                approverId = idList.get(positionM);
                                dialog.dismiss();
                            });
                        }
                    }
                });
    }

    private void commit() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pipeid", Integer.parseInt(lineId));
        jsonObject.addProperty("stakeid", Integer.parseInt(stationId));
        jsonObject.addProperty("col1", etPower.getText().toString());
        jsonObject.addProperty("col2", etBlackout.getText().toString());
        jsonObject.addProperty("col3", etBase.getText().toString());
        jsonObject.addProperty("col4", etAc.getText().toString());
        jsonObject.addProperty("col5", etResistance.getText().toString());
        jsonObject.addProperty("weathers", etWeather.getText().toString());
        jsonObject.addProperty("temperature", etTemperature.getText().toString());
        jsonObject.addProperty("locate", tvAddress.getText().toString());
        jsonObject.addProperty("creator", userId);
        jsonObject.addProperty("creatime", TimeUtil.getCurrentTime());
        jsonObject.addProperty("approvalid", approverId);
        Net.create(Api.class).commitElectricityRecord(token, jsonObject)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        ToastUtil.show(result.getMsg());
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            Intent intent = new Intent();
                            intent.setAction("com.action.update.waitingTask");
                            sendBroadcast(intent);
                            finish();
                        }

                    }
                });
    }

    private boolean paramsComplete() {
        if (TextUtils.isEmpty(tvStationNo.getText().toString())) {
            ToastUtil.show("请选择测试桩号");
            return false;
        }
        if (TextUtils.isEmpty(etPower.getText().toString())) {
            ToastUtil.show("通电电位不能为空");
            return false;
        }
        if (!NumberUtil.isNumber((etPower.getText().toString()))) {
            ToastUtil.show("通电电位格式输入不正确");
            return false;
        }
        if (TextUtils.isEmpty(etBlackout.getText().toString())) {
            ToastUtil.show("断电电位不能为空");
            return false;
        }
        if (!NumberUtil.isNumber((etBlackout.getText().toString()))) {
            ToastUtil.show("断电电位格式输入不正确");
            return false;
        }
        if (TextUtils.isEmpty(etBase.getText().toString())) {
            ToastUtil.show("基准电位不能为空");
            return false;
        }
        if (!NumberUtil.isNumber((etBase.getText().toString()))) {
            ToastUtil.show("基准电位格式输入不正确");
            return false;
        }
        if (TextUtils.isEmpty(etAc.getText().toString())) {
            ToastUtil.show("交流干扰电压不能为空");
            return false;
        }
        if (!NumberUtil.isNumber((etAc.getText().toString()))) {
            ToastUtil.show("交流干扰电压格式输入不正确");
            return false;
        }
        if (TextUtils.isEmpty(etResistance.getText().toString())) {
            ToastUtil.show("土壤电阻率不能为空");
            return false;
        }
        if (!NumberUtil.isNumber((etResistance.getText().toString()))) {
            ToastUtil.show("土壤电阻率格式输入不正确");
            return false;
        }
        if (TextUtils.isEmpty(etWeather.getText().toString())) {
            ToastUtil.show("天气不能为空");
            return false;
        }

        if (TextUtils.isEmpty(etTemperature.getText().toString())) {
            ToastUtil.show("温度不能为空");
            return false;
        }
        if (!NumberUtil.isNumber((etResistance.getText().toString()))) {
            ToastUtil.show("温度格式输入不正确");
            return false;
        }
        if (TextUtils.isEmpty(tvAddress.getText().toString())) {
            ToastUtil.show("填报位置不能为空");
            return false;
        }
        if (TextUtils.isEmpty(tvSpr.getText().toString())) {
            ToastUtil.show("审批人不能为空");
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
            stationId = data.getStringExtra("stationId");
            lineId = data.getStringExtra("lineId");
            String stationName = data.getStringExtra("stationName");
            tvStationNo.setText(stationName);
        } else if (requestCode == SELECT_AREA) {
            String area = data.getStringExtra("area");
            tvAddress.setText(area);
        }
    }
}
