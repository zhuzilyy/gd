package com.gd.form.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.gd.form.model.BuildingModel;
import com.gd.form.model.ServerModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.ToastUtil;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PipeBuildingActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.et_startStationNo)
    EditText etStartStationNo;
    @BindView(R.id.et_location)
    EditText etLocation;
    @BindView(R.id.et_pipeProperty)
    EditText etPipeProperty;
    @BindView(R.id.et_type)
    EditText etType;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.rg_isHighZone)
    RadioGroup rgIsHighZone;
    @BindView(R.id.rb_yes)
    RadioButton rbYes;
    @BindView(R.id.rb_no)
    RadioButton rbNo;
    @BindView(R.id.et_missLength)
    EditText etMissLength;
    @BindView(R.id.et_minDistance)
    EditText etMinDistance;
    @BindView(R.id.et_missArea)
    EditText etMissArea;
    @BindView(R.id.et_personActivity)
    EditText etPersonActivity;
    @BindView(R.id.et_des)
    EditText etDes;
    @BindView(R.id.et_riskEvaluate)
    EditText etRiskEvaluate;
    @BindView(R.id.et_riskType)
    EditText etRiskType;
    @BindView(R.id.et_beforeChangeMethod)
    EditText etBeforeChangeMethod;
    @BindView(R.id.et_changeMethod)
    EditText etChangeMethod;
    @BindView(R.id.tv_pipeName)
    TextView tvPipeName;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.ll_pipeName)
    LinearLayout llPipeName;
    @BindView(R.id.ll_time)
    LinearLayout llTime;
    private String isHighZone = "是";
    private TimePickerView pvTime;
    private String token, userId, buildingId, stationId, pipeId, pipeName, stationName;
    private final int SEARCH_BUILDING = 100;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_building;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListener();
        initTimePicker();
        tvTitle.setText("违规违建");
        if (getIntent() != null) {
            String tag = getIntent().getExtras().getString("tag");
            buildingId = getIntent().getExtras().getString("buildingId");
            stationId = getIntent().getExtras().getString("stationId");
            pipeId = getIntent().getExtras().getString("pipeId");
            String pipeName = getIntent().getExtras().getString("pipeName");
            String stationName = getIntent().getExtras().getString("stationName");
            tvPipeName.setText(pipeName);
            etStartStationNo.setText(stationName);
            if ("add".equals(tag)) {
                btnCommit.setVisibility(View.VISIBLE);
                llSearch.setVisibility(View.VISIBLE);
                llPipeName.setEnabled(true);
                etStartStationNo.setEnabled(true);
                etLocation.setEnabled(true);
                etPipeProperty.setEnabled(true);
                etType.setEnabled(true);
                etName.setEnabled(true);
                tvTime.setEnabled(true);
                rgIsHighZone.setEnabled(true);
                etMissLength.setEnabled(true);
                etMinDistance.setEnabled(true);
                etMissArea.setEnabled(true);
                etPersonActivity.setEnabled(true);
                etDes.setEnabled(true);
                etRiskEvaluate.setEnabled(true);
                etRiskType.setEnabled(true);
                etBeforeChangeMethod.setEnabled(true);
                etChangeMethod.setEnabled(true);
                llTime.setEnabled(true);
                rbNo.setEnabled(true);
                rbYes.setEnabled(true);
            } else if (("check".equals(tag))) {
                btnCommit.setVisibility(View.GONE);
                llSearch.setVisibility(View.GONE);
                llPipeName.setEnabled(false);
                etStartStationNo.setEnabled(false);
                etLocation.setEnabled(false);
                etPipeProperty.setEnabled(false);
                etType.setEnabled(false);
                etName.setEnabled(false);
                tvTime.setEnabled(false);
                rgIsHighZone.setEnabled(false);
                etMissLength.setEnabled(false);
                etMinDistance.setEnabled(false);
                etMissArea.setEnabled(false);
                etPersonActivity.setEnabled(false);
                etDes.setEnabled(false);
                etRiskEvaluate.setEnabled(false);
                etRiskType.setEnabled(false);
                etBeforeChangeMethod.setEnabled(false);
                etChangeMethod.setEnabled(false);
                rbNo.setEnabled(false);
                rbYes.setEnabled(false);
                llTime.setEnabled(false);
                if(!TextUtils.isEmpty(buildingId)){
                    getBuildingData(buildingId);
                }
            }
        }
        token = (String) SPUtil.get(PipeBuildingActivity.this, "token", "");
        userId = (String) SPUtil.get(PipeBuildingActivity.this, "userId", "");
    }

    private void initTimePicker() {
        //Dialog 模式下，在底部弹出
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                tvTime.setText(format.format(date));
            }
        }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
            @Override
            public void onTimeSelectChanged(Date date) {
                Log.i("pvTime", "onTimeSelectChanged");
            }
        }).setType(new boolean[]{true, true, true, true, true, true})
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

    private void initListener() {
        rgIsHighZone.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yes:
                        isHighZone = "是";
                        break;
                    case R.id.rb_no:
                        isHighZone = "否";
                        break;
                }
            }
        });
    }

    private void getBuildingData(String buildingId) {
        JsonObject params = new JsonObject();
        params.addProperty("id", buildingId);
        Net.create(Api.class).getBuildingData(token, params)
                .enqueue(new NetCallback<List<BuildingModel>>(this, true) {
                    @Override
                    public void onResponse(List<BuildingModel> result) {
                        if (result != null && result.size() > 0) {
                            BuildingModel buildingModel = result.get(0);
                            String pipeDesc = buildingModel.getPipedesc();
                            if (!TextUtils.isEmpty(pipeDesc)) {
                                String[] descArr = pipeDesc.split(":");
                                tvPipeName.setText(descArr[0]);
                                etStartStationNo.setText(descArr[1]);
                            }
                            etLocation.setText(buildingModel.getLocationdesc());
                            etPipeProperty.setText(buildingModel.getOverpropety());
                            etType.setText(buildingModel.getOvertype());
                            etName.setText(buildingModel.getOvername());
                            tvTime.setText(buildingModel.getGenernaldate());
                            if (buildingModel.getHighareasflag().equals("是")) {
                                rbYes.setChecked(true);
                            } else {
                                rbNo.setChecked(true);
                            }
                            etMissLength.setText(buildingModel.getShortareas() + "");
                            etMinDistance.setText(buildingModel.getMinspacing() + "");
                            etMissArea.setText(buildingModel.getShortareas() + "");
                            etPersonActivity.setText(buildingModel.getPeractives());
                            etDes.setText(buildingModel.getDangerdesc());
                            etRiskEvaluate.setText(buildingModel.getRiskevaluation());
                            etRiskType.setText(buildingModel.getDangertype());
                            etBeforeChangeMethod.setText(buildingModel.getPresolution());
                            etChangeMethod.setText(buildingModel.getAftsolution());
                        }
                    }
                });
    }

    @OnClick({
            R.id.iv_back,
            R.id.btn_commit,
            R.id.ll_pipeName,
            R.id.ll_time,
            R.id.ll_search,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_commit:
                if (paramsComplete()) {
                    addBuilding();
                }
                break;
            case R.id.ll_time:
                pvTime.show(view);
                break;
            case R.id.ll_search:
                Intent intent = new Intent(PipeBuildingActivity.this, SearchBuildingActivity.class);
                startActivityForResult(intent, SEARCH_BUILDING);
                break;
        }
    }

    private void addBuilding() {
        JsonObject params = new JsonObject();
        params.addProperty("id", 0);
        params.addProperty("stakeid", Integer.valueOf(stationId));
        params.addProperty("pipeid", Integer.valueOf(pipeId));
        params.addProperty("locationdesc", etLocation.getText().toString());
        params.addProperty("overpropety", etPipeProperty.getText().toString());
        params.addProperty("overtype", etType.getText().toString());
        params.addProperty("overname", etName.getText().toString());
        params.addProperty("genernaldate", tvTime.getText().toString());
        params.addProperty("highareasflag", isHighZone);
        params.addProperty("shortlength", Double.parseDouble(etMissLength.getText().toString()));
        params.addProperty("minspacing", Double.parseDouble(etMinDistance.getText().toString()));
        params.addProperty("shortareas", Double.parseDouble(etMissArea.getText().toString()));
        params.addProperty("peractives", etPersonActivity.getText().toString());
        params.addProperty("dangerdesc", etDes.getText().toString());
        params.addProperty("riskevaluation", etRiskEvaluate.getText().toString());
        params.addProperty("dangertype", etRiskType.getText().toString());
        params.addProperty("presolution", etBeforeChangeMethod.getText().toString());
        params.addProperty("aftsolution", etChangeMethod.getText().toString());
        Net.create(Api.class).addBuilding(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            ToastUtil.show("保存成功");
                            Intent intent = new Intent();
                            intent.setAction("com.action.update");
                            sendBroadcast(intent);
                            finish();
                        } else {
                            ToastUtil.show("保存失败");
                        }
                    }
                });
    }

    private boolean paramsComplete() {
        if (TextUtils.isEmpty(tvPipeName.getText().toString())) {
            ToastUtil.show("请选择管道名称");
            return false;
        }
        if (TextUtils.isEmpty(etStartStationNo.getText().toString())) {
            ToastUtil.show("请输入起始桩号");
            return false;
        }
        if (TextUtils.isEmpty(etLocation.getText().toString())) {
            ToastUtil.show("请输入行政位置");
            return false;
        }
        if (TextUtils.isEmpty(etPipeProperty.getText().toString())) {
            ToastUtil.show("请输入占据性质");
            return false;
        }
        if (TextUtils.isEmpty(etType.getText().toString())) {
            ToastUtil.show("请输入占压类型");
            return false;
        }
        if (TextUtils.isEmpty(etName.getText().toString())) {
            ToastUtil.show("请输入占压名称");
            return false;
        }
        if (TextUtils.isEmpty(tvTime.getText().toString())) {
            ToastUtil.show("请选择形成时间");
            return false;
        }
        if (TextUtils.isEmpty(etMissLength.getText().toString())) {
            ToastUtil.show("请输入占压或安全距离不足长度(m)");
            return false;
        }
        if (TextUtils.isEmpty(etMinDistance.getText().toString())) {
            ToastUtil.show("请输入与管道最小间距(m)");
            return false;
        }
        if (TextUtils.isEmpty(etMissArea.getText().toString())) {
            ToastUtil.show("请输入占压或安全距离不足面积(㎡)");
            return false;
        }
        if (TextUtils.isEmpty(etPersonActivity.getText().toString())) {
            ToastUtil.show("请输入人员活动情况");
            return false;
        }
        if (TextUtils.isEmpty(etDes.getText().toString())) {
            ToastUtil.show("请输入隐患内容描述");
            return false;
        }
        if (TextUtils.isEmpty(etRiskEvaluate.getText().toString())) {
            ToastUtil.show("请输入风险评价");
            return false;
        }
        if (TextUtils.isEmpty(etRiskType.getText().toString())) {
            ToastUtil.show("请输入隐患类型");
            return false;
        }
        if (TextUtils.isEmpty(etBeforeChangeMethod.getText().toString())) {
            ToastUtil.show("请输入整改前控制措施");
            return false;
        }
        if (TextUtils.isEmpty(etChangeMethod.getText().toString())) {
            ToastUtil.show("请输入整改控制措施");
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
        if(requestCode == SEARCH_BUILDING){
            BuildingModel buildingModel = (BuildingModel)data.getSerializableExtra("building");
            String pipeDesc = buildingModel.getPipedesc();
            if (!TextUtils.isEmpty(pipeDesc)) {
                String[] descArr = pipeDesc.split(":");
                tvPipeName.setText(descArr[0]);
                etStartStationNo.setText(descArr[1]);
            }
            etLocation.setText(buildingModel.getLocationdesc());
            etPipeProperty.setText(buildingModel.getOverpropety());
            etType.setText(buildingModel.getOvertype());
            etName.setText(buildingModel.getOvername());
            tvTime.setText(buildingModel.getGenernaldate());
            if (buildingModel.getHighareasflag().equals("是")) {
                rbYes.setChecked(true);
            } else {
                rbNo.setChecked(true);
            }
            etMissLength.setText(buildingModel.getShortareas() + "");
            etMinDistance.setText(buildingModel.getMinspacing() + "");
            etMissArea.setText(buildingModel.getShortareas() + "");
            etPersonActivity.setText(buildingModel.getPeractives());
            etDes.setText(buildingModel.getDangerdesc());
            etRiskEvaluate.setText(buildingModel.getRiskevaluation());
            etRiskType.setText(buildingModel.getDangertype());
            etBeforeChangeMethod.setText(buildingModel.getPresolution());
            etChangeMethod.setText(buildingModel.getAftsolution());
        }
    }
}
