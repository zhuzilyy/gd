package com.gd.form.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
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

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
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
import com.gd.form.utils.WeiboDialogUtils;
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
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.ll_level)
    LinearLayout llLevel;
    @BindView(R.id.et_desc)
    EditText etDesc;
    @BindView(R.id.et_controlMethod)
    EditText etControlMethod;
    @BindView(R.id.tv_recognise)
    TextView tvRecognise;
    @BindView(R.id.tv_locationLevel)
    TextView tvLocationLevel;
    @BindView(R.id.ll_locationLevel)
    LinearLayout llLocationLevel;
    @BindView(R.id.tv_riskLevel)
    TextView tvRiskLevel;
    @BindView(R.id.ll_riskLevel)
    LinearLayout llRiskLevel;
    @BindView(R.id.et_ply)
    EditText etPly;
    @BindView(R.id.tv_relativeAreaLevel)
    TextView tvRelativeAreaLevel;
    @BindView(R.id.ll_relativeAreaLevel)
    LinearLayout llRelativeAreaLevel;
    @BindView(R.id.tv_death)
    TextView tvDeath;
    @BindView(R.id.ll_death)
    LinearLayout llDeath;
    @BindView(R.id.tv_influence)
    TextView tvInfluence;
    @BindView(R.id.ll_influence)
    LinearLayout llInfluence;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.ll_type)
    LinearLayout llType;
    @BindView(R.id.ll_recognise)
    LinearLayout llRecognise;
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
    @BindView(R.id.tv_fileName1)
    TextView tvFileName1;
    @BindView(R.id.tv_fileName2)
    TextView tvFileName2;
    @BindView(R.id.tv_fileName3)
    TextView tvFileName3;
    @BindView(R.id.ll_selectFile1)
    LinearLayout llSelectFile1;
    @BindView(R.id.ll_selectFile2)
    LinearLayout llSelectFile2;
    @BindView(R.id.ll_selectFile3)
    LinearLayout llSelectFile3;
    private String isAdd = "???";
    private String token,highZoneId;
    private final int SEARCH_HIGHZONE = 100;
    private int FILE_REQUEST_CODE1 = 101;
    private int FILE_REQUEST_CODE2 = 102;
    private int FILE_REQUEST_CODE3 = 103;
    private List<Pipelineinfo> pipelineInfoList;
    private ListDialog dialog;
    private List<String> typeList, recogniseList, locationLevelList, highZoneLevelList,
            relativeAreaLevelList, riskLevelList, deathList, influenceList;
    private String selectFileName1, selectFileName2, selectFileName3;
    private String selectFilePath1, selectFilePath2, selectFilePath3;
    private Dialog mWeiboDialog;
    private OSSCredentialProvider ossCredentialProvider;
    private OSS oss;
    private String ossFilePath1, ossFilePath2, ossFilePath3;
    private String tag;
    private String fileName1, fileName2, fileName3,uploadPath1,uploadPath2,uploadPath3;
    private int pipeId;
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
        token = (String) SPUtil.get(PipeHighZoneActivity.this, "token", "");
        tvTitle.setText("????????????");
        ossCredentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.ACCESSKEYID, Constant.ACCESSKEYSECRET);
        oss = new OSSClient(mContext.getApplicationContext(), Constant.ENDPOINT, ossCredentialProvider);
        typeList = new ArrayList<>();
        recogniseList = new ArrayList<>();
        locationLevelList = new ArrayList<>();
        relativeAreaLevelList = new ArrayList<>();
        riskLevelList = new ArrayList<>();
        deathList = new ArrayList<>();
        influenceList = new ArrayList<>();
        highZoneLevelList = new ArrayList<>();
        dialog = new ListDialog(this);
        if (getIntent() != null) {
            highZoneId = getIntent().getExtras().getString("highZoneId");
            tag = getIntent().getExtras().getString("tag");
            //????????????????????????
            if (!TextUtils.isEmpty(highZoneId)) {
                getHighZoneData(highZoneId);
            }
            if ("update".equals(tag)) {
                btnCommit.setVisibility(View.VISIBLE);
                llSearch.setVisibility(View.GONE);
                etHighZoneName.setEnabled(true);
                etLocation.setEnabled(true);
                etLength.setEnabled(true);
                llLevel.setEnabled(true);
                etDesc.setEnabled(true);
                etControlMethod.setEnabled(true);
                tvRecognise.setEnabled(true);
                tvLocationLevel.setEnabled(true);
                tvRiskLevel.setEnabled(true);
                etPly.setEnabled(true);
                tvRelativeAreaLevel.setEnabled(true);
                tvDeath.setEnabled(true);
                tvInfluence.setEnabled(true);
                rbYes.setEnabled(true);
                rbNo.setEnabled(true);
                rgIsAdd.setEnabled(true);
                tvType.setEnabled(true);
                llType.setEnabled(true);
                llPipeName.setEnabled(true);
                llRecognise.setEnabled(true);
                llLocationLevel.setEnabled(true);
                llRiskLevel.setEnabled(true);
                llDeath.setEnabled(true);
                llInfluence.setEnabled(true);
                llRelativeAreaLevel.setEnabled(true);
                llSelectFile1.setEnabled(true);
                llSelectFile2.setEnabled(true);
                llSelectFile3.setEnabled(true);
            } else if ("check".equals(tag)) {
                btnCommit.setVisibility(View.GONE);
                llSearch.setVisibility(View.GONE);
                etHighZoneName.setEnabled(false);
                etLocation.setEnabled(false);
                etLength.setEnabled(false);
                llLevel.setEnabled(false);
                etDesc.setEnabled(false);
                etControlMethod.setEnabled(false);
                tvRecognise.setEnabled(false);
                tvLocationLevel.setEnabled(false);
                tvRiskLevel.setEnabled(false);
                etPly.setEnabled(false);
                tvRelativeAreaLevel.setEnabled(false);
                tvDeath.setEnabled(false);
                tvInfluence.setEnabled(false);
                rbYes.setEnabled(false);
                rbNo.setEnabled(false);
                rgIsAdd.setEnabled(false);
                tvType.setEnabled(false);
                llType.setEnabled(false);
                llPipeName.setEnabled(false);
                llRecognise.setEnabled(false);
                llLocationLevel.setEnabled(false);
                llRiskLevel.setEnabled(false);
                llDeath.setEnabled(false);
                llInfluence.setEnabled(false);
                llRelativeAreaLevel.setEnabled(false);
//                llSelectFile1.setEnabled(false);
//                llSelectFile2.setEnabled(false);
//                llSelectFile3.setEnabled(false);
            }
        }
        initListener();
        getPipelineInfoListRequest();
        initDialogList();

    }

    private void initDialogList() {
        typeList.add("???????????????");
        typeList.add("??????????????????");
        typeList.add("????????????");
        typeList.add("???????????????&??????????????????");
        typeList.add("???????????????&????????????");
        typeList.add("????????????&??????????????????");
        typeList.add("????????????&??????????????????&???????????????");
        recogniseList.add("???????????????????????????");
        recogniseList.add("???????????????????????????");
        recogniseList.add("???????????????762mm???????????????????????????????????????6.9MPa??????????????????????????????????????????????????????????????????");
        recogniseList.add("???????????????273mm???????????????????????????????????????1.6MPa??????????????????????????????????????????????????????????????????");
        recogniseList.add("?????????????????????200m???????????????????????????");
        recogniseList.add("?????????????????????????????????????????????200m?????????????????????????????????????????????");
        locationLevelList.add("??????");
        locationLevelList.add("??????");
        locationLevelList.add("??????");
        locationLevelList.add("??????");
        relativeAreaLevelList.add("??????");
        relativeAreaLevelList.add("??????");
        relativeAreaLevelList.add("??????");
        relativeAreaLevelList.add("??????");
        riskLevelList.add("??????");
        riskLevelList.add("?????????");
        riskLevelList.add("??????");
        riskLevelList.add("?????????");
        riskLevelList.add("??????");
        deathList.add("130");
        deathList.add("199");
        deathList.add("251");
        deathList.add("301");
        deathList.add("329");
        influenceList.add("165");
        influenceList.add("253");
        influenceList.add("318");
        influenceList.add("381");
        influenceList.add("418");
        highZoneLevelList.add("??????");
        highZoneLevelList.add("??????");
        highZoneLevelList.add("??????");

    }

    private void getPipelineInfoListRequest() {

        Net.create(Api.class).pipelineinfosget(token)
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
        Log.i("tag","params=="+params);
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
                            tvLevel.setText(highZoneModel.getLevel());
                            tvType.setText(highZoneModel.getHtype());
                            etDesc.setText(highZoneModel.getDesc());
                            etControlMethod.setText(highZoneModel.getControlmeasures());
                            tvRecognise.setText(highZoneModel.getIdentifiers());
                            tvLocationLevel.setText(highZoneModel.getAreaslevel());
                            tvRiskLevel.setText(highZoneModel.getRisklevel());
                            etPly.setText(highZoneModel.getPipethickness() + "");
                            tvRelativeAreaLevel.setText(highZoneModel.getDesignlevel());
                            if (highZoneModel.getAreaslevelflag().equals("???")) {
                                rbYes.setChecked(true);
                            } else {
                                rbNo.setChecked(true);
                            }
                            tvDeath.setText(highZoneModel.getArearadius());
                            tvInfluence.setText(highZoneModel.getFluentionareas());
                            fileName1 = highZoneModel.getFilename1();
                            fileName2 = highZoneModel.getFilename2();
                            fileName3 = highZoneModel.getFilename3();
                            if(!TextUtils.isEmpty(fileName1) && fileName1.contains("/")){
                                fileName1 =  fileName1.split("/")[1];
                            }
                            if(!TextUtils.isEmpty(fileName2) && fileName2.contains("/")){
                                fileName2 =  fileName2.split("/")[1];
                            }
                            if(!TextUtils.isEmpty(fileName3) && fileName3.contains("/")){
                                fileName3 =  fileName3.split("/")[1];
                            }
                            pipeId = highZoneModel.getPipeid();
                            uploadPath1 = highZoneModel.getUploadfile1();
                            uploadPath2 = highZoneModel.getUploadfile2();
                            uploadPath3 = highZoneModel.getUploadfile3();
                            if (!TextUtils.isEmpty(fileName1)) {
                                tvFileName1.setText(fileName1);
                            }
                            if (!TextUtils.isEmpty(fileName2)) {
                                tvFileName2.setText(fileName2);
                            }
                            if (!TextUtils.isEmpty(fileName3)) {
                                tvFileName3.setText(fileName3);
                            }
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
                        isAdd = "???";
                        break;
                    case R.id.rb_no:
                        isAdd = "???";
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
            R.id.ll_type,
            R.id.ll_recognise,
            R.id.ll_locationLevel,
            R.id.ll_relativeAreaLevel,
            R.id.ll_riskLevel,
            R.id.ll_death,
            R.id.ll_influence,
            R.id.ll_selectFile1,
            R.id.ll_selectFile2,
            R.id.ll_selectFile3,
            R.id.ll_level,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_level:
                dialog.setData(highZoneLevelList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvLevel.setText(highZoneLevelList.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_selectFile1:
                if ("check".equals(tag)) {
                    if (!uploadPath1.equals("00")) {
                        Uri uri = Uri.parse(uploadPath1);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                } else {
                    Intent intentSelectFile1 = new Intent(PipeHighZoneActivity.this, SelectFileActivity.class);
                    startActivityForResult(intentSelectFile1, FILE_REQUEST_CODE1);
                }

                break;
            case R.id.ll_selectFile2:
                if ("check".equals(tag)) {
                    if (!uploadPath2.equals("00")) {
                        Uri uri = Uri.parse(uploadPath2);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                } else {
                    Intent intentSelectFile2 = new Intent(PipeHighZoneActivity.this, SelectFileActivity.class);
                    startActivityForResult(intentSelectFile2, FILE_REQUEST_CODE2);
                }

                break;
            case R.id.ll_selectFile3:
                if ("check".equals(tag)) {
                    if (!uploadPath3.equals("00")) {
                        Uri uri = Uri.parse(uploadPath3);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                } else {
                    Intent intentSelectFile3 = new Intent(PipeHighZoneActivity.this, SelectFileActivity.class);
                    startActivityForResult(intentSelectFile3, FILE_REQUEST_CODE3);
                }
                break;
            case R.id.ll_influence:
                dialog.setData(influenceList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvInfluence.setText(influenceList.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_death:
                dialog.setData(deathList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvDeath.setText(deathList.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_riskLevel:
                dialog.setData(riskLevelList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvRiskLevel.setText(riskLevelList.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_relativeAreaLevel:
                dialog.setData(relativeAreaLevelList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvRelativeAreaLevel.setText(relativeAreaLevelList.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_locationLevel:
                dialog.setData(locationLevelList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvLocationLevel.setText(locationLevelList.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_recognise:
                dialog.setData(recogniseList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvRecognise.setVisibility(View.VISIBLE);
                    tvRecognise.setText(recogniseList.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_type:
                dialog.setData(typeList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvType.setText(typeList.get(positionM));
                    dialog.dismiss();
                });
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
                    pipeId = pipeIdList.get(positionM);
                    dialog.dismiss();
                });
                break;
            case R.id.ll_search:
                Intent intent = new Intent(PipeHighZoneActivity.this, SearchHighZoneActivity.class);
                startActivityForResult(intent, SEARCH_HIGHZONE);
                break;
            case R.id.btn_commit:
                if (paramsComplete()) {
                    updateHighZone();
                }
                break;
        }
    }

    private void updateHighZone() {
        JsonObject params = new JsonObject();
        params.addProperty("id", Integer.parseInt(highZoneId));
        params.addProperty("pipeid", Integer.valueOf(pipeId));
        params.addProperty("name", etHighZoneName.getText().toString());
        params.addProperty("locationdesc", etLocation.getText().toString());
        params.addProperty("length", Double.parseDouble(etLength.getText().toString()));
        params.addProperty("level", tvLevel.getText().toString());
        params.addProperty("htype", tvType.getText().toString());
        params.addProperty("desc", etDesc.getText().toString());
        params.addProperty("controlmeasures", etControlMethod.getText().toString());
        params.addProperty("identifiers", tvRecognise.getText().toString());
        params.addProperty("areaslevel", tvLocationLevel.getText().toString());
        params.addProperty("risklevel", tvRiskLevel.getText().toString());
        params.addProperty("pipethickness", Double.parseDouble(etPly.getText().toString()));
        params.addProperty("designlevel", tvRelativeAreaLevel.getText().toString());
        params.addProperty("areaslevelflag", isAdd);
        params.addProperty("arearadius", tvDeath.getText().toString());
        params.addProperty("fluentionareas", tvInfluence.getText().toString());
        if (!TextUtils.isEmpty(ossFilePath1)) {
            params.addProperty("uploadfile1", ossFilePath1);
        } else {
            params.addProperty("uploadfile1", "00");
        }
        if (!TextUtils.isEmpty(ossFilePath2)) {
            params.addProperty("uploadfile2", ossFilePath2);
        } else {
            params.addProperty("uploadfile2", "00");
        }
        if (!TextUtils.isEmpty(ossFilePath3)) {
            params.addProperty("uploadfile3", ossFilePath3);
        } else {
            params.addProperty("uploadfile3", "00");
        }
        Log.i("tag", "params===" + params);
        Net.create(Api.class).updateHighZone(token, params)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            ToastUtil.show("????????????");
                            finish();
                        } else {
                            ToastUtil.show("????????????");
                        }
                    }
                });
    }

    private boolean paramsComplete() {
        if (TextUtils.isEmpty(tvPipeName.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etHighZoneName.getText().toString())) {
            ToastUtil.show("???????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etLocation.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etLength.getText().toString())) {
            ToastUtil.show("???????????????");
            return false;
        }
        if (!NumberUtil.isNumber(etLength.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;

        }
        if (TextUtils.isEmpty(tvLevel.getText().toString())) {
            ToastUtil.show("???????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvType.getText().toString())) {
            ToastUtil.show("???????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etDesc.getText().toString())) {
            ToastUtil.show("???????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etControlMethod.getText().toString())) {
            ToastUtil.show("?????????????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvRecognise.getText().toString())) {
            ToastUtil.show("??????????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvLocationLevel.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvRiskLevel.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etPly.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (!NumberUtil.isNumber(etPly.getText().toString())) {
            ToastUtil.show("???????????????????????????");
            return false;

        }
        if (TextUtils.isEmpty(tvRelativeAreaLevel.getText().toString())) {
            ToastUtil.show("?????????????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvDeath.getText().toString())) {
            ToastUtil.show("???????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvInfluence.getText().toString())) {
            ToastUtil.show("???????????????????????????");
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
            tvLevel.setText(highZoneModel.getLevel());
            tvType.setText(highZoneModel.getHtype());
            etDesc.setText(highZoneModel.getDesc());
            etControlMethod.setText(highZoneModel.getControlmeasures());
            tvRecognise.setText(highZoneModel.getIdentifiers());
            tvLocationLevel.setText(highZoneModel.getAreaslevel());
            tvRiskLevel.setText(highZoneModel.getRisklevel());
            etPly.setText(highZoneModel.getPipethickness() + "");
            tvRelativeAreaLevel.setText(highZoneModel.getDesignlevel());
            if (highZoneModel.getAreaslevelflag().equals("???")) {
                rbYes.setChecked(true);
            } else {
                rbNo.setChecked(true);
            }
            tvDeath.setText(highZoneModel.getArearadius());
            tvInfluence.setText(highZoneModel.getFluentionareas());
        } else if (requestCode == FILE_REQUEST_CODE1) {
            selectFileName1 = data.getStringExtra("fileName");
            selectFilePath1 = data.getStringExtra("selectFilePath");
            tvFileName1.setText(selectFileName1);
            mWeiboDialog = WeiboDialogUtils.createLoadingDialog(this, "?????????...");
            mWeiboDialog.getWindow().setDimAmount(0f);
//            uploadOffice(userId + "_" + TimeUtil.getFileNameTime() + "_" + selectFileName1, selectFilePath1, 1);
            uploadOffice("highaccount/"+selectFileName1, selectFilePath1, 1);
        } else if (requestCode == FILE_REQUEST_CODE2) {
            selectFileName2 = data.getStringExtra("fileName");
            selectFilePath2 = data.getStringExtra("selectFilePath");
            tvFileName2.setText(selectFileName2);
            mWeiboDialog = WeiboDialogUtils.createLoadingDialog(this, "?????????...");
            mWeiboDialog.getWindow().setDimAmount(0f);
//            uploadOffice(userId + "_" + TimeUtil.getFileNameTime() + "_" + selectFileName2, selectFilePath2, 2);
            uploadOffice("highaccount/"+selectFileName2, selectFilePath2, 2);
        } else if (requestCode == FILE_REQUEST_CODE3) {
            selectFileName3 = data.getStringExtra("fileName");
            selectFilePath3 = data.getStringExtra("selectFilePath");
            tvFileName3.setText(selectFileName3);
            mWeiboDialog = WeiboDialogUtils.createLoadingDialog(this, "?????????...");
            mWeiboDialog.getWindow().setDimAmount(0f);
//            uploadOffice(userId + "_" + TimeUtil.getFileNameTime() + "_" + selectFileName3, selectFilePath3, 3);
            uploadOffice("highaccount/"+selectFileName3, selectFilePath3, 3);
        }

    }

    public void uploadOffice(String fileName, String filePath, int index) {
        PutObjectRequest put = new PutObjectRequest(Constant.BUCKETSTRING, fileName, filePath);
        // ??????????????????????????????
        OSSAsyncTask ossAsyncTask = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                if (index == 1) {
                    ossFilePath1 = fileName;
                } else if (index == 2) {
                    ossFilePath2 = fileName;
                } else {
                    ossFilePath3 = fileName;
                }
                WeiboDialogUtils.closeDialog(mWeiboDialog);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                ToastUtil.show("?????????????????????");
                // ???????????????
                if (clientException != null) {
                    // ????????????????????????????????????
                }
                if (serviceException != null) {


                }
            }
        });
    }
}
