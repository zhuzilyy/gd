package com.gd.form.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.gd.form.R;
import com.gd.form.adapter.PhotoAdapter;
import com.gd.form.base.BaseActivity;
import com.gd.form.constants.Constant;
import com.gd.form.model.GlideImageLoader;
import com.gd.form.model.NextStationModel;
import com.gd.form.model.Pipelineinfo;
import com.gd.form.model.Pipemploys;
import com.gd.form.model.SearchArea;
import com.gd.form.model.ServerModel;
import com.gd.form.model.StationDetailInfo;
import com.gd.form.net.Api;
import com.gd.form.net.Net;
import com.gd.form.net.NetCallback;
import com.gd.form.utils.NumberUtil;
import com.gd.form.utils.SPUtil;
import com.gd.form.utils.TimeUtil;
import com.gd.form.utils.ToastUtil;
import com.gd.form.utils.WeiboDialogUtils;
import com.gd.form.view.ListDialog;
import com.gd.form.view.ListLandTagDialog;
import com.google.gson.JsonObject;
import com.jaeger.library.StatusBarUtil;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PipeTagActivity extends BaseActivity implements AMapLocationListener {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.ll_area)
    LinearLayout llArea;
    @BindView(R.id.ll_pipeName)
    LinearLayout llPipeName;
    @BindView(R.id.ll_groundTagType)
    LinearLayout llGroundTagType;
    @BindView(R.id.ll_landForm)
    LinearLayout llLandForm;
    @BindView(R.id.ll_location)
    LinearLayout llLocation;
    @BindView(R.id.et_kgInfo)
    EditText etKgInfo;
    @BindView(R.id.et_corner)
    EditText etCorner;
    @BindView(R.id.et_longitude)
    EditText etLongitude;
    @BindView(R.id.et_latitude)
    EditText etLatitude;
    @BindView(R.id.et_depth)
    EditText etDepth;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.tv_pipeName)
    TextView tvPipeName;
    @BindView(R.id.tv_groundTagType)
    TextView tvGroundTagType;
    @BindView(R.id.tv_landForm)
    TextView tvLandForm;
    @BindView(R.id.tv_upStationNo)
    TextView tvUpStationNo;
    @BindView(R.id.tv_downStationNo)
    TextView tvDownStationNo;
    @BindView(R.id.tv_stationNoPrefix)
    TextView tvStationNoPrefix;
    @BindView(R.id.et_location)
    EditText etLocation;
    @BindView(R.id.et_stationNameNo)
    EditText etStationNameNo;
    @BindView(R.id.tv_pipeManager)
    TextView tvPipeManager;
    @BindView(R.id.ll_upStationNo)
    LinearLayout llUpStationNo;
    @BindView(R.id.ll_downStationNo)
    LinearLayout llDownStationNo;
    @BindView(R.id.ll_upStationKm)
    LinearLayout llUpStationKm;
    @BindView(R.id.ll_pipeManager)
    LinearLayout llPipeManager;
    @BindView(R.id.ll_downStationKm)
    LinearLayout llDownStationKm;
    @BindView(R.id.ll_stationNoPrefix)
    LinearLayout llStationNoPrefix;
    @BindView(R.id.tv_upStationKm)
    TextView tvUpStationKm;
    @BindView(R.id.tv_downStationKm)
    TextView tvDownStationKm;

    @BindView(R.id.view_upStationNo)
    View viewUpStationNo;
    @BindView(R.id.view_upStationKm)
    View viewUpStationKm;
    @BindView(R.id.view_downStationNo)
    View viewDownStationNo;
    @BindView(R.id.view_downStationKm)
    View viewDownStationKm;
    @BindView(R.id.view_stationNoPrefix)
    View viewStationNoPrefix;
    @BindView(R.id.rvResultPhoto)
    RecyclerView rvResultPhoto;
    @BindView(R.id.rg_isCheckGood)
    RadioGroup rgIsCheckGood;
    @BindView(R.id.rg_isOnTop)
    RadioGroup rgIsOnTop;
    @BindView(R.id.rg_isComplete)
    RadioGroup rgIsComplete;
    @BindView(R.id.tv_material)
    TextView tvMaterial;
    private int departmentId, pipeId;
    private ListLandTagDialog landTypeDialog;
    private ListDialog dialog;
    private String token, userId;
    private String stationId, highZoneId, tunnelId, prefixName;
    private String tag;
    private List<SearchArea> departmentList;
    private List<Pipelineinfo> pipelineInfoList;
    private int SELECT_STATION = 101;
    private int SELECT_APPROVER = 102;
    private int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private String upStationId, approverName, approverId, id, lineId, highZoneName = "", pipeName = "";
    public AMapLocationClient mlocationClient;
    //??????mLocationOption??????
    public AMapLocationClientOption mLocationOption = null;
    private AMapLocation currentAmapLocation;
    private StationDetailInfo stationDetailInfo;
    private IHandlerCallBack iHandlerCallBack;
    private List<String> path;
    private GalleryConfig galleryConfig;
    private PhotoAdapter photoAdapter;
    private List<String> nameList;
    /**
     * ?????????????????????????????????
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };
    //???????????????target > 28????????????????????????????????????????????????"????????????"???????????????
    private static String BACKGROUND_LOCATION_PERMISSION = "android.permission.ACCESS_BACKGROUND_LOCATION";
    /**
     * ????????????????????????????????????????????????
     */
    private boolean isNeedCheck = true;
    private static final int PERMISSON_REQUESTCODE = 0;
    //????????????????????????????????????????????????true???????????????????????????????????????????????????????????????
    private boolean needCheckBackLocation = false;
    private boolean isLoactionSuccess;
    private Dialog mWeiboDialog;
    private OSS oss;
    private OSSCredentialProvider ossCredentialProvider;
    private String checkStatus = "??????";
    private String isOnTop = "???";
    private String isComplete = "???";
    private List<String> landMaterialList;
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ContextCompat.getColor(mContext, R.color.colorFF52A7F9));
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.activity_pipe_tag;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvTitle.setText("????????????");
        tvRight.setVisibility(View.VISIBLE);
        token = (String) SPUtil.get(this, "token", "");
        userId = (String) SPUtil.get(this, "userId", "");
        ossCredentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.ACCESSKEYID, Constant.ACCESSKEYSECRET);
        oss = new OSSClient(mContext.getApplicationContext(), Constant.ENDPOINT, ossCredentialProvider);
        landTypeDialog = new ListLandTagDialog(this);
        departmentList = new ArrayList<>();
        pipelineInfoList = new ArrayList<>();
        path = new ArrayList<>();
        dialog = new ListDialog(this);
        landMaterialList = new ArrayList<>();
        landMaterialList.add("??????");
        landMaterialList.add("????????????");
        landMaterialList.add("??????");
        if (getIntent() != null) {
            tag = getIntent().getStringExtra("tag");
            //??????
            if (tag.equals("update")) {
                tvRight.setText("???????????????");
                id = getIntent().getStringExtra("id");
                lineId = getIntent().getStringExtra("lineId");
                getDetailInfo(id, lineId);
                tvRight.setVisibility(View.VISIBLE);
                tvTitle.setText("????????????");
                llUpStationNo.setVisibility(View.GONE);
                viewUpStationNo.setVisibility(View.GONE);
                llUpStationKm.setVisibility(View.GONE);
                viewUpStationKm.setVisibility(View.GONE);
                llDownStationNo.setVisibility(View.GONE);
                viewDownStationNo.setVisibility(View.GONE);
                llDownStationKm.setVisibility(View.GONE);
                viewDownStationKm.setVisibility(View.GONE);
                llStationNoPrefix.setVisibility(View.GONE);
                viewStationNoPrefix.setVisibility(View.GONE);
//                llArea.setEnabled(false);
//                llPipeName.setEnabled(false);
//                llGroundTagType.setEnabled(false);
//                llUpStationNo.setEnabled(false);
//                llDownStationNo.setEnabled(false);
//                etStationNameNo.setEnabled(false);
//                etKgInfo.setEnabled(false);
//                llPipeManager.setEnabled(false);
//                etCorner.setEnabled(false);
//                etLongitude.setEnabled(false);
//                etLatitude.setEnabled(false);
//                llLandForm.setEnabled(false);
//                etLocation.setEnabled(false);
//                etDepth.setEnabled(false);
//                etName.setEnabled(false);
//                etPhone.setEnabled(false);
//                etRemark.setEnabled(false);
//                llLocation.setEnabled(false);
            } else if (tag.equals("add")) {
                tvRight.setVisibility(View.GONE);
                tvRight.setText("???????????????");
//                llArea.setEnabled(true);
//                llPipeName.setEnabled(true);
//                llGroundTagType.setEnabled(true);
//                llUpStationNo.setEnabled(true);
//                llDownStationNo.setEnabled(true);
//                etStationNameNo.setEnabled(true);
//                etKgInfo.setEnabled(true);
//                llPipeManager.setEnabled(true);
//                etCorner.setEnabled(true);
//                etLongitude.setEnabled(true);
//                etLatitude.setEnabled(true);
//                llLandForm.setEnabled(true);
//                etLocation.setEnabled(true);
//                etDepth.setEnabled(true);
//                etName.setEnabled(true);
//                etPhone.setEnabled(true);
//                etRemark.setEnabled(true);
//                llLocation.setEnabled(true);
            }
        }
        getLocation();
        pipeDepartmentInfoGetList();
        initGallery();
        initConfig();
        initListener();
    }

    private void initListener() {
        rgIsCheckGood.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesCheck:
                        checkStatus = "??????";
                        break;
                    case R.id.rb_noCheck:
                        checkStatus = "?????????";
                        break;
                }
            }
        });

        rgIsOnTop.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yes:
                        isOnTop = "???";
                        break;
                    case R.id.rb_noCheck:
                        isOnTop = "???";
                        break;
                }
            }
        });
        rgIsComplete.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yesComplete:
                        isComplete = "???";
                        break;
                    case R.id.rb_noComplete:
                        isComplete = "???";
                        break;
                }
            }
        });
    }

    //????????????
    private void getDetailInfo(String id, String lineId) {
        JsonObject params = new JsonObject();
        params.addProperty("id", Integer.parseInt(id));
        params.addProperty("pipeid", Integer.parseInt(lineId));
        Net.create(Api.class).getStationDetailInfo(token, params)
                .enqueue(new NetCallback<List<StationDetailInfo>>(this, true) {
                    @Override
                    public void onResponse(List<StationDetailInfo> list) {
                        if (list != null && list.size() > 0) {
                            stationDetailInfo = list.get(0);
                            if (!TextUtils.isEmpty(stationDetailInfo.getDesc())) {
                                if (stationDetailInfo.getDesc().contains(":")) {
                                    tvArea.setText(stationDetailInfo.getDesc().split(":")[0]);
                                    tvPipeName.setText(stationDetailInfo.getDesc().split(":")[1]);
                                }
                            }
                            tvGroundTagType.setText(stationDetailInfo.getStaketype());
                            tvUpStationNo.setText(stationDetailInfo.getName());
                            if (!TextUtils.isEmpty(stationDetailInfo.getCornerinfo())) {
                                etCorner.setText(stationDetailInfo.getCornerinfo());
                            }
                            if (tvGroundTagType.getText().equals("?????????")) {
                                etCorner.setEnabled(true);
                            } else {
                                etCorner.setEnabled(false);
                            }
                            etLongitude.setText(stationDetailInfo.getEastlongitude());
                            etLatitude.setText(stationDetailInfo.getNorthlatitude());
                            tvLandForm.setText(stationDetailInfo.getTopagraphy());
                            etKgInfo.setText(stationDetailInfo.getMileageinfo());
                            etLocation.setText(stationDetailInfo.getLocationdesc());
                            etName.setText(stationDetailInfo.getLandinfo());
                            etPhone.setText(stationDetailInfo.getLandtel());
                            etRemark.setText(stationDetailInfo.getRemarks());
                            upStationId = stationDetailInfo.getId() + "";
                            etStationNameNo.setText(stationDetailInfo.getName());
                            highZoneName = stationDetailInfo.getHighareasname();
                            pipeName = stationDetailInfo.getPipeaccountname();
                            departmentId = stationDetailInfo.getDepartmentid();
                            pipeId = stationDetailInfo.getPipeid();
                            approverId = stationDetailInfo.getPipeowners();
                            tvPipeManager.setText(stationDetailInfo.getOwnername());
                            if (currentAmapLocation != null) {
                                if (TextUtils.isEmpty(stationDetailInfo.getEastlongitude())) {
                                    etLongitude.setText(currentAmapLocation.getLongitude() + "");

                                }
                                if (TextUtils.isEmpty(stationDetailInfo.getNorthlatitude())) {
                                    etLatitude.setText(currentAmapLocation.getLatitude() + "");
                                }
                                if (TextUtils.isEmpty(stationDetailInfo.getLocationdesc())) {
                                    etLocation.setText(currentAmapLocation.getAddress());
                                }
                            }
                            getStationInfo();

                        }
                    }
                });
    }

    private void initGallery() {
        iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(List<String> photoList) {
                path.clear();
                for (String s : photoList) {
                    path.add(s);
                }
                photoAdapter.notifyDataSetChanged();
                mWeiboDialog = WeiboDialogUtils.createLoadingDialog(PipeTagActivity.this, "?????????...");
                mWeiboDialog.getWindow().setDimAmount(0f);
                for (int i = 0; i < path.size(); i++) {
                    String suffix = path.get(i).substring(path.get(i).length() - 4);
                    uploadFiles("stakes/" + userId + "_" + TimeUtil.getFileNameTime() + "_" + i + suffix, path.get(i));
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

    private void getLocation() {
        mlocationClient = new AMapLocationClient(getApplicationContext());
        //?????????????????????
        mLocationOption = new AMapLocationClientOption();
        //??????????????????
        mlocationClient.setLocationListener(this);
        //???????????????????????????????????????Battery_Saving?????????????????????Device_Sensors??????????????????
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //??????????????????,????????????,?????????2000ms
        mLocationOption.setInterval(2000);
        //??????????????????
        mlocationClient.setLocationOption(mLocationOption);
        // ????????????????????????????????????????????????????????????????????????????????????????????????????????????
        // ??????????????????????????????????????????????????????????????????1000ms?????????????????????????????????stopLocation()???????????????????????????
        // ???????????????????????????????????????????????????onDestroy()??????
        // ?????????????????????????????????????????????????????????????????????stopLocation()???????????????????????????sdk???????????????
        //????????????
        mlocationClient.startLocation();
    }

    private void pipeDepartmentInfoGetList() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("employid", userId);
        Net.create(Api.class).getSearchArea(token, jsonObject)
                .enqueue(new NetCallback<List<SearchArea>>(this, false) {
                    @Override
                    public void onResponse(List<SearchArea> list) {
                        departmentList = list;
                    }
                });
    }

    private void getPipelineInfoListRequest() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("dptid", departmentId);
        Net.create(Api.class).pipelineinfosgetById(token, jsonObject)
                .enqueue(new NetCallback<List<Pipelineinfo>>(this, false) {
                    @Override
                    public void onResponse(List<Pipelineinfo> list) {
                        pipelineInfoList = list;
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
                    }
                });
    }

    @OnClick({
            R.id.iv_back,
            R.id.tv_right,
            R.id.btn_commit,
            R.id.ll_groundTagType,
            R.id.ll_landForm,
            R.id.ll_area,
            R.id.ll_pipeName,
            R.id.ll_upStationNo,
            R.id.ll_pipeManager,
            R.id.rl_selectImage,
            R.id.ll_landMaterial,
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_landMaterial:
                dialog.setData(landMaterialList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvMaterial.setText(landMaterialList.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.rl_selectImage:
                initPermissions();
                break;
            case R.id.ll_pipeManager:
//                if(TextUtils.isEmpty(tvArea.getText().toString())){
//                    ToastUtil.show("?????????????????????");
//                    return;
//                }
//                getPipeManager();
//                Intent intentApprover = new Intent(PipeTagActivity.this, ApproverActivity.class);
//                startActivityForResult(intentApprover, SELECT_APPROVER);
                break;
            case R.id.ll_upStationNo:
                if (TextUtils.isEmpty(tvGroundTagType.getText().toString())) {
                    ToastUtil.show("??????????????????????????????");
                    return;
                }
                if (TextUtils.isEmpty(tvPipeName.getText().toString())) {
                    ToastUtil.show("??????????????????");
                    return;
                }
                Intent intentStartStation = new Intent(this, StationByFullParamsActivity.class);
                intentStartStation.putExtra("tag", "start");
                intentStartStation.putExtra("departmentId", departmentId + "");
                intentStartStation.putExtra("pipeId", pipeId + "");
                startActivityForResult(intentStartStation, SELECT_STATION);
                break;
            case R.id.ll_pipeName:
                if (TextUtils.isEmpty(tvArea.getText().toString())) {
                    ToastUtil.show("?????????????????????");
                    return;
                }
                getPipelineInfoListRequest();
                break;
            case R.id.ll_area:
                List<String> areaList = new ArrayList<>();
                List<Integer> idList = new ArrayList<>();
                if (departmentList != null && departmentList.size() > 0) {
                    for (int i = 0; i < departmentList.size(); i++) {
                        areaList.add(departmentList.get(i).getName());
                        idList.add(departmentList.get(i).getId());
                    }
                }
                dialog.setData(areaList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvArea.setText(areaList.get(positionM));
                    departmentId = idList.get(positionM);
                    dialog.dismiss();
                });
                break;
            case R.id.ll_landForm:
                List<String> landFormList = new ArrayList<>();
                landFormList.add("????????????");
                landFormList.add("????????????");
                landFormList.add("????????????");
                landFormList.add("????????????");
                landFormList.add("????????????");
                landFormList.add("??????");
                landFormList.add("?????????");
                landFormList.add("???????????????");
                landFormList.add("???????????????");
                landFormList.add("???????????????");
                landFormList.add("??????????????????");
                landFormList.add("??????");
                dialog.setData(landFormList);
                dialog.show();
                dialog.setListItemClick(positionM -> {
                    tvLandForm.setText(landFormList.get(positionM));
                    dialog.dismiss();
                });
                break;
            case R.id.ll_groundTagType:
                List<String> typeList = new ArrayList<>();
                typeList.add("?????????H");
                typeList.add("?????????G");
                typeList.add("?????????A");
                typeList.add("?????????D");
                typeList.add("?????????T");
                typeList.add("?????????J");
                typeList.add("?????????Z");
                typeList.add("?????????");
                typeList.add("???????????????P");
                typeList.add("???????????????L");
                typeList.add("???????????????Y");
                typeList.add("?????????");
                typeList.add("?????????");
                typeList.add("?????????");
                typeList.add("?????????C");
                typeList.add("?????????R");
                typeList.add("?????????P");
                landTypeDialog.setData(typeList);
                landTypeDialog.show();
                landTypeDialog.setListItemClick(positionM -> {
                    tvGroundTagType.setText(typeList.get(positionM));
                    if (!TextUtils.isEmpty(tvUpStationNo.getText().toString())) {
                        getStationInfo();
                    }
                    landTypeDialog.dismiss();
                });
                break;
            case R.id.btn_commit:
                if (paramsComplete()) {
                    if ("add".equals(tag)) {
                        addPipeTag();
                    } else if ("update".equals(tag)) {
                        updateTag();
                    }
                }
                break;
            case R.id.tv_right:
                Bundle bundle = new Bundle();
                bundle.putString("stationId", id + "");
                openActivity(PipeMeasureActivity.class, bundle);
                break;

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

    private void getPipeManager() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("dptid", departmentId);
        Net.create(Api.class).getPipeManager(token, jsonObject)
                .enqueue(new NetCallback<List<Pipemploys>>(this, true) {
                    @Override
                    public void onResponse(List<Pipemploys> list) {
                        List<String> nameList = new ArrayList<>();
                        List<String> idList = new ArrayList<>();
                        if (list != null && list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                nameList.add(list.get(i).getName());
                                idList.add(list.get(i).getId());
                            }
                        }
                        dialog.setData(nameList);
                        dialog.show();
                        dialog.setListItemClick(positionM -> {
                            tvPipeManager.setText(nameList.get(positionM));
                            approverId = idList.get(positionM);
                            dialog.dismiss();
                        });
                    }
                });
    }

    private void updateTag() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("departmentid", departmentId);
        jsonObject.addProperty("pipeid", pipeId);
        jsonObject.addProperty("id", Integer.parseInt(id));
        jsonObject.addProperty("name", tvUpStationNo.getText().toString());
        jsonObject.addProperty("desc", tvArea.getText().toString() + ":" + tvPipeName.getText().toString());
        jsonObject.addProperty("staketype", tvGroundTagType.getText().toString());
        jsonObject.addProperty("mileageinfo", etKgInfo.getText().toString());
        jsonObject.addProperty("cornerinfo", etCorner.getText().toString());
        jsonObject.addProperty("eastlongitude", etLongitude.getText().toString());
        jsonObject.addProperty("northlatitude", etLatitude.getText().toString());
        jsonObject.addProperty("topagraphy", tvLandForm.getText().toString());
        jsonObject.addProperty("locationdesc", etLocation.getText().toString());
        jsonObject.addProperty("pipeowners", approverId);
        jsonObject.addProperty("landinfo", etName.getText().toString());
        jsonObject.addProperty("landtel", etPhone.getText().toString());
        jsonObject.addProperty("remarks", etRemark.getText().toString());
        jsonObject.addProperty("highareasid", highZoneId);
        jsonObject.addProperty("pipeaccountid", tunnelId);
        jsonObject.addProperty("highareasname", highZoneName);
        jsonObject.addProperty("pipeaccountname", pipeName);
        Log.i("tag", "jsonObject====" + jsonObject);
        Net.create(Api.class).updateStation(token, jsonObject)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            ToastUtil.show("????????????");
                            Intent intent = new Intent();
                            intent.setAction("com.action.update");
                            sendBroadcast(intent);
                            finish();
                        } else {
                            ToastUtil.show(result.getMsg());
                        }
                    }
                });
    }

    private void addPipeTag() {
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
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("departmentid", departmentId);
        jsonObject.addProperty("pipeid", pipeId);
        jsonObject.addProperty("prixname", prefixName);
        jsonObject.addProperty("tailname", etStationNameNo.getText().toString());
        jsonObject.addProperty("staketype", tvGroundTagType.getText().toString());
        jsonObject.addProperty("mileageinfo", etKgInfo.getText().toString());
        jsonObject.addProperty("cornerinfo", etCorner.getText().toString());
        jsonObject.addProperty("eastlongitude", etLongitude.getText().toString());
        jsonObject.addProperty("northlatitude", etLatitude.getText().toString());
        jsonObject.addProperty("topagraphy", tvLandForm.getText().toString());
        jsonObject.addProperty("locationdesc", etLocation.getText().toString());
        jsonObject.addProperty("pipeowners", approverId);
        jsonObject.addProperty("landinfo", etName.getText().toString());
        jsonObject.addProperty("landtel", etPhone.getText().toString());
        jsonObject.addProperty("remarks", etRemark.getText().toString());
        jsonObject.addProperty("highareasid", highZoneId);
        jsonObject.addProperty("pipeaccountid", tunnelId);
        jsonObject.addProperty("routinginspection", checkStatus);
        jsonObject.addProperty("abovepipe", isOnTop);
        jsonObject.addProperty("perfectflag", isComplete);
        jsonObject.addProperty("landmaterial",tvMaterial.getText().toString());
        if (!TextUtils.isEmpty(photoSb.toString())) {
            jsonObject.addProperty("uploadpicture", photoSb.toString());
        } else {
            jsonObject.addProperty("uploadpicture", "00");
        }
        Log.i("tag", "jsonObject===" + jsonObject);
        Net.create(Api.class).addPipeStakeInfo(token, jsonObject)
                .enqueue(new NetCallback<ServerModel>(this, true) {
                    @Override
                    public void onResponse(ServerModel result) {
                        if (result.getCode() == Constant.SUCCESS_CODE) {
                            ToastUtil.show("????????????");
                            Intent intent = new Intent();
                            intent.setAction("com.action.update");
                            sendBroadcast(intent);
                            finish();
                        } else {
                            ToastUtil.show(result.getMsg());
                        }
                    }
                });
    }

    public boolean paramsComplete() {
        if (TextUtils.isEmpty(tvArea.getText().toString())) {
            ToastUtil.show("??????????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvPipeName.getText().toString())) {
            ToastUtil.show("???????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvGroundTagType.getText().toString())) {
            ToastUtil.show("???????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvUpStationNo.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvDownStationNo.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(tvStationNoPrefix.getText().toString())) {
            ToastUtil.show("????????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etStationNameNo.getText().toString())) {
            ToastUtil.show("????????????????????????");
            return false;
        }

        if (TextUtils.isEmpty(etKgInfo.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (!NumberUtil.isNumber(etKgInfo.getText().toString())) {
            ToastUtil.show("???????????????????????????");
            return false;
        }
        double kgInfo = Double.parseDouble(etKgInfo.getText().toString());
        double upStationKm = Double.parseDouble(tvUpStationKm.getText().toString());
        double downStationKm = Double.parseDouble(tvDownStationKm.getText().toString());
        if (kgInfo < upStationKm || kgInfo > downStationKm) {
            ToastUtil.show("????????????????????????????????????????????????");
            return false;
        }

        if (TextUtils.isEmpty(etCorner.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etLongitude.getText().toString())) {
            ToastUtil.show("???????????????????????????-E(??????)");
            return false;
        }
        if (TextUtils.isEmpty(etLatitude.getText().toString())) {
            ToastUtil.show("??????????????????-N(??????)");
            return false;
        }
        if (TextUtils.isEmpty(tvLandForm.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
        if (TextUtils.isEmpty(etLocation.getText().toString())) {
            ToastUtil.show("?????????????????????");
            return false;
        }
//        if (TextUtils.isEmpty(etDepth.getText().toString())) {
//            ToastUtil.show("?????????????????????");
//            return false;
//        }
//        if (!NumberUtil.isNumber(etDepth.getText().toString())) {
//            ToastUtil.show("???????????????????????????");
//            return false;
//        }
//        if (TextUtils.isEmpty(etName.getText().toString())) {
//            ToastUtil.show("?????????????????????-??????");
//            return false;
//        }
//        if (TextUtils.isEmpty(etPhone.getText().toString())) {
//            ToastUtil.show("?????????????????????-????????????");
//            return false;
//        }
//        if (TextUtils.isEmpty(etRemark.getText().toString())) {
//            ToastUtil.show("???????????????");
//            return false;
//        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == SELECT_STATION) {
            String selectTag = data.getStringExtra("selectTag");
            String stationName = data.getStringExtra("stationName");
            if (!TextUtils.isEmpty(selectTag)) {
                if (selectTag.equals("start")) {
                    tvUpStationNo.setText(stationName);
                    upStationId = data.getStringExtra("stationId");
                    //????????????????????????
                    getStationInfo();
                }
            }
        } else if (requestCode == SELECT_APPROVER) {
            approverName = data.getStringExtra("name");
            approverId = data.getStringExtra("id");
            if (!TextUtils.isEmpty(approverName)) {
                tvPipeManager.setText(approverName);
            }
        }
    }

    private void getStationInfo() {
        JsonObject params = new JsonObject();
        params.addProperty("stakeid", upStationId);
        params.addProperty("ptype", tvGroundTagType.getText().toString());
        Log.i("tag", "params==" + params);
        Net.create(Api.class).getStationInfo(token, params)
                .enqueue(new NetCallback<NextStationModel>(this, true) {
                    @Override
                    public void onResponse(NextStationModel model) {
                        if (model != null) {
                            highZoneId = model.getHighareasid();
                            tunnelId = model.getPipeaccountid();
                            approverId = model.getOwnerid();
                            prefixName = model.getPrixname();
                            tvUpStationKm.setText(model.getStakemile() + "");
                            tvDownStationNo.setText(model.getNextname());
                            tvDownStationKm.setText(model.getNextmile() + "");
                            tvStationNoPrefix.setText(model.getPrixname());
                            tvPipeManager.setText(model.getOwnername());
                            if (tvGroundTagType.getText().equals("?????????")) {
                                etCorner.setEnabled(true);
                            } else {
                                etCorner.setEnabled(false);
                            }
                        }
                    }
                });
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (Build.VERSION.SDK_INT >= 23
                && getApplicationInfo().targetSdkVersion >= 23) {
            if (isNeedCheck) {
                checkPermissions(needPermissions);
            }
        }

        if (Build.VERSION.SDK_INT > 28
                && getApplicationContext().getApplicationInfo().targetSdkVersion > 28) {
            needPermissions = new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE,
                    BACKGROUND_LOCATION_PERMISSION
            };
        }
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                currentAmapLocation = amapLocation;
                if (!isLoactionSuccess) {
                    isLoactionSuccess = true;
                    //?????????????????????????????????????????????
                    amapLocation.getLocationType();//??????????????????????????????????????????????????????????????????????????????
                    amapLocation.getLatitude();//????????????
                    amapLocation.getLongitude();//????????????
                    amapLocation.getAccuracy();//??????????????????
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(amapLocation.getTime());
                    df.format(date);//????????????
                    if ("add".equals(tag)) {
                        etLocation.setText(amapLocation.getAddress());
                        etLongitude.setText(amapLocation.getLongitude() + "");
                        etLatitude.setText(amapLocation.getLatitude() + "");
                    } else if ("update".equals(tag)) {
                        if (TextUtils.isEmpty(stationDetailInfo.getEastlongitude())) {
                            etLongitude.setText(currentAmapLocation.getLongitude() + "");

                        }
                        if (TextUtils.isEmpty(stationDetailInfo.getNorthlatitude())) {
                            etLatitude.setText(currentAmapLocation.getLatitude() + "");
                        }
                        if (TextUtils.isEmpty(stationDetailInfo.getLocationdesc())) {
                            etLocation.setText(currentAmapLocation.getAddress());
                        }
                    }
                }

            } else {
                //??????????????????ErrCode???????????????errInfo???????????????????????????????????????
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }

    /**
     * @param permissions
     * @since 2.5.0
     */
    private void checkPermissions(String... permissions) {
        try {
            if (Build.VERSION.SDK_INT >= 23
                    && getApplicationInfo().targetSdkVersion >= 23) {
                List<String> needRequestPermissonList = findDeniedPermissions(permissions);
                if (null != needRequestPermissonList
                        && needRequestPermissonList.size() > 0) {
                    String[] array = needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]);
                    Method method = getClass().getMethod("requestPermissions", new Class[]{String[].class,
                            int.class});

                    method.invoke(this, array, PERMISSON_REQUESTCODE);
                }
            }
        } catch (Throwable e) {
        }
    }

    /**
     * ?????????????????????????????????????????????
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        if (Build.VERSION.SDK_INT >= 23
                && getApplicationInfo().targetSdkVersion >= 23) {
            try {
                for (String perm : permissions) {
                    Method checkSelfMethod = getClass().getMethod("checkSelfPermission", String.class);
                    Method shouldShowRequestPermissionRationaleMethod = getClass().getMethod("shouldShowRequestPermissionRationale",
                            String.class);
                    if ((Integer) checkSelfMethod.invoke(this, perm) != PackageManager.PERMISSION_GRANTED
                            || (Boolean) shouldShowRequestPermissionRationaleMethod.invoke(this, perm)) {
                        if (!needCheckBackLocation
                                && BACKGROUND_LOCATION_PERMISSION.equals(perm)) {
                            continue;
                        }
                        needRequestPermissonList.add(perm);
                    }
                }
            } catch (Throwable e) {

            }
        }
        return needRequestPermissonList;
    }

    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {
        if (requestCode == PERMISSON_REQUESTCODE) {
            if (!verifyPermissions(paramArrayOfInt)) {
                showMissingPermissionDialog();
                isNeedCheck = false;
            }
        }
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * ??????????????????
     *
     * @since 2.5.0
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("??????");
        builder.setMessage("??????????????????????????????????????????\"??????\"-\"??????\"-?????????????????????");

        // ??????, ????????????
        builder.setNegativeButton("??????",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton("??????",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * ?????????????????????
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mlocationClient != null) {
            mlocationClient.onDestroy();
        }
    }
}
