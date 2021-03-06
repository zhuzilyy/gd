package com.gd.form.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gd.form.R;
import com.gd.form.adapter.PhotoAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.BuildingModel;
import com.gd.form.model.GlideImageLoader;
import com.gd.form.model.Pipelineinfo;
import com.gd.form.model.ServerModel;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.NumberUtil;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.TimeUtil;
import com.gd.form.utils.ToastUtil;
import com.gd.form.utils.WeiboDialogUtils;
import com.gd.form.view.ListDialog;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    @BindView(R.id.tv_pipeProperty)
    TextView tvPipeProperty;
    @BindView(R.id.tv_type)
    TextView tvType;
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
    @BindView(R.id.tv_riskEvaluate)
    TextView tvRiskEvaluate;
    @BindView(R.id.tv_riskType)
    TextView tvRiskType;
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
    @BindView(R.id.ll_pipeProperty)
    LinearLayout llPipeProperty;
    @BindView(R.id.ll_type)
    LinearLayout llType;
    @BindView(R.id.ll_riskEvaluate)
    LinearLayout llRiskEvaluate;
    @BindView(R.id.tv_riskResult)
    TextView tvRiskResult;
    @BindView(R.id.ll_riskResult)
    LinearLayout llRiskResult;
    @BindView(R.id.ll_riskType)
    LinearLayout llRiskType;
    @BindView(R.id.rvResultPhoto)
    RecyclerView rvResultPhoto;
    @BindView(R.id.rl_selectImage)
    RelativeLayout rlSelectImage;
    @BindView(R.id.ll_location)
    LinearLayout llLocation;
    @BindView(R.id.tv_totalLevel)
    TextView tvTotalLevel;
    @BindView(R.id.flow_layout)
    TagFlowLayout mFlowLayout;
    private String isHighZone = "???";
    private TimePickerView pvTime;
    private String token, userId, buildingId;
    private final int SEARCH_BUILDING = 100;
    private int SELECT_AREA = 104;
    private int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private List<Pipelineinfo> pipelineInfoList;
    private ListDialog dialog;
    private List<String> pipePropertyList, typeList, riskEvaluateList, riskResultList, riskTypeList;
    private List<String> path;
    private GalleryConfig galleryConfig;
    private PhotoAdapter photoAdapter;
    private List<String> nameList;
    private IHandlerCallBack iHandlerCallBack;
    private Dialog mWeiboDialog;
    private OSSCredentialProvider ossCredentialProvider;
    private OSS oss;
    private int stationId, pipeId;
    private double rateLevel, resultLevel;
    private TagAdapter<String> mAdapter;
    private String[] dataSource = {"??????", "??????", "?????????", "??????", "U?????????", "?????????", "?????????"};
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
        token = (String) SPUtil.get(PipeBuildingActivity.this, "token", "");
        userId = (String) SPUtil.get(PipeBuildingActivity.this, "userId", "");
        ossCredentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.ACCESSKEYID, Constant.ACCESSKEYSECRET);
        oss = new OSSClient(mContext.getApplicationContext(), Constant.ENDPOINT, ossCredentialProvider);
        tvTitle.setText("????????????");
        dialog = new ListDialog(this);
        pipePropertyList = new ArrayList<>();
        typeList = new ArrayList<>();
        riskEvaluateList = new ArrayList<>();
        riskResultList = new ArrayList<>();
        riskTypeList = new ArrayList<>();
        path = new ArrayList<>();
        nameList = new ArrayList<>();
        if (getIntent() != null) {
            String tag = getIntent().getExtras().getString("tag");
            buildingId = getIntent().getExtras().getString("buildingId");
            if (!TextUtils.isEmpty(buildingId)) {
                getBuildingData(buildingId);
            }
            if ("add".equals(tag)) {
                btnCommit.setVisibility(View.VISIBLE);
                llSearch.setVisibility(View.GONE);
                llPipeName.setEnabled(true);
                etStartStationNo.setEnabled(true);
                etLocation.setEnabled(true);
                tvPipeProperty.setEnabled(true);
                tvType.setEnabled(true);
                etName.setEnabled(true);
                tvTime.setEnabled(true);
                rgIsHighZone.setEnabled(true);
                etMissLength.setEnabled(true);
                etMinDistance.setEnabled(true);
                etMissArea.setEnabled(true);
                etPersonActivity.setEnabled(true);
                etDes.setEnabled(true);
                tvRiskEvaluate.setEnabled(true);
                tvRiskType.setEnabled(true);
                etBeforeChangeMethod.setEnabled(true);
                etChangeMethod.setEnabled(true);
                llTime.setEnabled(true);
                llPipeName.setEnabled(true);
                rbNo.setEnabled(true);
                rbYes.setEnabled(true);
                llPipeProperty.setEnabled(true);
                llType.setEnabled(true);
                llRiskEvaluate.setEnabled(true);
                llRiskResult.setEnabled(true);
                llRiskType.setEnabled(true);
                rlSelectImage.setEnabled(true);
            } else if (("check".equals(tag))) {
                btnCommit.setVisibility(View.GONE);
                llSearch.setVisibility(View.GONE);
                llPipeName.setEnabled(false);
                etStartStationNo.setEnabled(false);
                etLocation.setEnabled(false);
                tvPipeProperty.setEnabled(false);
                tvType.setEnabled(false);
                etName.setEnabled(false);
                tvTime.setEnabled(false);
                rgIsHighZone.setEnabled(false);
                etMissLength.setEnabled(false);
                etMinDistance.setEnabled(false);
                etMissArea.setEnabled(false);
                etPersonActivity.setEnabled(false);
                etDes.setEnabled(false);
                tvRiskEvaluate.setEnabled(false);
                tvRiskType.setEnabled(false);
                etBeforeChangeMethod.setEnabled(false);
                etChangeMethod.setEnabled(false);
                llPipeName.setEnabled(false);
                rbNo.setEnabled(false);
                rbYes.setEnabled(false);
                llTime.setEnabled(false);
                llPipeProperty.setEnabled(false);
                llType.setEnabled(false);
                llRiskEvaluate.setEnabled(false);
                llRiskResult.setEnabled(false);
                llRiskType.setEnabled(false);
                rlSelectImage.setEnabled(false);
                llLocation.setEnabled(false);
            }
        }
        initListener();
        initTimePicker();
        getPipelineInfoListRequest();
        initDialog();
        initGallery();
        initConfig();
    }

    private void initGallery() {
        iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(List<String> photoList) {
                path.clear();
                nameList.clear();
                for (String s : photoList) {
                    path.add(s);
                }
                photoAdapter.notifyDataSetChanged();
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(PipeBuildingActivity.this, "?????????...");
                mWeiboDialog.getWindow().setDimAmount(0f);
                for (int i = 0; i < path.size(); i++) {
                    String suffix = path.get(i).substring(path.get(i).length() - 4);
//                    uploadFiles(userId + "_" + TimeUtil.getFileNameTime() + "_" + i + suffix, path.get(i));
                    uploadFiles("llegalaccount/" + userId + "_" + TimeUtil.getFileNameTime() + "_" + i + suffix, path.get(i));
                }

            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onError() {

            }
        };

    }

    private void initConfig() {
        galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())    // ImageLoader ????????????????????????
                .iHandlerCallBack(iHandlerCallBack)     // ????????????????????????
                .provider("com.gd.form.fileprovider")   // provider(??????)
                .pathList(path)                         // ?????????????????????
                .multiSelect(true)                      // ????????????   ?????????false
                .multiSelect(true, 9)                   // ??????????????????????????? ??????????????????   ?????????false ??? 9
                .maxSize(9)                             // ??????????????? ??????????????????    ?????????9
                .crop(false)                             // ??????????????????????????????????????? ??????????????????????????????
                .crop(false, 1, 1, 500, 500)             // ??????????????????????????????   ?????????????????? 1:1
                .isShowCamera(true)                     // ????????????????????????  ?????????false
                .filePath("/Gallery/Pictures")// ??????????????????
                .build();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvResultPhoto.setLayoutManager(gridLayoutManager);
        photoAdapter = new PhotoAdapter(this, path);
        rvResultPhoto.setAdapter(photoAdapter);
    }

    private void initDialog() {
        pipePropertyList.add("????????????");
        pipePropertyList.add("????????????");
        typeList.add("????????????????????????");
        typeList.add("????????????????????????");
        typeList.add("??????????????????????????????????????????????????????");
        typeList.add("??????");
        typeList.add("??????");
        typeList.add("??????");
        typeList.add("????????????");
        typeList.add("????????????");
        riskEvaluateList.add("1");
        riskEvaluateList.add("2");
        riskEvaluateList.add("3");
        riskEvaluateList.add("4");
        riskEvaluateList.add("5");
        riskResultList.add("I");
        riskResultList.add("II");
        riskResultList.add("III");
        riskResultList.add("IV");
        riskResultList.add("V");
        riskTypeList.add("??????");
        riskTypeList.add("??????");
        riskTypeList.add("??????");
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

    private void initTimePicker() {
        //Dialog ???????????????????????????
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                tvTime.setText(format.format(date));
            }
        }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
            @Override
            public void onTimeSelectChanged(Date date) {
                Log.i("pvTime", "onTimeSelectChanged");
            }
        }).setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true) //????????????false ??????????????????DecorView ????????????????????????
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("pvTime", "onCancelClickListener");
                    }
                })
                .setItemVisibleCount(5) //?????????????????????????????????1???????????????6???????????????????????????7???
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
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//??????????????????
                dialogWindow.setGravity(Gravity.BOTTOM);//??????Bottom,????????????
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
                        isHighZone = "???";
                        break;
                    case R.id.rb_no:
                        isHighZone = "???";
                        break;
                }
            }
        });

        final LayoutInflater mInflater = LayoutInflater.from(this);
        mFlowLayout.setAdapter(mAdapter = new com.zhy.view.flowlayout.TagAdapter<String>(dataSource) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.background_tag,
                        mFlowLayout, false);
                tv.setText(s);
                return tv;
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
//                                etStartStationNo.setText(descArr[1]);
                            }
                            etLocation.setText(buildingModel.getLocationdesc());
                            tvPipeProperty.setText(buildingModel.getOverpropety());
                            tvType.setText(buildingModel.getOvertype());
                            etName.setText(buildingModel.getOvername());
                            tvTime.setText(buildingModel.getGenernaldate());
                            if (buildingModel.getHighareasflag().equals("???")) {
                                rbYes.setChecked(true);
                            } else {
                                rbNo.setChecked(true);
                            }
                            etMissLength.setText(buildingModel.getShortareas() + "");
                            etMinDistance.setText(buildingModel.getMinspacing() + "");
                            etMissArea.setText(buildingModel.getShortareas() + "");
                            etPersonActivity.setText(buildingModel.getPeractives());
                            etDes.setText(buildingModel.getDangerdesc());
                            tvRiskEvaluate.setText(buildingModel.getRiskevaluation());
                            tvRiskType.setText(buildingModel.getDangertype());
                            etBeforeChangeMethod.setText(buildingModel.getPresolution().trim());
                            etChangeMethod.setText(buildingModel.getAftsolution());
                            stationId = buildingModel.getStakeid();
                            pipeId = buildingModel.getPipeid();
                            tvRiskEvaluate.setText(buildingModel.getRiskevaluation1());
                            tvRiskResult.setText(buildingModel.getRiskevaluation2());
                            tvTotalLevel.setText(buildingModel.getRiskevaluation3());
                            if (!TextUtils.isEmpty(buildingModel.getUploadpicture())) {
                                if (buildingModel.getUploadpicture().contains(";")) {
                                    String[] pathArr = buildingModel.getUploadpicture().split(";");
                                    for (int i = 0; i < pathArr.length; i++) {
                                        path.add(pathArr[i]);
                                        photoAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    path.add(buildingModel.getUploadpicture());
                                    photoAdapter.notifyDataSetChanged();
                                }
                            }
                            if (!TextUtils.isEmpty((buildingModel.getRiskevaluation1())) &&
                                    NumberUtil.isNumber(buildingModel.getRiskevaluation1())) {
                                rateLevel = Double.parseDouble(buildingModel.getRiskevaluation1());
                            }
                            switch (buildingModel.getRiskevaluation2()) {
                                case "I":
                                    resultLevel = 1;
                                    break;
                                case "II":
                                    resultLevel = 2;
                                    break;
                                case "III":
                                    resultLevel = 3;
                                    break;
                                case "IV":
                                    resultLevel = 4;
                                    break;
                                case "V":
                                    resultLevel = 5;
                                    break;
                            }
                        }
                        if (rateLevel != 0 && resultLevel != 0) {
                            tvTotalLevel.setText(rateLevel * resultLevel + "");
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
            R.id.ll_location,
            R.id.ll_pipeProperty,
            R.id.ll_type,
            R.id.ll_riskEvaluate,
            R.id.ll_riskResult,
            R.id.ll_riskType,
            R.id.rl_selectImage,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_selectImage:
                initPermissions();
                break;
            case R.id.ll_riskType:
                dialog.setData(riskTypeList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvRiskType.setText(riskTypeList.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_riskResult:
                dialog.setData(riskResultList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvRiskResult.setText(riskResultList.get(positionM));
                    resultLevel = positionM + 1;
                    if (resultLevel != 0 && rateLevel != 0) {
                        tvTotalLevel.setText(resultLevel * rateLevel + "");
                    }
                    dialog.dismiss();
                });
                break;
            case R.id.ll_riskEvaluate:
                dialog.setData(riskEvaluateList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvRiskEvaluate.setText(riskEvaluateList.get(positionM));
                    rateLevel = positionM + 1;
                    if (resultLevel != 0 && rateLevel != 0) {
                        tvTotalLevel.setText(resultLevel * rateLevel + "");
                    }
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
            case R.id.ll_pipeProperty:
                dialog.setData(pipePropertyList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvPipeProperty.setText(pipePropertyList.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_location:
                Intent intentArea = new Intent(this, MapActivity.class);
                startActivityForResult(intentArea, SELECT_AREA);
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
            case R.id.btn_commit:
                if (paramsComplete()) {
                    updateBuilding();
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

    private void updateBuilding() {
        StringBuilder photoSb = new StringBuilder();
        if (nameList.size() > 0) {
            for (int i = 0; i < nameList.size(); i++) {
                if (i != nameList.size() - 1) {
                    photoSb.append(nameList.get(i) + ";");
                } else {
                    photoSb.append(nameList.get(i));
                }
            }
        }
        JsonObject params = new JsonObject();
        params.addProperty("id", Integer.parseInt(buildingId));
        params.addProperty("stakeid", stationId);
        params.addProperty("pipeid", pipeId);
        params.addProperty("locationdesc", etLocation.getText().toString());
        params.addProperty("overpropety", tvPipeProperty.getText().toString());
        params.addProperty("overtype", tvType.getText().toString());
        params.addProperty("overname", etName.getText().toString());
        params.addProperty("genernaldate", tvTime.getText().toString());
        params.addProperty("highareasflag", isHighZone);
        params.addProperty("shortlength", Double.parseDouble(etMissLength.getText().toString()));
        params.addProperty("minspacing", Double.parseDouble(etMinDistance.getText().toString()));
        params.addProperty("shortareas", Double.parseDouble(etMissArea.getText().toString()));
        params.addProperty("peractives", etPersonActivity.getText().toString());
        params.addProperty("dangerdesc", etDes.getText().toString());
        params.addProperty("riskevaluation1", tvRiskEvaluate.getText().toString());
        params.addProperty("riskevaluation2", tvRiskResult.getText().toString());
        params.addProperty("riskevaluation3", tvTotalLevel.getText().toString());
        params.addProperty("dangertype", tvRiskType.getText().toString());
        params.addProperty("presolution", etBeforeChangeMethod.getText().toString());
        params.addProperty("aftsolution", etChangeMethod.getText().toString());

        if (!TextUtils.isEmpty(photoSb.toString())) {
            params.addProperty("uploadpicture", photoSb.toString());
        } else {
            params.addProperty("uploadpicture", "00");
        }
        Net.create(Api.class).updateBuilding(token, params)
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
//        if (TextUtils.isEmpty(tvPipeName.getText().toString())) {
//            ToastUtil.show("?????????????????????");
//            return false;
//        }
//        if (TextUtils.isEmpty(etStartStationNo.getText().toString())) {
//            ToastUtil.show("?????????????????????");
//            return false;
//        }
        if (TextUtils.isEmpty(etLocation.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvPipeProperty.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvType.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etName.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvTime.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etMissLength.getText().toString())) {
            ToastUtil.show("??????????????????????????????????????????(m)");
            return false;
        }
        if (!NumberUtil.isNumber(etMissLength.getText().toString())) {
            ToastUtil.show("????????????????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etMinDistance.getText().toString())) {
            ToastUtil.show("??????????????????????????????(m)");
            return false;
        }
        if (!NumberUtil.isNumber(etMinDistance.getText().toString())) {
            ToastUtil.show("????????????????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etMissArea.getText().toString())) {
            ToastUtil.show("??????????????????????????????????????????(???)");
            return false;
        }
        if (!NumberUtil.isNumber(etMissArea.getText().toString())) {
            ToastUtil.show("????????????????????????????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etPersonActivity.getText().toString())) {
            ToastUtil.show("???????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etDes.getText().toString())) {
            ToastUtil.show("???????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvRiskEvaluate.getText().toString())) {
            ToastUtil.show("????????????-????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvRiskResult.getText().toString())) {
            ToastUtil.show("????????????-????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvRiskType.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etBeforeChangeMethod.getText().toString())) {
            ToastUtil.show("??????????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etChangeMethod.getText().toString())) {
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
        if (requestCode == SEARCH_BUILDING) {
            BuildingModel buildingModel = (BuildingModel) data.getSerializableExtra("building");
            String pipeDesc = buildingModel.getPipedesc();
            if (!TextUtils.isEmpty(pipeDesc)) {
                String[] descArr = pipeDesc.split(":");
                tvPipeName.setText(descArr[0]);
                etStartStationNo.setText(descArr[1]);
            }
            etLocation.setText(buildingModel.getLocationdesc());
            tvPipeProperty.setText(buildingModel.getOverpropety());
            tvType.setText(buildingModel.getOvertype());
            etName.setText(buildingModel.getOvername());
            tvTime.setText(buildingModel.getGenernaldate());
            if (buildingModel.getHighareasflag().equals("???")) {
                rbYes.setChecked(true);
            } else {
                rbNo.setChecked(true);
            }
            etMissLength.setText(buildingModel.getShortareas() + "");
            etMinDistance.setText(buildingModel.getMinspacing() + "");
            etMissArea.setText(buildingModel.getShortareas() + "");
            etPersonActivity.setText(buildingModel.getPeractives());
            etDes.setText(buildingModel.getDangerdesc());
            tvRiskEvaluate.setText(buildingModel.getRiskevaluation());
            tvRiskType.setText(buildingModel.getDangertype());
            etBeforeChangeMethod.setText(buildingModel.getPresolution());
            etChangeMethod.setText(buildingModel.getAftsolution());
        } else if (requestCode == SELECT_AREA) {
            String area = data.getStringExtra("area");
            etLocation.setText(area);
        }
    }

    // ????????????
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(mContext, "?????? ??????-???????????? ????????????????????????????????????", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(this);
            } else {
                Log.i("tag", "????????????");
            }
        }
    }

    //?????????????????????
    public void uploadFiles(String fileName, String filePath) {
        PutObjectRequest put = new PutObjectRequest(Constant.BUCKETSTRING, fileName, filePath);
        // ??????????????????????????????
        OSSAsyncTask ossAsyncTask = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                nameList.add(fileName);
                if (nameList.size() == path.size()) {
                    WeiboDialogUtils.closeDialog(mWeiboDialog);
                }
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
