package com.gd.form.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.gd.form.R;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.HighZoneModel;
import com.gd.form.model.Pipelineinfo;
import com.gd.form.model.ServerModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.NumberUtil;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.ToastUtil;
import com.gd.form.view.ListDialog;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PipeHighZoneActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.tv_pipeName)
    TextView tvPipeName;
    @BindView(R.id.et_highZoneName)
    EditText etHighZoneName;
    @BindView(R.id.et_location)
    EditText etLocation;
    @BindView(R.id.et_length)
    EditText etLength;
    @BindView(R.id.et_level)
    EditText etLevel;
    @BindView(R.id.et_desc)
    EditText etDesc;
    @BindView(R.id.et_controlMethod)
    EditText etControlMethod;
    @BindView(R.id.et_recognise)
    EditText etRecognise;
    @BindView(R.id.et_locationLevel)
    EditText etLocationLevel;
    @BindView(R.id.et_riskLevel)
    EditText etRiskLevel;
    @BindView(R.id.et_ply)
    EditText etPly;
    @BindView(R.id.et_relativeAreaLevel)
    EditText etRelativeAreaLevel;
    @BindView(R.id.et_death)
    EditText etDeath;
    @BindView(R.id.et_influence)
    EditText etInfluence;
    @BindView(R.id.et_type)
    EditText etType;
    @BindView(R.id.rg_isAdd)
    RadioGroup rgIsAdd;
    @BindView(R.id.rb_yes)
    RadioButton rbYes;
    @BindView(R.id.rb_no)
    RadioButton rbNo;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.ll_pipeName)
    LinearLayout llPipeName;
    private String isAdd = "是";
    private String stationId, pipeId, pipeName;
    private String token, userId, highZoneId;
    private final int SEARCH_HIGHZONE = 100;
    private List<Pipelineinfo> pipelineInfoList;
    private ListDialog dialog;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_pipe_high_zone;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListener();
        getPipelineInfoListRequest();
        tvTitle.setText("高后果区");
        dialog = new ListDialog(this);
        if (getIntent() != null) {
            stationId = getIntent().getExtras().getString("stationId");
            pipeId = getIntent().getExtras().getString("pipeId");
            pipeName = getIntent().getExtras().getString("pipeName");
            highZoneId = getIntent().getExtras().getString("highZoneId");
            String tag = getIntent().getExtras().getString("tag");
            //根据接口获取数据
            if (!TextUtils.isEmpty(highZoneId)) {
                getHighZoneData(highZoneId);
            }
            if ("add".equals(tag)) {
                btnCommit.setVisibility(View.VISIBLE);
                llSearch.setVisibility(View.VISIBLE);
                etHighZoneName.setEnabled(true);
                etLocation.setEnabled(true);
                etLength.setEnabled(true);
                etLevel.setEnabled(true);
                etDesc.setEnabled(true);
                etControlMethod.setEnabled(true);
                etRecognise.setEnabled(true);
                etLocationLevel.setEnabled(true);
                etRiskLevel.setEnabled(true);
                etPly.setEnabled(true);
                etRelativeAreaLevel.setEnabled(true);
                etDeath.setEnabled(true);
                etInfluence.setEnabled(true);
                rbYes.setEnabled(true);
                rbNo.setEnabled(true);
                rgIsAdd.setEnabled(true);
                etType.setEnabled(true);
                llPipeName.setEnabled(true);
            } else if ("check".equals(tag)) {
                btnCommit.setVisibility(View.GONE);
                llSearch.setVisibility(View.GONE);
                etHighZoneName.setEnabled(false);
                etLocation.setEnabled(false);
                etLength.setEnabled(false);
                etLevel.setEnabled(false);
                etDesc.setEnabled(false);
                etControlMethod.setEnabled(false);
                etRecognise.setEnabled(false);
                etLocationLevel.setEnabled(false);
                etRiskLevel.setEnabled(false);
                etPly.setEnabled(false);
                etRelativeAreaLevel.setEnabled(false);
                etDeath.setEnabled(false);
                etInfluence.setEnabled(false);
                rbYes.setEnabled(false);
                rbNo.setEnabled(false);
                rgIsAdd.setEnabled(false);
                etType.setEnabled(false);
                etType.setEnabled(false);
                llPipeName.setEnabled(false);
            }
        }
        token = (String) SPUtil.get(PipeHighZoneActivity.this, "token", "");
        userId = (String) SPUtil.get(PipeHighZoneActivity.this, "userId", "");

    }

    private void getPipelineInfoListRequest() {

        Net.create(Api.class).pipelineinfosget()
                .enqueue(new NetCallback<List<Pipelineinfo>>(this, false) {
                    @Override
                    public void onResponse(List<Pipelineinfo> list) {
                        pipelineInfoList = list;
                    }
                });
    }

    private void getHighZoneData(String highZoneId) {
        JsonObject params = new JsonObject();
        params.addProperty("id", highZoneId);
        Net.create(Api.class).getHighZoneData(token, params)
                .enqueue(new NetCallback<List<HighZoneModel>>(this, true) {
                    @Override
                    public void onResponse(List<HighZoneModel> result) {
                        if (result != null && result.size() > 0) {
                            HighZoneModel highZoneModel = result.get(0);
                            tvPipeName.setText(highZoneModel.getPipedesc());
                            etHighZoneName.setText(highZoneModel.getName());
                            etLocation.setText(highZoneModel.getLocationdesc());
                            etLength.setText(highZoneModel.getLength() + "");
                            etLevel.setText(highZoneModel.getLevel());
                            etType.setText(highZoneModel.getHtype());
                            etDesc.setText(highZoneModel.getDesc());
                            etControlMethod.setText(highZoneModel.getControlmeasures());
                            etRecognise.setText(highZoneModel.getIdentifiers());
                            etLocationLevel.setText(highZoneModel.getAreaslevel());
                            etRiskLevel.setText(highZoneModel.getRisklevel());
                            etPly.setText(highZoneModel.getPipethickness() + "");
                            etRelativeAreaLevel.setText(highZoneModel.getDesignlevel());
                            if (highZoneModel.getAreaslevelflag().equals("是")) {
                                rbYes.setChecked(true);
                            } else {
                                rbNo.setChecked(true);
                            }
                            etDeath.setText(highZoneModel.getArearadius());
                            etInfluence.setText(highZoneModel.getFluentionareas());
                        }
                    }
                });
    }

    private void initListener() {
        rgIsAdd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yes:
                        isAdd = "是";
                        break;
                    case R.id.rb_no:
                        isAdd = "否";
                        break;
                }
            }
        });
    }

    @OnClick({
            R.id.iv_back,
            R.id.btn_commit,
            R.id.ll_search,
            R.id.ll_pipeName,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_pipeName:
                List<String> pipeList = new ArrayList<>();
                List<Integer> pipeIdList = new ArrayList<>();
                if (pipelineInfoList != null && pipelineInfoList.size() > 0) {
                    for (int i = 0; i < pipelineInfoList.size(); i++) {
                        pipeList.add(pipelineInfoList.get(i).getName());
                        pipeIdList.add(pipelineInfoList.get(i).getId());
                    }
                }
                dialog.setData(pipeList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvPipeName.setText(pipeList.get(positionM));
                    pipeId = pipeIdList.get(positionM) + "";
                    dialog.dismiss();
                });
                break;
            case R.id.ll_search:
                Intent intent = new Intent(PipeHighZoneActivity.this, SearchHighZoneActivity.class);
                startActivityForResult(intent, SEARCH_HIGHZONE);
                break;
            case R.id.btn_commit:
                if (paramsComplete()) {
                    addHighZone();
                }
                break;
        }
    }

    private void addHighZone() {
        JsonObject params = new JsonObject();
        if(TextUtils.isEmpty(highZoneId)){
            params.addProperty("id", 0);
        }else{
            params.addProperty("id", Integer.parseInt(highZoneId));
        }
        params.addProperty("stakeid", Integer.valueOf(stationId));
        params.addProperty("pipeid", Integer.valueOf(pipeId));
        params.addProperty("pipedesc", tvPipeName.getText().toString());
        params.addProperty("name", etHighZoneName.getText().toString());
        params.addProperty("locationdesc", etLocation.getText().toString());
        params.addProperty("length", Double.parseDouble(etLength.getText().toString()));
        params.addProperty("level", etLevel.getText().toString());
        params.addProperty("htype", etType.getText().toString());
        params.addProperty("desc", etDesc.getText().toString());
        params.addProperty("controlmeasures", etControlMethod.getText().toString());
        params.addProperty("identifiers", etRecognise.getText().toString());
        params.addProperty("areaslevel", etLocationLevel.getText().toString());
        params.addProperty("risklevel", etRiskLevel.getText().toString());
        params.addProperty("pipethickness", Double.parseDouble(etPly.getText().toString()));
        params.addProperty("designlevel", etRelativeAreaLevel.getText().toString());
        params.addProperty("areaslevelflag", isAdd);
        params.addProperty("arearadius", etDeath.getText().toString());
        params.addProperty("fluentionareas", etInfluence.getText().toString());
        Log.i("tag", "params===" + params);
        Net.create(Api.class).addHighZone(token, params)
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
        if (TextUtils.isEmpty(etHighZoneName.getText().toString())) {
            ToastUtil.show("请输入高后果区名称");
            return false;
        }
        if (TextUtils.isEmpty(etLocation.getText().toString())) {
            ToastUtil.show("请输入所在区域");
            return false;
        }
        if (TextUtils.isEmpty(etLength.getText().toString())) {
            ToastUtil.show("请输入长度");
            return false;
        }
        if (!NumberUtil.isNumber(etLength.getText().toString())) {
            ToastUtil.show("长度格式不正确");
            return false;

        }
        if (TextUtils.isEmpty(etLevel.getText().toString())) {
            ToastUtil.show("请输入高后果区等级");
            return false;
        }
        if (TextUtils.isEmpty(etType.getText().toString())) {
            ToastUtil.show("请输入高后果区类型");
            return false;
        }
        if (TextUtils.isEmpty(etDesc.getText().toString())) {
            ToastUtil.show("请输入高后果区描述");
            return false;
        }
        if (TextUtils.isEmpty(etControlMethod.getText().toString())) {
            ToastUtil.show("请输入高后果区管控措施");
            return false;
        }
        if (TextUtils.isEmpty(etRecognise.getText().toString())) {
            ToastUtil.show("请输入高后果区识别项");
            return false;
        }
        if (TextUtils.isEmpty(etLocationLevel.getText().toString())) {
            ToastUtil.show("请输入地区等级");
            return false;
        }
        if (TextUtils.isEmpty(etRiskLevel.getText().toString())) {
            ToastUtil.show("请输入风险等级");
            return false;
        }
        if (TextUtils.isEmpty(etPly.getText().toString())) {
            ToastUtil.show("请输入管道壁厚");
            return false;
        }
        if (!NumberUtil.isNumber(etPly.getText().toString())) {
            ToastUtil.show("管道壁厚格式不正确");
            return false;

        }
        if (TextUtils.isEmpty(etRelativeAreaLevel.getText().toString())) {
            ToastUtil.show("请输入对应设计地区等级");
            return false;
        }
        if (TextUtils.isEmpty(etRelativeAreaLevel.getText().toString())) {
            ToastUtil.show("请输入对应设计地区等级");
            return false;
        }
        if (TextUtils.isEmpty(etDeath.getText().toString())) {
            ToastUtil.show("请输入致死半径区域");
            return false;
        }
        if (TextUtils.isEmpty(etInfluence.getText().toString())) {
            ToastUtil.show("请输入潜在影响区域");
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
        if (requestCode == SEARCH_HIGHZONE) {
            HighZoneModel highZoneModel = (HighZoneModel) data.getSerializableExtra("highZone");
            etHighZoneName.setText(highZoneModel.getName());
            etLocation.setText(highZoneModel.getLocationdesc());
            etLength.setText(highZoneModel.getLength() + "");
            etLevel.setText(highZoneModel.getLevel());
            etType.setText(highZoneModel.getHtype());
            etDesc.setText(highZoneModel.getDesc());
            etControlMethod.setText(highZoneModel.getControlmeasures());
            etRecognise.setText(highZoneModel.getIdentifiers());
            etLocationLevel.setText(highZoneModel.getAreaslevel());
            etRiskLevel.setText(highZoneModel.getRisklevel());
            etPly.setText(highZoneModel.getPipethickness() + "");
            etRelativeAreaLevel.setText(highZoneModel.getDesignlevel());
            if (highZoneModel.getAreaslevelflag().equals("是")) {
                rbYes.setChecked(true);
            } else {
                rbNo.setChecked(true);
            }
            etDeath.setText(highZoneModel.getArearadius());
            etInfluence.setText(highZoneModel.getFluentionareas());
        }
    }
}
